package ru.startandroid.retrofit.ui;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import ru.startandroid.retrofit.Model.collatedestination.Packet;
import ru.startandroid.retrofit.Model.routes.Routes;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.adapter.RoutesRVAdapter;
import ru.startandroid.retrofit.databinding.ActivityNavigationBinding;
import ru.startandroid.retrofit.databinding.NavHeaderNavigationBinding;

import static ru.startandroid.retrofit.Const.FLIGHT_ROUTES;
import static ru.startandroid.retrofit.Const.ROUTES;
import static ru.startandroid.retrofit.utils.Singleton.getUserClient;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ActivityNavigationBinding activityNavigationBinding;

    ProgressBar navProgressBar;
    TextView tvFirstName;
    TextView tvLastName, tvRoleName, tvRouteHeader;
    private ArrayList<Routes> routesList = new ArrayList<>();


    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_navigation);

        activityNavigationBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_navigation);

        Toolbar toolbar = activityNavigationBinding.appBarNavigation.toolbar;
        setSupportActionBar(toolbar);


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
            getMembershipInfo();
        } else {

            Log.d("Main", "no fetching " + "size = " + queryData.findAll().size());

            memberData = queryData.findFirst();
            tvFirstName.setText(memberData.getFirstName());
            tvLastName.setText(memberData.getLastName());
//            tvRoleName.setText(memberData.getRoleName());
           /* for (int i = 0; i < queryData.findAll().size(); i++) {
                memberData = queryData.findAll().get(i)
            }*/
        }


        int position;

        //If VPN didn't choose flight then getRoutesInfo
        //TODO gotta remove shared pref value when needed (onLogout)
        SharedPreferences pref = getApplicationContext().getSharedPreferences("FLIGHT_PREF", 0); // 0 - for private mode



        SharedPreferences pref1 = getApplicationContext().getSharedPreferences("NAV_PREF", 0); // 0 - for private mode
        if (pref1.contains("FLIGHT_NAME")){
            tvRouteHeader.setText(pref1.getString("FLIGHT_NAME", "Путь"));

        }

        if (!pref.contains("FLIGHT_POS")) {
            getRoutesInfo();

        } else {
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


    private void getRoutesInfo() {
        Retrofit retrofitLastActions = new Retrofit.Builder()
                .baseUrl("http://pls-test.kazpost.kz/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUserClient(Const.Token))
                .build();

        GitHubService gitHubServ = retrofitLastActions.create(GitHubService.class);

        final Call<Routes> callEdges =
                gitHubServ.getRoutesInfo();

        callEdges.enqueue(new Callback<Routes>() {
            @Override
            public void onResponse(Call<Routes> call, Response<Routes> response) {

                Log.d("MainNav", "got to response RoutesInfo" + response.body().getFlights().size());


                routesList.add(response.body());


                Log.d("MainNav", "Routes get name" + response.body().getFlights().get(0).getName());


                //Save Flight Id to shared preferences
                SharedPreferences pref1 = getApplicationContext().getSharedPreferences("NAV_PREF", 0); // 0 - for private mode
                SharedPreferences.Editor editor1 = pref1.edit();
                editor1.putString("FLIGHT_NAME", response.body().getFlights().get(0).getName());
                editor1.apply();


                tvRouteHeader.setText(response.body().getFlights().get(0).getName());

                //if one route then go to history fragment
                if (response.body().getFlights().size() == 1) {

                    //Save Flight Id to shared preferences
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("FLIGHT_PREF", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("FLIGHT_ID", response.body().getFlights().get(0).getId());
                    editor.apply();


                    startFragment(new LastActionsFragment());

                } else {
                    Toast.makeText(NavigationActivity.this, response.body().getFlights().get(0).getName(), Toast.LENGTH_SHORT).show();

                    ArrayList<String> flights = new ArrayList<String>();
                    for (int i = 0; i < response.body().getFlights().size(); i++) {
                        flights.add(i, response.body().getFlights().get(i).getName());
                    }

                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("flightsList", flights);



                    FlightFragment dialogFragment = new FlightFragment();

                    dialogFragment.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction().replace(R.id.content_navigation_container,
                            dialogFragment).commit();


                }

//                navProgressBar.setVisibility(View.GONE);

//                LastActionsFragment fragment = new LastActionsFragment();
//                startFragment(fragment);

                Log.d("Main", Const.Token);
            }

            @Override
            public void onFailure(Call<Routes> call, Throwable t) {
                Log.d("Main", t.getMessage());

            }
        });
    }


    private void getMembershipInfo() {
        Retrofit retrofitLastActions = new Retrofit.Builder()
                .baseUrl("http://pls-test.kazpost.kz/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUserClient(Const.Token))
                .build();


        GitHubService gitHubServ = retrofitLastActions.create(GitHubService.class);

        final Call<Member> callEdges =
                gitHubServ.getMembershipInfo();

        callEdges.enqueue(new Callback<Member>() {
            @Override
            public void onResponse(Call<Member> call, Response<Member> response) {

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

            @Override
            public void onFailure(Call<Member> call, Throwable t) {
                Log.d("Main", t.getMessage());

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
            startActivity(new Intent(this, LoginActivity.class));
            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void startFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction().replace(R.id.content_navigation_container,
                fragment).commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!realm.isClosed()) realm.close();
    }

    public void replaceFragments(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_navigation_container, fragment)
                .commit();
    }
}
