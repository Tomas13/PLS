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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.birbit.android.jobqueue.JobManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.startandroid.retrofit.AppJobManager;
import ru.startandroid.retrofit.Model.routes.Entry;
import ru.startandroid.retrofit.Model.routes.Routes;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.adapter.RoutesRVAdapter;
import ru.startandroid.retrofit.events.AccessTokenEvent;
import ru.startandroid.retrofit.events.LoadRoutesEvent;
import ru.startandroid.retrofit.events.RoutesEventError;
import ru.startandroid.retrofit.jobs.GetAccessTokenJob;
import ru.startandroid.retrofit.jobs.LoadRoutesJob;
import ru.startandroid.retrofit.view.RoutesView;

import static ru.startandroid.retrofit.Const.AccessTokenConst;
import static ru.startandroid.retrofit.Const.FLIGHT_ID;
import static ru.startandroid.retrofit.Const.FLIGHT_SHARED_PREF;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoutesFragment extends Fragment implements RoutesView {

    @BindView(R.id.rv_routes)
    RecyclerView rvRoutes;

    @BindView(R.id.tv_no_data_routes)
    TextView tvNoData;

    @BindView(R.id.progress_routes)
    ProgressBar progressBar;

    @BindView(R.id.ll_routes)
    LinearLayout linearLayoutHeader;


    private JobManager jobManager;

    public RoutesFragment() {
        // Required empty public constructor
    }

    private void init() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Маршруты");

        jobManager = AppJobManager.getJobManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_routes, container, false);
        ButterKnife.bind(this, viewRoot);

        init();

        showProgress();
        jobManager.addJobInBackground(new GetAccessTokenJob());

        return viewRoot;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccessTokenEvent(AccessTokenEvent accessTokenEvent){
        AccessTokenConst = accessTokenEvent.getLoginResponse().getAccessToken();
        Log.d("Access2Routes", AccessTokenConst);
        jobManager.addJobInBackground(new LoadRoutesJob());
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onLoadRoutesEvent(LoadRoutesEvent routesEvent) {

        hideProgress();

//        int pos = 0;
        long flightId = 0;
        if (isAdded()) {
            SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences(FLIGHT_SHARED_PREF, 0); // 0 - for private mode
//            pos = pref.getInt(FLIGHT_POS, 0);

            flightId = pref.getLong(FLIGHT_ID, 0);
        }

        List<Entry> flights = new ArrayList<>();

        boolean foundOne = false;

        for (int i = 0; i < routesEvent.getRoutes().getFlights().size(); i++) {
            if (routesEvent.getRoutes().getFlights().get(i).getFlight().getId() == flightId && !foundOne){
                flights.addAll(routesEvent.getRoutes().getFlights().get(i).getFlight().getItineraryDTO().getEntries());
                foundOne = true;
            }
        }

//        flights.addAll(routesEvent.getRoutes().getFlights().get(pos).getFlight().getItineraryDTO().getEntries());

        RoutesRVAdapter routesRVAdapter = new RoutesRVAdapter(flights, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvRoutes.setLayoutManager(mLayoutManager);
        rvRoutes.setAdapter(routesRVAdapter);
    }

    @Override
    public void showRoutesEmptyData() {
        tvNoData.setVisibility(View.VISIBLE);
        linearLayoutHeader.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRoutesErrorEvent(RoutesEventError eventError) {
        hideProgress();
        showRoutesError(eventError.error);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRoutesEmptyEvent() {
        hideProgress();
        showRoutesEmptyData();
    }

    @Override
    public void showRoutesError(Throwable throwable) {
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
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

    }

}
