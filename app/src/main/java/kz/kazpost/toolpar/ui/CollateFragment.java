package kz.kazpost.toolpar.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import kz.kazpost.toolpar.AppJobManager;
import kz.kazpost.toolpar.Model.IdsCollate;
import kz.kazpost.toolpar.Model.acceptgen.Destination;
import kz.kazpost.toolpar.Model.acceptgen.LabelList;
import kz.kazpost.toolpar.Model.acceptgen.PacketList;
import kz.kazpost.toolpar.R;
import kz.kazpost.toolpar.base.BaseFragment;
import kz.kazpost.toolpar.events.AccessTokenEvent;
import kz.kazpost.toolpar.events.CollateEvent;
import kz.kazpost.toolpar.jobs.CollateJob;
import kz.kazpost.toolpar.jobs.GetAccessTokenJob;
import kz.kazpost.toolpar.view.CollateView;

import static kz.kazpost.toolpar.Const.AccessTokenConst;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollateFragment extends BaseFragment implements CollateView {

    @BindView(R.id.list_view_accept_gen)
    ListView listViewAcceptGen;

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
    private ArrayAdapter<String> listAdapter;
    private List<String> generalInvoiceIdsList = new ArrayList<>();
    int count = 0;
    private RealmQuery<Destination> queryDestination;
    private JobManager jobManager;

    public CollateFragment() {
        // Required empty public constructor
    }

    private void init() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Список S-накладных");

        ids = new ArrayList<>();
        realm = Realm.getDefaultInstance();
        queryDestination = realm.where(Destination.class);
        chosenIds = new ArrayList<>();

        for (int i = 0; i < queryDestination.findAll().size(); i++) {
            generalInvoiceIdsList.add(queryDestination.findAll().get(i).getDestinationListId());
            ids.add(queryDestination.findAll().get(i).getId());
        }

        jobManager = AppJobManager.getJobManager();
    }


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

                for (int i = 0; i < generalInvoiceIdsList.size(); i++) {

                    if (listViewAcceptGen.getChildAt(count - 1) != null) {
                        if (generalInvoiceIdsList.get(i).equals(s.toString())) {

                            count++;
                            String temp = generalInvoiceIdsList.get(i);
                            generalInvoiceIdsList.remove(i);
                            generalInvoiceIdsList.add(0, temp);
                            listAdapter.notifyDataSetChanged();
                            listViewAcceptGen.getChildAt(0).setBackgroundColor(Color.GREEN);
                            listViewAcceptGen.setItemChecked(0, true);

                            editTextScan.setText("");
                            chosenIds.add(ids.get(i));
                        }

                    } else {
                        if (generalInvoiceIdsList.get(i).equals(s.toString())) {

                            count++;
                            String temp = generalInvoiceIdsList.get(i);
                            generalInvoiceIdsList.remove(i);
                            generalInvoiceIdsList.add(0, temp);
                            listAdapter.notifyDataSetChanged();
                            listViewAcceptGen.getChildAt(count - 1).setBackgroundColor(Color.GREEN);
                            listViewAcceptGen.setItemChecked(count - 1, true);

                            editTextScan.setText("");
                            chosenIds.add(ids.get(i));
                        }

                    }
                }
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the login_new for this fragment
        View view = inflater.inflate(R.layout.fragment_collate, container, false);
        ButterKnife.bind(this, view);
        init();

        jobManager.addJobInBackground(new GetAccessTokenJob());

//        showProgress();
        //  presenter.loadDestinationList();

        loadSRealm();

        addTextChangeListener();

        btnCollate.setOnClickListener(v -> onButtonCollateClick());
        btnScan.setOnClickListener(v -> startScanActivity());

        listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice, generalInvoiceIdsList);

        listViewAcceptGen.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

//        ArrayList<String> pickedNames = new ArrayList<>();

        listViewAcceptGen.setOnItemClickListener((parent, view1, position, id1) -> {

            int j = position - listViewAcceptGen.getFirstVisiblePosition();
            if (listViewAcceptGen.getChildAt(j) != null) {
//                if (pickedNames.contains(generalInvoiceIdsList.get(j)) && !listViewAcceptGen.isItemChecked(position)) {
                if (!listViewAcceptGen.isItemChecked(position)) {
                    listViewAcceptGen.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
//                    pickedNames.remove(generalInvoiceIdsList.get(j));
                    listViewAcceptGen.setItemChecked(j, false);

                    chosenIds.remove(ids.get(position));
                } else if (listViewAcceptGen.isItemChecked(position)) {
//                    pickedNames.add(generalInvoiceIdsList.get(j));

                    listViewAcceptGen.setItemChecked(j, true);
                    listViewAcceptGen.getChildAt(j).setBackgroundColor(Color.GREEN);
                    chosenIds.add(ids.get(position));

                }
            }
        });

        listViewAcceptGen.setAdapter(listAdapter);

        return view;
    }

    private void onButtonCollateClick() {
        if (!chosenIds.isEmpty()) {
            progressAccept.setVisibility(View.VISIBLE);
            IdsCollate idsCol = new IdsCollate(chosenIds);
//            presenter.postCollate(idsCol);

            for (int i = 0; i < queryDestination.findAll().size(); i++) {
                for (int j = 0; j < chosenIds.size(); j++) {
                    if (queryDestination.findAll().get(i).getId().equals(chosenIds.get(j))) {
                        setLabelsAndPacketsCollated(i);

                        generalInvoiceIdsList.remove(queryDestination.findAll().get(i).getDestinationListId());
                        listAdapter.notifyDataSetChanged();

                        int k = i;
                        realm.executeTransaction(realm -> {
                            queryDestination.findAll().get(k).deleteFromRealm();
//                            if (!realm.where(Example.class).findAll().isEmpty())
//                                realm.where(Example.class).findAll().get(k).deleteFromRealm();  //17.02.17
                        });
                    }
                }
            }

            ((NavigationActivity) getActivity()).startFragment(new VolumesFragment());


//            jobManager.addJobInBackground(new GetAccessTokenCollateJob());

            jobManager.addJobInBackground(new CollateJob(idsCol));

            hideProgress();

        } else {
            Toast.makeText(getContext(), "Нечего сличать", Toast.LENGTH_SHORT).show();
        }
    }

  /*  @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccessTokenCollateEvent(AccessTokenCollateEvent accessTokenEvent) {
        AccessTokenConst = accessTokenEvent.getLoginResponse().getAccessToken();
        Log.d("GetTokCollate", AccessTokenConst);
        jobManager.addJobInBackground(new CollateJob(idsCol));
    }*/


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccessTokenEvent(AccessTokenEvent accessTokenEvent) {
        AccessTokenConst = accessTokenEvent.getLoginResponse().getAccessToken();
        Log.d("onAccessCollate", AccessTokenConst);
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



/*    @Override
    public void showCollateResponse(CollateResponse collateResponse) {

        ((NavigationActivity) getActivity()).startFragment(new VolumesFragment());
    }

    @Override
    public void showVolumesData(CollateResponse collateResponse) {

        ArrayList<PacketList> packetsArrayList = new ArrayList<>();
        packetsArrayList.addAll(collateResponse.getDto().getPackets());

        ArrayList<LabelList> labelsArrayList = new ArrayList<>();
        labelsArrayList.addAll(collateResponse.getDto().getLabels());

        ArrayList<Object> objects = new ArrayList<Object>();
        objects.addAll(packetsArrayList);
        objects.addAll(labelsArrayList);

        realm.executeTransaction(realm -> {
            realm.insert(labelsArrayList);
            realm.insert(packetsArrayList);
        });

        ((NavigationActivity) getActivity()).startFragment(new VolumesFragment());
    }*/


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