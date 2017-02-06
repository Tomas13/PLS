package ru.startandroid.retrofit.ui;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;
import ru.startandroid.retrofit.Model.acceptgen.Destination;
import ru.startandroid.retrofit.Model.acceptgen.Example;
import ru.startandroid.retrofit.Model.acceptgen.LabelList;
import ru.startandroid.retrofit.Model.acceptgen.PacketList;
import ru.startandroid.retrofit.Model.collatedestination.CollateResponse;
import ru.startandroid.retrofit.Model.collatedestination.Dto;
import ru.startandroid.retrofit.Model.destinationlist.ResponseDestinationList;

import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.models.NetworkService;
import ru.startandroid.retrofit.models.newOinvoice;
import ru.startandroid.retrofit.presenter.AcceptGenInvoicePresenter;
import ru.startandroid.retrofit.presenter.AcceptGenInvoicePresenterImpl;
import ru.startandroid.retrofit.view.CollateView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollateFragment extends Fragment implements CollateView {

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


    private List<Long> ids;
    private List<Long> chosenIds;
    private Realm realm;
    private ArrayAdapter<String> listAdapter;
    private List<String> generalInvoiceIdsList = new ArrayList<>();
    private AcceptGenInvoicePresenter presenter;
    private Dto collateDtoObject;


    @BindView(R.id.et_scan)
    EditText editTextScan;

    int count = 0;

    private RealmQuery<Destination> queryDestination;

    public CollateFragment() {
        // Required empty public constructor
    }

    private void init() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Список S-накладных");

        ids = new ArrayList<>();
        realm = Realm.getDefaultInstance();
        queryDestination = realm.where(Destination.class);
        presenter = new AcceptGenInvoicePresenterImpl(this, new NetworkService());
        chosenIds = new ArrayList<>();

        for (int i = 0; i < queryDestination.findAll().size(); i++) {
            generalInvoiceIdsList.add(queryDestination.findAll().get(i).getDestinationListId());
            ids.add(queryDestination.findAll().get(i).getId());
        }

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collate, container, false);

        ButterKnife.bind(this, view);

        init();


        if (getArguments() != null) {

            Long id = getArguments().getLong("generalInvoiceId");
            Toast.makeText(getContext(), "id " + id, Toast.LENGTH_SHORT).show();

            progressAccept.setVisibility(View.VISIBLE);
            presenter.retrofitAcceptGeneralInvoice(id);
        } else {
            progressAccept.setVisibility(View.VISIBLE);
            presenter.loadDestinationList();

        }

        addTextChangeListener();

        btnCollate.setOnClickListener(v -> {
            if (!chosenIds.isEmpty()) {
                progressAccept.setVisibility(View.VISIBLE);

//                IdsCollate idsCol = new IdsCollate(chosenIds);
//                presenter.postCollate(idsCol);

                realm.executeTransaction(realm -> {

                    for (int i = 0; i < queryDestination.findAll().size(); i++) {
                        realm.insert(queryDestination.findAll().get(i).getLabelList());
                        realm.insert(queryDestination.findAll().get(i).getPacketList());
                        queryDestination.findAll().deleteFromRealm(i);
                        listAdapter.notifyDataSetChanged();
                    }
                });

            } else {
                Toast.makeText(getContext(), "Нечего сличать", Toast.LENGTH_SHORT).show();
            }
        });

        btnScan.setOnClickListener(v -> startScanActivity());


        //TODO COMMENT THIS OUT
        //  generalInvoiceIdsList.add("First");
//        generalInvoiceIdsList.add("Second");
//        generalInvoiceIdsList.add("Third");
//        generalInvoiceIdsList.add("Four");
//        generalInvoiceIdsList.add("Five");
//        listAdapter = new ArrayAdapter<>(getContext(), R.layout.custom_textview, generalInvoiceIdsList);
        listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice, generalInvoiceIdsList);

        listViewAcceptGen.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        ArrayList<String> pickedNames = new ArrayList<>();

        listViewAcceptGen.setOnItemClickListener((parent, view1, position, id1) -> {

            int j = position - listViewAcceptGen.getFirstVisiblePosition();
            if (listViewAcceptGen.getChildAt(j) != null) {
//                if (pickedNames.contains(generalInvoiceIdsList.get(j)) && !listViewAcceptGen.isItemChecked(position)) {
                if (!listViewAcceptGen.isItemChecked(position)) {
                    listViewAcceptGen.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                    pickedNames.remove(generalInvoiceIdsList.get(j));
                    listViewAcceptGen.setItemChecked(j, false);

                    chosenIds.remove(ids.get(position));
                } else if (listViewAcceptGen.isItemChecked(position)) {
                    pickedNames.add(generalInvoiceIdsList.get(j));

                    listViewAcceptGen.setItemChecked(j, true);
                    listViewAcceptGen.getChildAt(j).setBackgroundColor(Color.GREEN);
                    chosenIds.add(ids.get(position));

                }
            }
        });

        listViewAcceptGen.setAdapter(listAdapter);

        return view;
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
                    if (!TextUtils.isEmpty(resultHistory)) {
//                        startActivity(new Intent(MainActivity.this,HistoryActivity.class));
                    }
                }
                break;
        }
    }

    @Override
    public void showDestinationList(ResponseDestinationList destinationList) {

        for (int i = 0; i < destinationList.getDestinationLists().size(); i++) {
            generalInvoiceIdsList.add(destinationList.getDestinationLists().get(i).getDestinationListId());
            ids.add(destinationList.getDestinationLists().get(i).getId());
        }

        listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, generalInvoiceIdsList);
        listViewAcceptGen.setAdapter(listAdapter);

        Log.d("acceptGen", generalInvoiceIdsList.get(0));
    }


    @Override
    public void showGeneralInvoiceId(Example destinations) {

        for (int i = 0; i < destinations.getDestinations().size(); i++) {
            generalInvoiceIdsList.add(destinations.getDestinations().get(i).getDestinationListId());
            ids.add(destinations.getDestinations().get(i).getId());
        }

        realm.executeTransaction(realm -> {
            realm.insert(destinations);
        });

        listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, generalInvoiceIdsList);
        listViewAcceptGen.setAdapter(listAdapter);
    }

    @Override
    public void showGeneralInvoiceId(newOinvoice newoinvoice) {

        for (int i = 0; i < newoinvoice.getDestinations().size(); i++) {
            generalInvoiceIdsList.add(newoinvoice.getDestinations().get(i).getDestinationListId());
            ids.add(newoinvoice.getDestinations().get(i).getId());
        }

        listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, generalInvoiceIdsList);
        listViewAcceptGen.setAdapter(listAdapter);
    }


    @Override
    public void showCollateResponse(CollateResponse collateResponse) {
        Log.d("MainAccept", "got response");

        Log.d("MainAccept", collateResponse.getStatus());
        Log.d("MainAccept labels", collateResponse.getDto().getLabels().size() + " ");
        Log.d("MainAccept packets", collateResponse.getDto().getPackets().size() + " ");


        collateDtoObject = new Dto();

        collateDtoObject = collateResponse.getDto();

        ArrayList<LabelList> labels = new ArrayList<LabelList>();
        labels.addAll(collateDtoObject.getLabels());

        ArrayList<PacketList> packets = new ArrayList<>();
        packets.addAll(collateDtoObject.getPackets());

        ArrayList<CollateResponse> collateResponsesArrayList = new ArrayList<CollateResponse>();
//                collateResponsesArrayList.addAll(response)


        realm.executeTransaction(realm -> {
            realm.insert(packets);
            realm.insert(labels);
        });

        Log.d("MainAccept", "got response");


        ((NavigationActivity) getActivity()).startFragment(new VolumesFragment());
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
    public void showVolumesData(CollateResponse volumes) {

        Log.d("MainAccept", volumes.getStatus());
        Log.d("MainAccept labels", volumes.getDto().getLabels().size() + " ");
        Log.d("MainAccept packets", volumes.getDto().getPackets().size() + " ");

        ArrayList<PacketList> packetsArrayList = new ArrayList<>();
        packetsArrayList.addAll(volumes.getDto().getPackets());

        ArrayList<LabelList> labelsArrayList = new ArrayList<>();
        labelsArrayList.addAll(volumes.getDto().getLabels());

        ArrayList<Object> objects = new ArrayList<Object>();
        objects.addAll(packetsArrayList);
        objects.addAll(labelsArrayList);

//                    CollateRVAdapter collateRVAdapter = new CollateRVAdapter(objects);


        // Create the Realm instance
        realm = Realm.getDefaultInstance();

        realm.executeTransaction(realm -> {
            realm.insert(labelsArrayList);
            realm.insert(packetsArrayList);
        });

        ((NavigationActivity) getActivity()).startFragment(new VolumesFragment());
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

}
