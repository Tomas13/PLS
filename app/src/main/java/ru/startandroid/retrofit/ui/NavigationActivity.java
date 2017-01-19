package ru.startandroid.retrofit.ui;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmQuery;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.startandroid.retrofit.Application;
import ru.startandroid.retrofit.Const;
import ru.startandroid.retrofit.Interface.GitHubService;
import ru.startandroid.retrofit.Model.Datum;
import ru.startandroid.retrofit.Model.Member;
import ru.startandroid.retrofit.Model.routes.Entry;
import ru.startandroid.retrofit.Model.routes.Flight;
import ru.startandroid.retrofit.Model.routes.Routes;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.databinding.ActivityNavigationBinding;
import ru.startandroid.retrofit.models.NetworkService;
import ru.startandroid.retrofit.presenter.NavigationPresenterImpl;
import ru.startandroid.retrofit.presenter.NavitationPresenter;
import ru.startandroid.retrofit.presenter.RoutesPresenter;
import ru.startandroid.retrofit.presenter.RoutesPresenterImpl;
import ru.startandroid.retrofit.utils.KeycloakHelper;
import ru.startandroid.retrofit.view.NavigationActView;
import ru.startandroid.retrofit.view.RoutesView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static ru.startandroid.retrofit.Const.BASE_URL;
import static ru.startandroid.retrofit.Const.FLIGHT_ID;
import static ru.startandroid.retrofit.Const.FLIGHT_NAME;
import static ru.startandroid.retrofit.Const.FLIGHT_POS;
import static ru.startandroid.retrofit.Const.FLIGHT_SHARED_PREF;
import static ru.startandroid.retrofit.Const.LOGIN_PREF;
import static ru.startandroid.retrofit.Const.NAV_SHARED_PREF;
import static ru.startandroid.retrofit.Const.TOKEN;
import static ru.startandroid.retrofit.Const.TOKEN_SHARED_PREF;
import static ru.startandroid.retrofit.Const.Token;
import static ru.startandroid.retrofit.utils.Singleton.getUserClient;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RoutesView, NavigationActView {

    private ActivityNavigationBinding activityNavigationBinding;
    private ProgressBar navProgressBar;
    private TextView tvFirstName;
    private TextView tvLastName, tvRoleName, tvRouteHeader;
    private ArrayList<Routes> routesList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ArrayList<String> flights;
    private int posReturn;
    private List<Entry> entries;
    private List<Flight> flightArrayList;
    private RoutesPresenter routesPresenter;
    private NavitationPresenter navPresenter;
    private Realm realm;
    private Subscription subscription;

    private void runRefreshToken() {
        subscription = Observable.interval(45, TimeUnit.SECONDS)
                .map(aLong -> "HeyRx " + aLong)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::connect,
                        error -> Log.d("MainNavObser", error.getMessage()),
                        () -> Log.d("MainNavObser", "OnCompleted"));
    }

    private void connect(Object object) {

        if (!KeycloakHelper.isConnected()) {
            KeycloakHelper.connect(NavigationActivity.this, new org.jboss.aerogear.android.core.Callback<String>() {
                @Override
                public void onSuccess(String data) {
                    Const.Token = "Bearer " + data;

                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences(TOKEN_SHARED_PREF, 0); // 0 - for private mode

                    //Save Token to shared preferences
                    SharedPreferences.Editor editor1 = pref1.edit();
                    editor1.putString(TOKEN, data);
                    editor1.apply();

                    Log.d("MainNavTimerRX", Const.Token);
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        Log.d("MainApplication", object.toString() + Token); //.getExpires_on());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_navigation);

        activityNavigationBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_navigation);

        Toolbar toolbar = activityNavigationBinding.appBarNavigation.toolbar;
        setSupportActionBar(toolbar);
        setTitle("PLS");

        runRefreshToken();

        routesPresenter = new RoutesPresenterImpl(this, new NetworkService());
        navPresenter = new NavigationPresenterImpl(this, new NetworkService());
        navProgressBar = (ProgressBar) findViewById(R.id.activity_navigation_progressbar);

        tvFirstName = (TextView) activityNavigationBinding.navView.getHeaderView(0).findViewById(R.id.tv_fname);
        tvLastName = (TextView) activityNavigationBinding.navView.getHeaderView(0).findViewById(R.id.tv_lname);
        tvRoleName = (TextView) activityNavigationBinding.navView.getHeaderView(0).findViewById(R.id.tv_role_name);
        tvRouteHeader = (TextView) activityNavigationBinding.navView.getHeaderView(0).findViewById(R.id.tv_route_header);

        // Create the Realm instance
        realm = Realm.getDefaultInstance();
        // Build the query looking at all users:
        RealmQuery<Datum> queryData = realm.where(Datum.class);

        Datum memberData;

        //if we don't have data of user
        if (queryData.findAll().size() == 0) {
            Log.d("Main", "fetching membership info");
            navProgressBar.setVisibility(View.VISIBLE);

            navPresenter.loadMembershipInfo();

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
        //TODO gotta remove shared pref value when needed (onLogout)
        SharedPreferences pref = getApplicationContext().getSharedPreferences(FLIGHT_SHARED_PREF, 0); // 0 - for private mode


        SharedPreferences pref1 = getApplicationContext().getSharedPreferences(NAV_SHARED_PREF, 0); // 0 - for private mode
        if (pref1.contains(FLIGHT_NAME)) {
            tvRouteHeader.setText(pref1.getString(FLIGHT_NAME, "Путь"));
        }

        if (!pref.contains(FLIGHT_POS)) {

//            navProgressBar.setVisibility(View.VISIBLE);
            routesPresenter.loadRoutes();

        } else {

            Log.d("MainNav", "no request getroutesinfo");
            startFragment(new HistoryFragment());

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void getMembershipData(Member member) {
        if (member != null) {

            String firstname = member.getData().get(0).getFirstName();
            String lastname = member.getData().get(0).getLastName();

            realm.beginTransaction();
            realm.insert(member.getData());
            realm.commitTransaction();

            tvFirstName.setText(firstname);
            tvLastName.setText(lastname);

            hideProgress();

//                    HistoryFragment fragment = new HistoryFragment();
//                startFragment(fragment);

            Log.d("Main", Const.Token);
            Log.d("Main", "got here");
        }
    }

    @Override
    public void showMemberEmptyData() {

    }

    @Override
    public void showMemberError(Throwable throwable) {
        Log.d("Main", throwable.getMessage());
        hideProgress();

        Toast.makeText(NavigationActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();

    }

    private void createDialog() {

        final Dialog flightDialog = new Dialog(this);
        flightDialog.setContentView(R.layout.fragment_flight);
        flightDialog.setCancelable(false);

        ListView listView = (ListView) flightDialog.findViewById(R.id.list_view_flight);
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_view_item, flights);
//        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_activated_1, flights);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);

        flightDialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                flightDialog.setTitle(flights.get(position));
                listView.setItemChecked(position, true);

//                Toast.makeText(getApplicationContext(), "Сохраняем " + flights.get(position), Toast.LENGTH_SHORT).show();
                //Save Flight Id to shared preferences
                SharedPreferences pref = getApplicationContext().getSharedPreferences(FLIGHT_SHARED_PREF, 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt(FLIGHT_POS, position);
                editor.putLong(FLIGHT_ID, flightArrayList.get(position).getId()); // ;.getItineraryDTO().getEntries().get(0).getDept().getName());
                editor.apply();

                //Save Flight Id to shared preferences
                SharedPreferences pref1 = getApplicationContext().getSharedPreferences(NAV_SHARED_PREF, 0); // 0 - for private mode
                SharedPreferences.Editor editor1 = pref1.edit();
                editor1.putString(FLIGHT_NAME, flights.get(position));
                editor1.apply();

                tvRouteHeader.setText(pref1.getString(FLIGHT_NAME, "Путь"));

                posReturn = position;

                Toast.makeText(NavigationActivity.this, "Готово, можете нажать кнопку ОК для закрытия диалога", Toast.LENGTH_SHORT).show();

            }
        });

        Button btnOk = (Button) flightDialog.findViewById(R.id.btn_ok_flight);
        btnOk.setOnClickListener(v -> {
            flightDialog.dismiss();

            if (posReturn != 0) {

                entries = flightArrayList.get(posReturn).getItineraryDTO().getEntries();

                realm.beginTransaction();
                realm.insert(entries);
                realm.commitTransaction();

                startFragment(new HistoryFragment());

            } else {
                Toast.makeText(NavigationActivity.this, "Необходимо выбрать путь следования", Toast.LENGTH_SHORT).show();
            }
        });

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
        return true;
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

            startFragment(new AcceptGenInvoiceFragment());

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
        } else {
            // use old hacky way, which can be removed
            // once minSdkVersion goes above 19 in a few years.
            stopSubscription();
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

    void stopSubscription() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            Log.d("MainNav", " Observable stopped");
            subscription.unsubscribe();
        }
    }

    private void clearSharedPrefs() {
        getSharedPreferences(LOGIN_PREF, MODE_PRIVATE).edit().clear().apply();
        getSharedPreferences(NAV_SHARED_PREF, MODE_PRIVATE).edit().clear().apply();
        getSharedPreferences(FLIGHT_SHARED_PREF, MODE_PRIVATE).edit().clear().apply();
        getSharedPreferences(TOKEN_SHARED_PREF, MODE_PRIVATE).edit().clear().apply();
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

            tvRouteHeader.setText(routes.getFlights().get(0).getName());

            //if one route then go to history fragment
            if (routes.getFlights().size() == 1) {

                //Save Flight Id to shared preferences
                SharedPreferences pref = getApplicationContext().getSharedPreferences(FLIGHT_SHARED_PREF, 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putLong(FLIGHT_ID, routes.getFlights().get(0).getId());
                editor.apply();

                //Save Flight Id to shared preferences
                SharedPreferences pref1 = getApplicationContext().getSharedPreferences(NAV_SHARED_PREF, 0); // 0 - for private mode
                SharedPreferences.Editor editor1 = pref1.edit();
                editor1.putString(FLIGHT_NAME, routes.getFlights().get(0).getName());
                editor1.apply();

                entries = routes.getFlights().get(0).getItineraryDTO().getEntries();

                realm.beginTransaction();
                realm.insert(entries);
                realm.commitTransaction();


                startFragment(new HistoryFragment());

            } else {
                Toast.makeText(NavigationActivity.this, routes.getFlights().get(0).getName(), Toast.LENGTH_SHORT).show();

                flights = new ArrayList<String>();
                for (int i = 0; i < routes.getFlights().size(); i++) {
                    flights.add(i, routes.getFlights().get(i).getName());
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
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                         User cancelled the dialog
//                    }

        // Create the AlertDialog object and return it
        builder.create().show();
    }
}
