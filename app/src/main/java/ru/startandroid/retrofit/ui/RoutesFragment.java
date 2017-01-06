package ru.startandroid.retrofit.ui;


import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.startandroid.retrofit.Const;
import ru.startandroid.retrofit.Interface.GitHubService;
import ru.startandroid.retrofit.Model.Member;
import ru.startandroid.retrofit.Model.routes.Entry;
import ru.startandroid.retrofit.Model.routes.Flight;
import ru.startandroid.retrofit.Model.routes.Routes;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.adapter.RoutesRVAdapter;

import static ru.startandroid.retrofit.Const.BASE_URL;
import static ru.startandroid.retrofit.Const.FLIGHT_POS;
import static ru.startandroid.retrofit.Const.FLIGHT_SHARED_PREF;
import static ru.startandroid.retrofit.Const.ROUTES;
import static ru.startandroid.retrofit.utils.Singleton.getUserClient;


/**
 * A simple {@link Fragment} subclass.
 */
public class RoutesFragment extends Fragment {


    private RecyclerView rvRoutes;

    private TextView tvNoData;
    private ProgressBar progressBar;


    public RoutesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View viewRoot = inflater.inflate(R.layout.fragment_routes, container, false);

        rvRoutes = (RecyclerView) viewRoot.findViewById(R.id.rv_routes);

        tvNoData = (TextView) viewRoot.findViewById(R.id.tv_no_data_routes);
        progressBar = (ProgressBar) viewRoot.findViewById(R.id.progress_routes);

        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle("Маршруты");


        progressBar.setVisibility(View.VISIBLE);
        getRoutesInfo();


        return viewRoot;
    }


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

                progressBar.setVisibility(View.GONE);

                if (response.body() != null) {

                    if (response.isSuccessful() && response.body().getStatus().equals("success")) {


                        int pos = 0;
                        if (isAdded()) {
                            SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences(FLIGHT_SHARED_PREF, 0); // 0 - for private mode
                            pos = pref.getInt(FLIGHT_POS, 0);
                        }


                        Log.d("MainNav", "got to response" + response.body().getFlights().get(pos).getItineraryDTO().getEntries().size() + " pos " + pos);

                        List<Entry> flights = new ArrayList<>();

                        flights.addAll(response.body().getFlights().get(pos).getItineraryDTO().getEntries());


                        Log.d("MainNav", flights.size() + " size");
                        RoutesRVAdapter routesRVAdapter = new RoutesRVAdapter(flights);

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                        rvRoutes.setLayoutManager(mLayoutManager);
                        rvRoutes.setAdapter(routesRVAdapter);

                    } else {
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onFailure(Call<Routes> call, Throwable t) {
                Log.d("Main", t.getMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
