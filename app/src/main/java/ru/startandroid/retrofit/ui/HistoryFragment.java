package ru.startandroid.retrofit.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.startandroid.retrofit.AppJobManager;
import ru.startandroid.retrofit.Application;
import ru.startandroid.retrofit.Model.History;
import ru.startandroid.retrofit.Model.LastActions;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.adapter.HistoryRVAdapter;
import ru.startandroid.retrofit.events.HistoryEvent;
import ru.startandroid.retrofit.jobs.GetHistoryJob;
import ru.startandroid.retrofit.models.NetworkService;
import ru.startandroid.retrofit.presenter.HistoryPresenter;
import ru.startandroid.retrofit.presenter.HistoryPresenterImpl;
import ru.startandroid.retrofit.view.HistoryView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements HistoryView {

//    private HistoryPresenter presenter;
    @BindView(R.id.rv_fragment_history)
    RecyclerView rvHistory;

    @BindView(R.id.tv_no_data_history)
    TextView tvNoDataHistory;

    @BindView(R.id.progress_history)
    ProgressBar progressHistory;

    @BindView(R.id.ll_history)
    LinearLayout llHistory;

    private JobManager jobManager;

    private Realm realmHistory;

    public HistoryFragment() {
        // Required empty public constructor
    }

    private void init(){
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Последние действия");

        realmHistory = Realm.getDefaultInstance();
        jobManager = AppJobManager.getJobManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View viewRoot = inflater.inflate(R.layout.fragment_last_actions, container, false);
        ButterKnife.bind(this, viewRoot);

        init();

//        presenter = new HistoryPresenterImpl(this, new NetworkService());
//        presenter.loadHistory();

//        RealmConfiguration historyConfig = new RealmConfiguration.Builder()
//                .name("history.realm")
//                .schemaVersion(1)
//                .deleteRealmIfMigrationNeeded()
//                .build();

        showProgress();
        jobManager.addJobInBackground(new GetHistoryJob());
        return viewRoot;
    }

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onHistoryEvent(HistoryEvent event) {

        showHistoryData(event.getHistory());

        hideProgress();
//        realmHistory.executeTransaction(realm -> {
//            realmHistory.insert(event.history);
//        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHistoryErrorEvent(Throwable message){
        showHistoryError(message);
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

  /*  // This method will be called when a SomeOtherEvent is posted
    @Subscribe
    public void handleSomethingElse(SomeOtherEvent event) {
        doSomethingWith(event);
    }*/

    @Override
    public void showProgress() {
        progressHistory.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressHistory.setVisibility(View.GONE);
    }

    @Override
    public void showHistoryData(LastActions lastActions) {

        final List<History> lastActionsList = new ArrayList<>();

        for (int i = 0; i < lastActions.getHistory().size(); i++) {
            lastActionsList.add(lastActions.getHistory().get(i));
        }

        HistoryRVAdapter historyAdapter = new HistoryRVAdapter(lastActionsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvHistory.setLayoutManager(mLayoutManager);
        rvHistory.setAdapter(historyAdapter);

    }

    @Override
    public void showHistoryEmptyData() {
        llHistory.setVisibility(View.GONE);
        tvNoDataHistory.setVisibility(View.VISIBLE);
    }

    @Override
    public void showHistoryError(Throwable throwable) {
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        presenter.unSubscribe();
    }
}
