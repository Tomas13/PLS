package ru.startandroid.retrofit.ui;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baozi.Zxing.CaptureActivity;
import com.baozi.Zxing.ZXingConstants;
import com.birbit.android.jobqueue.JobManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;
import ru.startandroid.retrofit.AppJobManager;
import ru.startandroid.retrofit.Model.IdsCollate;
import ru.startandroid.retrofit.Model.acceptgen.Destination;
import ru.startandroid.retrofit.Model.acceptgen.Example;
import ru.startandroid.retrofit.Model.acceptgen.LabelList;
import ru.startandroid.retrofit.Model.acceptgen.PacketList;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.adapter.CollateRVAdapter;
import ru.startandroid.retrofit.events.AccessTokenCollateEvent;
import ru.startandroid.retrofit.events.AccessTokenEvent;
import ru.startandroid.retrofit.events.CollateEvent;
import ru.startandroid.retrofit.jobs.CollateJob;
import ru.startandroid.retrofit.jobs.GetAccessTokenJob;
import ru.startandroid.retrofit.view.CollateView;

import static ru.startandroid.retrofit.Const.AccessTokenConst;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollateNewFragment extends Fragment implements CollateView {


    @BindView(R.id.rv_collate_new)
    RecyclerView recyclerView;

    @BindView(R.id.btn_scan)
    Button btnScan;

    @BindView(R.id.tv_no_data_accept_gen)
    TextView tvNoDataAcceptGen;

    @BindView(R.id.btn_collate)
    Button btnCollate;

    @BindView(R.id.progress_accept_gen)
    ProgressBar progressAccept;

    @BindView(R.id.et_scan)
    EditText editTextScan;

    private List<Long> ids;
    private List<Long> chosenIds;
    private Realm realm;
    private List<String> generalInvoiceIdsList = new ArrayList<>();
    private RealmQuery<Destination> queryDestination;
    private JobManager jobManager;
    private IdsCollate idsCol;
    ArrayList<Destination> destinationsList = new ArrayList<>();
    CollateRVAdapter collateRVAdapter;

    public CollateNewFragment() {
        // Required empty public constructor
    }

    private void init() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Отдельные накладные");

        ids = new ArrayList<>();
        realm = Realm.getDefaultInstance();
        queryDestination = realm.where(Destination.class);
        chosenIds = new ArrayList<>();

        for (int i = 0; i < queryDestination.findAll().size(); i++) {
            destinationsList.add(queryDestination.findAll().get(i));
            generalInvoiceIdsList.add(queryDestination.findAll().get(i).getDestinationListId());
            ids.add(queryDestination.findAll().get(i).getId());
        }

        initChosenIds();

        jobManager = AppJobManager.getJobManager();
    }

    private void initChosenIds() {
        for (int i = 0; i < queryDestination.findAll().size(); i++) {
            if (queryDestination.findAll().get(i).getIsChecked()){
                chosenIds.add(queryDestination.findAll().get(i).getId());
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collate_new, container, false);
        ButterKnife.bind(this, view);
        init();

        Log.d("CollateNew", "onCreateView Called");
        jobManager.addJobInBackground(new GetAccessTokenJob());

        loadSRealm();

        addTextChangeListener();
        btnCollate.setOnClickListener(v -> onButtonCollateClick());
        btnScan.setOnClickListener(v -> startScanActivity());


        collateRVAdapter = new CollateRVAdapter(getActivity(), destinationsList, (isChecked, position) -> {
            if (isChecked) {
                chosenIds.add(ids.get(position));

                realm.executeTransaction(realm -> {

                    queryDestination.findAll().get(position).setIsChecked(true);
                    destinationsList.get(position).setIsChecked(true);
                });

//                collateRVAdapter.notifyDataSetChanged();

            } else {
                chosenIds.remove(ids.get(position));

                realm.executeTransaction(realm -> {
                    queryDestination.findAll().get(position).setIsChecked(false);

                    destinationsList.get(position).setIsChecked(false);
                });


//                collateRVAdapter.notifyDataSetChanged();

            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(collateRVAdapter);

        return view;

    }

    private void onButtonCollateClick() {
        if (!chosenIds.isEmpty()) {
            progressAccept.setVisibility(View.VISIBLE);
            idsCol = new IdsCollate(chosenIds);
//            presenter.postCollate(idsCol);

            for (int i = 0; i < queryDestination.findAll().size(); i++) {
                for (int j = 0; j < chosenIds.size(); j++) {
                    if (queryDestination.findAll().get(i).getId().equals(chosenIds.get(j))) {
                        setLabelsAndPacketsCollated(i);

                        generalInvoiceIdsList.remove(queryDestination.findAll().get(i).getDestinationListId());
//                        listAdapter.notifyDataSetChanged();

                        int k = i;
                        realm.executeTransaction(realm -> {
                            queryDestination.findAll().get(k).deleteFromRealm();
//                            if (!realm.where(Example.class).findAll().isEmpty())
//                                realm.where(Example.class).findAll().get(k).deleteFromRealm();  //17.02.17
                        });
                    }
                }
            }
            jobManager.addJobInBackground(new CollateJob(idsCol));

            ((NavigationActivity) getActivity()).startFragment(new VolumesFragment());

//            jobManager.addJobInBackground(new GetAccessTokenJob());

            hideProgress();
        } else {
            Toast.makeText(getContext(), "Нечего сличать", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccessTokenEvent(AccessTokenCollateEvent accessTokenEvent) {
        AccessTokenConst = accessTokenEvent.getLoginResponse().getAccessToken();
        Log.d("GetTokCollate", AccessTokenConst);
//        jobManager.addJobInBackground(new CollateJob(idsCol));
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onCollateEvent(CollateEvent collateEvent) {

//        showCollateResponse(collateEvent.getCollateResponse());
        //probably no need for that, to show results

//        Toast.makeText(getContext(), collateEvent.getCollateResponse().getStatus(), Toast.LENGTH_SHORT).show();

       /* for (int i = 0; i < queryDestination.findAll().size(); i++) {
            for (int j = 0; j < chosenIds.size(); j++) {
                if (queryDestination.findAll().get(i).getId().equals(chosenIds.get(j))) {

                    int k = i;

                    realm.executeTransaction(realm -> {
                        queryDestination.findAll().get(k).deleteFromRealm();
                        if (!realm.where(Example.class).findAll().isEmpty())
                            realm.where(Example.class).findAll().get(k).deleteFromRealm();  //17.02.17
                    });
                }
            }
        }*/

//        realm.executeTransaction(realm -> {
//            queryDestination.findAll().deleteAllFromRealm();
//            if (!realm.where(Example.class).findAll().isEmpty()) {
//                realm.where(Example.class).findAll().deleteAllFromRealm();  //17.02.17
//            }
//        });

        ((NavigationActivity) getActivity()).startFragment(new VolumesFragment());
    }

    int y = 0;

    private void addTextChangeListener() {
        editTextScan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().isEmpty()) {
                    for (int i = 0; i < generalInvoiceIdsList.size(); i++) {
                        if (generalInvoiceIdsList.get(i).equals(s.toString())) {
                            y = i;
//                            addToChosenIds(y);
//                            putToFirstPosition(y);
                            setSChecked(y);

                            collateRVAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

    }

    private void setSChecked(int y) {
        realm.executeTransaction(realm -> queryDestination.findAll().get(y).setIsChecked(true));
    }

    private void addToChosenIds(int y) {
        if (!chosenIds.contains(ids.get(y))){
            Log.d("shosen", "add");
            chosenIds.add(ids.get(y));
        }
    }

    private void putToFirstPosition(int y) {
        String temp = generalInvoiceIdsList.get(y);
        generalInvoiceIdsList.remove(y);
        generalInvoiceIdsList.add(0, temp);


        Destination desTemp = destinationsList.get(y);
        destinationsList.remove(y);
        destinationsList.add(0, desTemp);

        collateRVAdapter.notifyDataSetChanged();

    }


    private void setLabelsAndPacketsCollated(int k) {
        Destination destination = queryDestination.findAll().get(k);

        realm.executeTransaction(realm -> {
            if (!destination.getLabelList().isEmpty()) {
                for (int l = 0; l < destination.getLabelList().size(); l++) {
                    destination.getLabelList().get(l).setIsCollated(true);
                }
                realm.copyToRealm(destination.getLabelList());
            }

            if (!destination.getPacketList().isEmpty()) {
                for (int l = 0; l < destination.getPacketList().size(); l++) {
                    destination.getPacketList().get(l).setIsCollated(true);
                }

                realm.copyToRealm(destination.getPacketList());
            }
        });
    }

    private void loadSRealm() {

        ArrayList<LabelList> labels = new ArrayList<LabelList>();

        for (int i = 0; i < queryDestination.findAll().size(); i++) {
            if (queryDestination.findAll().get(i).getLabelList().size() > 0) {
                labels.addAll(queryDestination.findAll().get(i).getLabelList());
            }
        }

        ArrayList<PacketList> packets = new ArrayList<>();

        for (int i = 0; i < queryDestination.findAll().size(); i++) {
            if (queryDestination.findAll().get(i).getPacketList().size() > 0) {
                packets.addAll(queryDestination.findAll().get(i).getPacketList());
            }
        }

        realm.executeTransaction(realm -> {
            realm.insert(packets);
            realm.insert(labels);
        });

    }


    @Override
    public void showRoutesEmptyData() {
        hideProgress();
        btnCollate.setVisibility(View.GONE);
//        tvNoDataAcceptGen.setVisibility(View.VISIBLE);

    }

    @Override
    public void showRoutesError(Throwable throwable) {
        Log.d("MainAccept", throwable.getMessage());
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        progressAccept.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressAccept.setVisibility(View.GONE);
    }

    private void startScanActivity() {
        Intent intent = new Intent(getContext(), CaptureActivity.class);
        intent.putExtra(ZXingConstants.ScanIsShowHistory, true);
        startActivityForResult(intent, ZXingConstants.ScanRequestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case ZXingConstants.ScanRequestCode:
                if (resultCode == ZXingConstants.ScanRequestCode) {
                    String result = data.getStringExtra(ZXingConstants.ScanResult);
                    editTextScan.setText(result);
                } else if (resultCode == ZXingConstants.ScanHistoryResultCode) {
                    String resultHistory = data.getStringExtra(ZXingConstants.ScanHistoryResult);
//                    if (!TextUtils.isEmpty(resultHistory)) {
//                        startActivity(new Intent(MainActivity.this,HistoryActivity.class));
//                    }
                }
                break;
        }
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
}
