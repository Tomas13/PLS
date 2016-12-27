package ru.startandroid.retrofit.ui;

import android.app.FragmentManager;
import android.content.Intent;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.startandroid.retrofit.Const;
import ru.startandroid.retrofit.Interface.GitHubService;
import ru.startandroid.retrofit.Model.Member;
import ru.startandroid.retrofit.Model.routes.Routes;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.adapter.RoutesRVAdapter;
import ru.startandroid.retrofit.databinding.ActivityNavigationBinding;
import ru.startandroid.retrofit.databinding.NavHeaderNavigationBinding;

import static ru.startandroid.retrofit.utils.Singleton.getUserClient;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ActivityNavigationBinding activityNavigationBinding;

    ProgressBar navProgressBar;
    TextView tvFirstName;
    TextView tvLastName;
    private ArrayList<Routes> routesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_navigation);

        activityNavigationBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_navigation);

        Toolbar toolbar = activityNavigationBinding.appBarNavigation.toolbar;
        setSupportActionBar(toolbar);



//        getMembershipInfo();

        getRoutesInfo();


        tvFirstName = (TextView) activityNavigationBinding.navView.getHeaderView(0).findViewById(R.id.tv_firstname);
        tvLastName = (TextView) activityNavigationBinding.navView.getHeaderView(0).findViewById(R.id.tv_lastname);


        navProgressBar = (ProgressBar) findViewById(R.id.activity_navigation_progressbar);

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

        Log.d("MainNav", "got to getRoutes");

        GitHubService gitHubServ = retrofitLastActions.create(GitHubService.class);

        final Call<Routes> callEdges =
                gitHubServ.getRoutesInfo();

        callEdges.enqueue(new Callback<Routes>() {
            @Override
            public void onResponse(Call<Routes> call, Response<Routes> response) {

                Log.d("MainNav", "got to response" + response.body().getFlights().size());


                routesList.add(response.body());
//
                //if one route then go to history fragment
                if(response.body().getFlights().size() == 1){
                    startFragment(new LastActionsFragment());

                }else{
                    Toast.makeText(NavigationActivity.this, response.body().getFlights().get(0).getName(), Toast.LENGTH_SHORT).show();

                    ArrayList<String> flights = new ArrayList<String>();
                    for (int i = 0; i < response.body().getFlights().size(); i++) {
                        flights.add(i, response.body().getFlights().get(i).getName());
                    }

                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("flightsList", flights);

                    FlightFragment dialogFragment = new FlightFragment();
                    dialogFragment.setArguments(bundle);

                    startFragment(dialogFragment);

                }
//                String username = response.body().getData().get(0).getUserName();
//                String firstname = response.body().getData().get(0).getFirstName();
//                String lastname = response.body().getData().get(0).getLastName();

                RoutesRVAdapter routesRVAdapter = new RoutesRVAdapter(routesList);


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
}
