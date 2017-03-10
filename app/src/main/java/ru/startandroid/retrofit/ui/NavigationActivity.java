package ru.startandroid.retrofit.ui;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import ru.startandroid.retrofit.Const;
import ru.startandroid.retrofit.Model.Datum;
import ru.startandroid.retrofit.Model.Member;
import ru.startandroid.retrofit.Model.login.LoginResponse;
import ru.startandroid.retrofit.Model.routes.Entry;
import ru.startandroid.retrofit.Model.routes.Flight;
import ru.startandroid.retrofit.Model.routes.Routes;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.models.NetworkService;
import ru.startandroid.retrofit.presenter.LoginPresenter;
import ru.startandroid.retrofit.presenter.LoginPresenterImpl;
import ru.startandroid.retrofit.presenter.NavigationPresenterImpl;
import ru.startandroid.retrofit.presenter.NavitationPresenter;
import ru.startandroid.retrofit.presenter.RoutesPresenter;
import ru.startandroid.retrofit.presenter.RoutesPresenterImpl;
import ru.startandroid.retrofit.utils.KeycloakHelper;
import ru.startandroid.retrofit.view.LoginView;
import ru.startandroid.retrofit.view.NavigationActView;
import ru.startandroid.retrofit.view.RoutesView;

import static ru.startandroid.retrofit.Const.AccessTokenConst;
import static ru.startandroid.retrofit.Const.CURRENT_ROUTE_POSITION;
import static ru.startandroid.retrofit.Const.FLIGHT_ID;
import static ru.startandroid.retrofit.Const.FLIGHT_NAME;
import static ru.startandroid.retrofit.Const.FLIGHT_POS;
import static ru.startandroid.retrofit.Const.FLIGHT_SHARED_PREF;
import static ru.startandroid.retrofit.Const.LOGIN_PREF;
import static ru.startandroid.retrofit.Const.NAV_SHARED_PREF;
import static ru.startandroid.retrofit.Const.NUMBER_OF_CITIES;
import static ru.startandroid.retrofit.Const.PASSWORD;
import static ru.startandroid.retrofit.Const.ACCESS_TOKEN;
import static ru.startandroid.retrofit.Const.TOKEN_SHARED_PREF;
import static ru.startandroid.retrofit.Const.TRANSPONST_LIST_ID;
import static ru.startandroid.retrofit.Const.USERNAME;
import static ru.startandroid.retrofit.Const.passwordConst;
import static ru.startandroid.retrofit.Const.usernameConst;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RoutesView, NavigationActView, LoginView {

    private String accessToken;

    private ProgressBar navProgressBar;
    private TextView tvFirstName;
    private TextView tvLastName, tvRoleName, tvRouteHeader;
    private ArrayList<Routes> routesList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ArrayList<String> flights;
    private int posReturn = -1;
    private List<Entry> entries;
    private List<Flight> flightArrayList;
    private RoutesPresenter routesPresenter;
    private NavitationPresenter navPresenter;
    private Realm realm;
    private LoginPresenter loginPresenter;

    SharedPreferences pref1;
    Toolbar toolbar;

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("PLS");

        pref1 = getApplicationContext().getSharedPreferences(TOKEN_SHARED_PREF, 0);

        loginPresenter = new LoginPresenterImpl(this);
        routesPresenter = new RoutesPresenterImpl(this, new NetworkService());
        navPresenter = new NavigationPresenterImpl(this);
        navProgressBar = (ProgressBar) findViewById(R.id.activity_navigation_progressbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeaderView = navigationView.getHeaderView(0);
        tvFirstName = (TextView) navHeaderView.findViewById(R.id.tv_fname);
        tvLastName = (TextView) navHeaderView.findViewById(R.id.tv_lname);
        tvRoleName = (TextView) navHeaderView.findViewById(R.id.tv_role_name);
        tvRouteHeader = (TextView) navHeaderView.findViewById(R.id.tv_route_header);

        realm = Realm.getDefaultInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String username = pref1.getString(USERNAME, "mLogin");
        String password = pref1.getString(PASSWORD, "mPassword");
        usernameConst = username;
        passwordConst = password;

        showProgress();
        loginPresenter.postLogin(username, password);   //get access token
    }

    private void init2() {
        RealmQuery<Datum> queryData = realm.where(Datum.class);
        Datum memberData;

        //if we don't have data of user
        if (queryData.findAll().size() == 0) {
            Log.d("Main", "fetching membership info");

            showProgress();

            navPresenter.loadMembershipInfo(AccessTokenConst);

        } else {

            navProgressBar.setVisibility(View.GONE);

            Log.d("Main", "no fetching " + "size = " + queryData.findAll().size());

            memberData = queryData.findFirst();
            tvFirstName.setText(memberData.getFirstName());
            tvLastName.setText(memberData.getLastName());
//            tvRoleName.setText(memberData.getRoleName());
           /* for (int i = 0; i < queryData.findAll().size(); i++) {
                memberData = queryData.findAll().get(i)
            }*/
        }

        //If VPN didn't choose flight then getRoutesInfo
        SharedPreferences pref = getApplicationContext().getSharedPreferences(FLIGHT_SHARED_PREF, 0); // 0 - for private mode

        if (!pref.contains(FLIGHT_POS)) {
//            navProgressBar.setVisibility(View.VISIBLE);
            routesPresenter.loadRoutes(AccessTokenConst);

        } else {

            Log.d("MainNav", "no request getroutesinfo");
            startFragment(new HistoryFragment());

        }



        SharedPreferences pref1 = getApplicationContext().getSharedPreferences(NAV_SHARED_PREF, 0); // 0 - for private mode
        if (pref1.contains(FLIGHT_NAME)) {
            tvRouteHeader.setText(pref1.getString(FLIGHT_NAME, "Путь"));
        }

    }

    @Override
    public void getMembershipData(Member member) {
        if (member != null) {
            String firstname = member.getData().get(0).getFirstName();
            String lastname = member.getData().get(0).getLastName();

            realm.executeTransaction(realm -> realm.insert(member.getData()));

            tvFirstName.setText(firstname);
            tvLastName.setText(lastname);
        }
    }

    @Override
    public void showMemberEmptyData() {

    }

    @Override
    public void showMemberError(Throwable throwable) {
        hideProgress();
        Toast.makeText(NavigationActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void createDialog() {

        final Dialog flightDialog = new Dialog(this);
        flightDialog.setContentView(R.layout.fragment_flight);
        flightDialog.setCancelable(false);

        ListView listView = (ListView) flightDialog.findViewById(R.id.list_view_flight);
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_view_item, flights);
//        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_activated_1, flights);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);

        flightDialog.show();

        listView.setOnItemClickListener((parent, view, position, id) -> {

            flightDialog.setTitle(flights.get(position));
            listView.setItemChecked(position, true);

            saveFlightIdToSharedPrefs(position);

            tvRouteHeader.setText(pref1.getString(FLIGHT_NAME, "Путь"));
            posReturn = position;

            Toast.makeText(NavigationActivity.this, "Готово, можете нажать кнопку ОК для закрытия диалога", Toast.LENGTH_SHORT).show();

        });

//        Button btnCancel = (Button) flightDialog.findViewById(R.id.btn_cancel_flight);

        Button btnOk = (Button) flightDialog.findViewById(R.id.btn_ok_flight);
        btnOk.setOnClickListener(v -> {

            if (posReturn != -1) {

                entries = flightArrayList.get(posReturn).getFlight().getItineraryDTO().getEntries();

                realm.executeTransaction(realm -> realm.insert(entries));

                flightDialog.dismiss();

                startFragment(new HistoryFragment());

            } else {
                Toast.makeText(NavigationActivity.this, "Необходимо выбрать путь следования", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void saveFlightIdToSharedPrefs(int position) {
        //Save Flight Id to shared preferences
        SharedPreferences pref = getApplicationContext().getSharedPreferences(FLIGHT_SHARED_PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(FLIGHT_POS, position);
        editor.putLong(FLIGHT_ID, flightArrayList.get(position).getFlight().getId());
        editor.putLong(TRANSPONST_LIST_ID, flightArrayList.get(position).getId());
        editor.putInt(NUMBER_OF_CITIES, flightArrayList.get(position).getFlight().getItineraryDTO().getEntries().size());
        editor.putInt(CURRENT_ROUTE_POSITION, 0);

        editor.apply();

        //Save Flight Id to shared preferences
        SharedPreferences pref1 = getApplicationContext().getSharedPreferences(NAV_SHARED_PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor1 = pref1.edit();
        editor1.putString(FLIGHT_NAME, flights.get(position));
        editor1.apply();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_last_actions) {
            startFragment(new HistoryFragment());
        } else if (id == R.id.nav_volumes) {

            startFragment(new VolumesFragment());

        } else if (id == R.id.nav_note) {

            startFragment(new InvoiceFragment());
        } else if (id == R.id.nav_dest_list) {

            startFragment(new CollateFragment());

        } else if (id == R.id.nav_routes) {

            startFragment(new RoutesFragment());

        } else if (id == R.id.nav_settings) {

            startActivity(new Intent(this, ScannerActivity.class));

        } else if (id == R.id.nav_logout) {


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(NavigationActivity.this);
            // Make us non-modal, so that others can receive touch events.
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

            alertDialog
                    .setTitle(R.string.exit_dialog_text)
                    .setPositiveButton(R.string.ok, ((dialog, which) -> remove()))
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())

                    .create()
                    .show();


//            Token = "";
//
//            isLoggedIn(false);
//            getSharedPreferences(LOGIN_PREF, MODE_PRIVATE).edit().putBoolean(LOGIN_BOOL, false).apply();
//            getSharedPreferences(TOKEN_SHARED_PREF, MODE_PRIVATE).edit().remove(TOKEN).apply();

//            KeycloakHelper.deleteAccount();
//            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void remove() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ((ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE))
                    .clearApplicationUserData(); // note: it has a return value!

            removeRealm();

        } else {
            removeRealm();

            // use old hacky way, which can be removed
            // once minSdkVersion goes above 19 in a few years.
            clearCookies(getApplicationContext());
            clearSharedPrefs();
            KeycloakHelper.remove();
            finish();
            System.exit(0);
        }

    }

    @SuppressWarnings("deprecation")
    public static void clearCookies(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Log.d("MainNav", "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            Log.d("MainNav", "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    private void clearSharedPrefs() {
        getSharedPreferences(LOGIN_PREF, MODE_PRIVATE).edit().clear().apply();
        getSharedPreferences(NAV_SHARED_PREF, MODE_PRIVATE).edit().clear().apply();
        getSharedPreferences(FLIGHT_SHARED_PREF, MODE_PRIVATE).edit().clear().apply();
        getSharedPreferences(TOKEN_SHARED_PREF, MODE_PRIVATE).edit().clear().apply();
    }

    private void removeRealm() {
        if (realm != null && !realm.isClosed()) {

            realm.close();
            Realm.deleteRealm(realm.getConfiguration());

        }
    }

    public void startFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_navigation_container,
                fragment).commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null && !realm.isClosed()) realm.close();

        routesPresenter.unSubscribe();
        routesPresenter.onDestroy();
    }


    @Override
    public void showProgress() {
        navProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        navProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showRoutesData(Routes routes) {

        if (routes != null) {

            Log.d("MainNav", "got to response RoutesInfo" + routes.getFlights().size());

            routesList.add(routes);

            // Create the Realm instance
            realm = Realm.getDefaultInstance();

            tvRouteHeader.setText(routes.getFlights().get(0).getFlight().getName());

            //if one route then go to history fragment
            if (routes.getFlights().size() == 1) {

                flightArrayList = routes.getFlights();

                Log.d("NavFligh", flightArrayList.toString());

                //Save Flight Id to shared preferences
                SharedPreferences pref = getApplicationContext().getSharedPreferences(FLIGHT_SHARED_PREF, 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putLong(FLIGHT_ID, routes.getFlights().get(0).getFlight().getId());
                editor.putLong(TRANSPONST_LIST_ID, flightArrayList.get(0).getId());
                editor.putInt(NUMBER_OF_CITIES, routes.getFlights().get(0).getFlight().getItineraryDTO().getEntries().size());
                editor.putInt(CURRENT_ROUTE_POSITION, 0);


                editor.apply();

                //Save Flight Id to shared preferences
                SharedPreferences pref1 = getApplicationContext().getSharedPreferences(NAV_SHARED_PREF, 0); // 0 - for private mode
                SharedPreferences.Editor editor1 = pref1.edit();
                editor1.putString(FLIGHT_NAME, routes.getFlights().get(0).getFlight().getName());
                editor1.apply();


                entries = routes.getFlights().get(0).getFlight().getItineraryDTO().getEntries();

                realm.executeTransaction(realm -> realm.insert(entries));

                realm.executeTransaction(realm -> realm.insert(routes));

                startFragment(new HistoryFragment());

            } else {

                flights = new ArrayList<>();
                for (int i = 0; i < routes.getFlights().size(); i++) {
                    flights.add(i, routes.getFlights().get(i).getFlight().getName());
                }

//                Bundle bundle = new Bundle();
//                bundle.putStringArrayList("flightsList", flights);

                flightArrayList = routes.getFlights();

                createDialog();
            }

        }
        Log.d("Main", Const.Token);
    }

    @Override
    public void showRoutesEmptyData() {
        //No routes e.g. status = list-emtpy
        tvRouteHeader.setText("");
        showErrorDialog(getString(R.string.empty_routes));
        startFragment(new HistoryFragment());
    }

    @Override
    public void showRoutesError(Throwable throwable) {
        showErrorDialog(throwable.getMessage());
    }

    public void showErrorDialog(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(error)
                .setPositiveButton(R.string.ok, (dialog, id) -> dialog.dismiss());
        builder.create().show();
    }


    @Override
    public void showLoginData(LoginResponse loginResponse) {

        hideProgress();

        accessToken = loginResponse.getAccessToken();
        AccessTokenConst = accessToken;

        pref1.edit().putString(ACCESS_TOKEN, accessToken).apply();

        init2();

    }

    @Override
    public void showLoginEmptyData() {

    }

    @Override
    public void showLoginError(Throwable throwable) {
        Toast.makeText(this, "Неверный логин", Toast.LENGTH_SHORT).show();
    }
}
