package ru.startandroid.retrofit.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jboss.aerogear.android.authentication.AuthenticationModule;
import org.jboss.aerogear.android.authentication.basic.HttpBasicAuthenticationModule;
import org.jboss.aerogear.android.authentication.digest.HttpDigestAuthenticationModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthzSession;
import org.jboss.aerogear.android.pipe.http.HeaderAndBody;
import org.jboss.aerogear.android.pipe.http.HttpException;
import org.jboss.aerogear.android.pipe.module.ModuleFields;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmQuery;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.startandroid.retrofit.Const;
import ru.startandroid.retrofit.Interface.GitHubService;
import ru.startandroid.retrofit.Model.Datum;
import ru.startandroid.retrofit.Model.Member;
import ru.startandroid.retrofit.Model.collatedestination.CollateResponse;
import ru.startandroid.retrofit.Model.routes.Entry;
import ru.startandroid.retrofit.Model.routes.Flight;
import ru.startandroid.retrofit.Model.routes.Routes;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.databinding.ActivityNavigationBinding;
import ru.startandroid.retrofit.utils.KeycloakHelper;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static org.spongycastle.asn1.x500.style.BCStyle.C;
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
import static ru.startandroid.retrofit.utils.KeycloakHelper.AUTHZ_URL;
import static ru.startandroid.retrofit.utils.KeycloakHelper.LOGOUT;
import static ru.startandroid.retrofit.utils.Singleton.getUserClient;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ActivityNavigationBinding activityNavigationBinding;

    ProgressBar navProgressBar;
    TextView tvFirstName;
    TextView tvLastName, tvRoleName, tvRouteHeader;
    private ArrayList<Routes> routesList = new ArrayList<>();

    private boolean isLogged;

    private Realm realm;
    private Observable observable;
    private Subscription subscription;

    private void runRefreshToken() {

        observable = Observable.interval(45, TimeUnit.SECONDS)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long o) {
                        return "Hey " + o;
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());


       subscription = observable.subscribe(new Observable.OnSubscribe() {
            @Override
            public void call(Object o) {


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

                            Log.d("MainNavTimer", Const.Token);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

                Log.d("MainApplication", o.toString() + Token); //.getExpires_on());

//                Const.Token = "Bearer "  + session.getRefreshToken();
            }
        });

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

            getMembershipInfo();
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

            navProgressBar.setVisibility(View.VISIBLE);
            getRoutesInfo();

        } else {

            Log.d("MainNav", "no request getroutesinfo");
            startFragment(new LastActionsFragment());

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    List<Entry> entries;

    List<Flight> flightArrayList;

    private void getRoutesInfo() {
        Retrofit retrofitLastActions = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUserClient(Const.Token))
                .build();

        GitHubService gitHubServ = retrofitLastActions.create(GitHubService.class);

        final Call<Routes> callEdges =
                gitHubServ.getRoutesInfo();

        callEdges.enqueue(new Callback<Routes>() {
            @Override
            public void onResponse(Call<Routes> call, Response<Routes> response) {

                navProgressBar.setVisibility(View.GONE);

                if (response.body() != null) {

                    Log.d("MainNav", "got to response RoutesInfo" + response.body().getFlights().size());

                    routesList.add(response.body());


                    // Create the Realm instance
                    realm = Realm.getDefaultInstance();

                    tvRouteHeader.setText(response.body().getFlights().get(0).getName());

                    //if one route then go to history fragment
                    if (response.body().getFlights().size() == 1) {

                        //Save Flight Id to shared preferences
                        SharedPreferences pref = getApplicationContext().getSharedPreferences(FLIGHT_SHARED_PREF, 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putLong(FLIGHT_ID, response.body().getFlights().get(0).getId());
                        editor.apply();

                        //Save Flight Id to shared preferences
                        SharedPreferences pref1 = getApplicationContext().getSharedPreferences(NAV_SHARED_PREF, 0); // 0 - for private mode
                        SharedPreferences.Editor editor1 = pref1.edit();
                        editor1.putString(FLIGHT_NAME, response.body().getFlights().get(0).getName());
                        editor1.apply();

                        entries = response.body().getFlights().get(0).getItineraryDTO().getEntries();


                        startFragment(new LastActionsFragment());

                    } else {
                        Toast.makeText(NavigationActivity.this, response.body().getFlights().get(0).getName(), Toast.LENGTH_SHORT).show();

                        flights = new ArrayList<String>();
                        for (int i = 0; i < response.body().getFlights().size(); i++) {
                            flights.add(i, response.body().getFlights().get(i).getName());
                        }

                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("flightsList", flights);


                        flightArrayList = response.body().getFlights();


                        createDialog();


//                    FlightFragment dialogFragment = new FlightFragment();
//
//                    dialogFragment.setArguments(bundle);
//
//                    getSupportFragmentManager().beginTransaction().replace(R.id.content_navigation_container,
//                            dialogFragment).commit();


                    }

//                navProgressBar.setVisibility(View.GONE);

//                LastActionsFragment fragment = new LastActionsFragment();
//                startFragment(fragment);
                }
                Log.d("Main", Const.Token);
            }

            @Override
            public void onFailure(Call<Routes> call, Throwable t) {
                Log.d("Main", t.getMessage());
                navProgressBar.setVisibility(View.GONE);

                Toast.makeText(NavigationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getMembershipInfo() {
        Retrofit retrofitLastActions = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUserClient(Const.Token))
                .build();


        GitHubService gitHubServ = retrofitLastActions.create(GitHubService.class);

        final Call<Member> callEdges =
                gitHubServ.getMembershipInfo();

        callEdges.enqueue(new Callback<Member>() {
            @Override
            public void onResponse(Call<Member> call, Response<Member> response) {

                if (response.body() != null) {

                    String username = response.body().getData().get(0).getUserName();
                    String firstname = response.body().getData().get(0).getFirstName();
                    String lastname = response.body().getData().get(0).getLastName();


                    realm.beginTransaction();
                    realm.insert(response.body().getData());
                    realm.commitTransaction();


                    tvFirstName.setText(firstname);
                    tvLastName.setText(lastname);

                    navProgressBar.setVisibility(View.GONE);

                    LastActionsFragment fragment = new LastActionsFragment();
//                startFragment(fragment);

                    Log.d("Main", Const.Token);
                    Log.d("Main", "got here");
                }
            }

            @Override
            public void onFailure(Call<Member> call, Throwable t) {
                Log.d("Main", t.getMessage());
                navProgressBar.setVisibility(View.GONE);

                Toast.makeText(NavigationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    ArrayAdapter<String> adapter;
    ArrayList<String> flights;


    int posReturn;

    private void createDialog() {

        final Dialog flightDialog = new Dialog(this);
        flightDialog.setContentView(R.layout.fragment_flight);

        ListView listView = (ListView) flightDialog.findViewById(R.id.list_view_flight);
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_view_item, flights);
        listView.setAdapter(adapter);

        flightDialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), "Сохраняем " + flights.get(position), Toast.LENGTH_SHORT).show();
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
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flightDialog.dismiss();

                if (posReturn != 0) {

//                    Toast.makeText(NavigationActivity.this, "POS IS " + posReturn, Toast.LENGTH_SHORT).show();

                    entries = flightArrayList.get(posReturn).getItineraryDTO().getEntries();

                    realm.beginTransaction();
                    realm.insert(entries);
                    realm.commitTransaction();

                    startFragment(new LastActionsFragment());


                } else {
                    Toast.makeText(NavigationActivity.this, "Необходимо выбрать путь следования", Toast.LENGTH_SHORT).show();
                }
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
            startFragment(new LastActionsFragment());
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
        stopSubscription();

       clearSharedPrefs();
        KeycloakHelper.remove();
        finish();
        System.exit(0);

    }

    void stopSubscription() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            Log.d("MainNav", " Observable stopped");
            subscription.unsubscribe();
        }
    }

    private void clearSharedPrefs(){
//        getSharedPreferences(LOGIN_PREF, MODE_PRIVATE).edit().clear().apply();
        getSharedPreferences(NAV_SHARED_PREF, MODE_PRIVATE).edit().clear().apply();
        getSharedPreferences(FLIGHT_SHARED_PREF, MODE_PRIVATE).edit().clear().apply();
        getSharedPreferences(TOKEN_SHARED_PREF, MODE_PRIVATE).edit().clear().apply();
    }

    private void isLoggedIn(boolean b) {
        isLogged = b;
    }

    public void startFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction().replace(R.id.content_navigation_container,
                fragment).commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null && !realm.isClosed()) realm.close();

        observable = null;
    }

}
