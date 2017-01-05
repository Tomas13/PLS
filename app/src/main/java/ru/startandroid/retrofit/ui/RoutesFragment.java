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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

import static ru.startandroid.retrofit.Const.FLIGHT_POS;
import static ru.startandroid.retrofit.Const.FLIGHT_SHARED_PREF;
import static ru.startandroid.retrofit.Const.ROUTES;
import static ru.startandroid.retrofit.utils.Singleton.getUserClient;


/**
 * A simple {@link Fragment} subclass.
 */
public class RoutesFragment extends Fragment {


    RecyclerView rvRoutes;

    public RoutesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View viewRoot = inflater.inflate(R.layout.fragment_routes, container, false);

        rvRoutes = (RecyclerView) viewRoot.findViewById(R.id.rv_routes);

        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle("Маршруты");


        getRoutesInfo();


        return viewRoot;
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


                int pos = 0;

                if (isAdded()) {

                    SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences(FLIGHT_SHARED_PREF, 0); // 0 - for private mode
                    pos = pref.getInt(FLIGHT_POS, 0);
                }


                Log.d("MainNav", "got to response" + response.body().getFlights().get(pos).getItineraryDTO().getEntries().size() + " pos " + pos);

                List<Entry> flights = new ArrayList<>();

//                for (int i = 0; i < response.body().getFlights().size(); i++) {
                flights.addAll(response.body().getFlights().get(pos).getItineraryDTO().getEntries());
//                }


                Log.d("MainNav", flights.size() + " size");
                RoutesRVAdapter routesRVAdapter = new RoutesRVAdapter(flights);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                rvRoutes.setLayoutManager(mLayoutManager);
                rvRoutes.setAdapter(routesRVAdapter);

               /* rvRoutes.addOnItemTouchListener(new RoutesRVAdapter(getActivity(), flights,
                        new RoutesRVAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View childView, int childAdapterPosition) {

                                Log.d("MainRoute", " pos is " + childAdapterPosition);
                                Toast.makeText(getActivity(), " pos is " + childAdapterPosition, Toast.LENGTH_SHORT).show();

                            }
                        }));*/
            }

            @Override
            public void onFailure(Call<Routes> call, Throwable t) {
                Log.d("Main", t.getMessage());

            }
        });
    }


}
