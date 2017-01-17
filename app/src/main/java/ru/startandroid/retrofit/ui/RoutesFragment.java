package ru.startandroid.retrofit.ui;

import android.content.SharedPreferences;
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

import ru.startandroid.retrofit.Model.routes.Entry;
import ru.startandroid.retrofit.Model.routes.Routes;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.adapter.RoutesRVAdapter;
import ru.startandroid.retrofit.models.NetworkService;
import ru.startandroid.retrofit.presenter.RoutesPresenter;
import ru.startandroid.retrofit.presenter.RoutesPresenterImpl;
import ru.startandroid.retrofit.view.RoutesView;

import static ru.startandroid.retrofit.Const.FLIGHT_POS;
import static ru.startandroid.retrofit.Const.FLIGHT_SHARED_PREF;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoutesFragment extends Fragment implements RoutesView {

    private RecyclerView rvRoutes;
    private TextView tvNoData;
    private ProgressBar progressBar;
    private RoutesPresenter presenter;

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

        presenter = new RoutesPresenterImpl(this, new NetworkService());
        presenter.loadRoutes();

        return viewRoot;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showRoutesData(Routes routes) {

        int pos = 0;
        if (isAdded()) {
            SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences(FLIGHT_SHARED_PREF, 0); // 0 - for private mode
            pos = pref.getInt(FLIGHT_POS, 0);
        }


        Log.d("MainNav", "got to response" + routes.getFlights().get(pos).getItineraryDTO().getEntries().size() + " pos " + pos);

        List<Entry> flights = new ArrayList<>();

        flights.addAll(routes.getFlights().get(pos).getItineraryDTO().getEntries());

        Log.d("MainNav", flights.size() + " size");
        RoutesRVAdapter routesRVAdapter = new RoutesRVAdapter(flights);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvRoutes.setLayoutManager(mLayoutManager);
        rvRoutes.setAdapter(routesRVAdapter);
    }

    @Override
    public void showRoutesEmptyData() {
        tvNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRoutesError(Throwable throwable) {
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
    }
}
