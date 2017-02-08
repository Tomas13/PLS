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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
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

    @BindView(R.id.rv_routes)
    RecyclerView rvRoutes;

    @BindView(R.id.tv_no_data_routes)
    TextView tvNoData;

    @BindView(R.id.progress_routes)
    ProgressBar progressBar;

    @BindView(R.id.ll_routes)
    LinearLayout linearLayoutHeader;

    private RoutesPresenter presenter;
    private Realm realm;

    public RoutesFragment() {
        // Required empty public constructor
    }

    private void init(){
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Маршруты");

        realm = Realm.getDefaultInstance();
        presenter = new RoutesPresenterImpl(this, new NetworkService());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View viewRoot = inflater.inflate(R.layout.fragment_routes, container, false);
        ButterKnife.bind(this, viewRoot);

        init();


        if (realm.where(Routes.class).findAll().size() == 0) {
            presenter.loadRoutes();
        } else {
            showRoutesData(realm.where(Routes.class).findAll().first());
        }

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

        if (realm.where(Routes.class).findAll().size() == 0) {

            realm.executeTransaction(realm -> {
                realm.insert(routes);
            });
        }


        int pos = 0;
        if (isAdded()) {
            SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences(FLIGHT_SHARED_PREF, 0); // 0 - for private mode
            pos = pref.getInt(FLIGHT_POS, 0);
        }

        List<Entry> flights = new ArrayList<>();

        flights.addAll(routes.getFlights().get(pos).getFlight().getItineraryDTO().getEntries());

        RoutesRVAdapter routesRVAdapter = new RoutesRVAdapter(flights);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvRoutes.setLayoutManager(mLayoutManager);
        rvRoutes.setAdapter(routesRVAdapter);
    }

    @Override
    public void showRoutesEmptyData() {
        tvNoData.setVisibility(View.VISIBLE);
        linearLayoutHeader.setVisibility(View.GONE);
    }

    @Override
    public void showRoutesError(Throwable throwable) {
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!realm.isClosed()) realm.close();
        presenter.unSubscribe();
    }
}
