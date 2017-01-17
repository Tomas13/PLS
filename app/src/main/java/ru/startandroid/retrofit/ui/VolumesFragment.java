package ru.startandroid.retrofit.ui;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.startandroid.retrofit.Const;
import ru.startandroid.retrofit.Interface.GitHubService;
import ru.startandroid.retrofit.Model.BodyForCreateInvoice;
import ru.startandroid.retrofit.Model.CreateResponse;
import ru.startandroid.retrofit.Model.Datum;
import ru.startandroid.retrofit.Model.collatedestination.CollateResponse;
import ru.startandroid.retrofit.Model.collatedestination.Dto;
import ru.startandroid.retrofit.Model.collatedestination.Label;
import ru.startandroid.retrofit.Model.collatedestination.Packet;
import ru.startandroid.retrofit.Model.routes.Entry;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.adapter.CollateRVAdapter;
import ru.startandroid.retrofit.models.NetworkService;
import ru.startandroid.retrofit.presenter.InvoicePresenterImpl;
import ru.startandroid.retrofit.presenter.VolumesPresenter;
import ru.startandroid.retrofit.presenter.VolumesPresenterImpl;
import ru.startandroid.retrofit.view.VolumesView;

import static ru.startandroid.retrofit.Const.BASE_URL;
import static ru.startandroid.retrofit.Const.FLIGHT_ID;
import static ru.startandroid.retrofit.Const.FLIGHT_SHARED_PREF;
import static ru.startandroid.retrofit.Const.NAV_SHARED_PREF;
import static ru.startandroid.retrofit.utils.Singleton.getUserClient;


/**
 * A simple {@link Fragment} subclass.
 */
public class VolumesFragment extends Fragment implements VolumesView {

    private List<Long> packetsList = new ArrayList<>();
    private List<Long> labelsList = new ArrayList<>();
    private ArrayList<Label> labelsArrayList = new ArrayList<>();
    private ArrayList<Packet> packetsArrayList = new ArrayList<>();
    private Realm realm;
    private Button btnSendInvoice;
    private TextView tvHeaderHint, tvNoDataVolumes;
    private RealmQuery<Entry> queryData;
    private RealmQuery<Packet> queryPacket;
    private RealmQuery<Label> queryLabel;
    private VolumesPresenter presenter;
    private RecyclerView recyclerViewVolumes;
    private ArrayAdapter<String> adapter;
    private List<Entry> entries;
    private ArrayList<String> flightName;
    private BodyForCreateInvoice body;
    private LinearLayout ll, llbtnHint;

    public VolumesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_volumes, container, false);

        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle("Ёмкости");

        ll = (LinearLayout) rootView.findViewById(R.id.ll);
        llbtnHint = (LinearLayout) rootView.findViewById(R.id.ll_btn_hint);

        tvNoDataVolumes = (TextView) rootView.findViewById(R.id.tv_no_data_volumes);
        recyclerViewVolumes = (RecyclerView) rootView.findViewById(R.id.rv_fragment_volumes);
        btnSendInvoice = (Button) rootView.findViewById(R.id.btn_send_invoice);
        tvHeaderHint = (TextView) rootView.findViewById(R.id.tv_header_hint);

        flightName = new ArrayList<>();
        entries = new ArrayList<>();

        // Create the Realm instance
        realm = Realm.getDefaultInstance();
        // Build the query looking at all users:
        queryData = realm.where(Entry.class);

        queryLabel = realm.where(Label.class);
        queryPacket = realm.where(Packet.class);


        if (queryPacket.findAll().size() > 0 || queryLabel.findAll().size() > 0) {
            inflateWithRealm();
        }


        if (!queryData.findAll().isEmpty()) {
            for (int i = 0; i < queryData.findAll().size(); i++) {
                entries.add(queryData.findAll().get(i));
            }
        }

        for (int i = 1; i < entries.size(); i++) {
            flightName.add(entries.get(i).getDept().getNameRu());
        }

        btnSendInvoice.setOnClickListener(v -> showDialog());

        presenter = new VolumesPresenterImpl(this, new NetworkService());
        presenter.loadGetListForVpn();

        return rootView;
    }

    private void inflateWithRealm() {
        ArrayList<Packet> packetsArrayList = new ArrayList<>();

        final ArrayList<Label> labelsArrayList = new ArrayList<>();
//        labelsArrayList.addAll(response.body().getDto().getLabels());

        if (queryPacket.findAll().size() > 0) {
            packetsArrayList.addAll(queryPacket.findAll());
        }

        if (queryLabel.findAll().size() > 0) {
            labelsArrayList.addAll(queryLabel.findAll());
        }


        objects = new ArrayList<Object>();
        objects.addAll(packetsArrayList);
        objects.addAll(labelsArrayList);
        collateRVAdapter = new CollateRVAdapter(getActivity(), objects, new CollateRVAdapter.OnItemCheckedListener() {
            @Override
            public void onCheckedChanged(View childView, boolean isChecked, int childPosition) {

                if (isChecked) {

/*
                                Object tempPacket = objects.get(childPosition);
                                objects.remove(childPosition);
                                objects.add(0, tempPacket);
                                collateRVAdapter.notifyDataSetChanged();*/


                    if (objects.get(childPosition) instanceof Packet) {
                        packetsList.add(((Packet) objects.get(childPosition)).getId());


//                                    collateRVAdapter.notifyItemMoved(childPosition, 0);
                    } else if (objects.get(childPosition) instanceof Label) {
                        labelsList.add(((Label) objects.get(childPosition)).getId());

                               /*     Label tempLabel = (Label) objects.get(childPosition);
                                    objects.remove(childPosition);
                                    objects.add(0, tempLabel);*/

                    }


                    if (labelsList.isEmpty() && packetsList.isEmpty()) {
                        btnSendInvoice.setVisibility(View.GONE);
                        tvHeaderHint.setVisibility(View.VISIBLE);
                    } else {
                        btnSendInvoice.setVisibility(View.VISIBLE);
                        tvHeaderHint.setVisibility(View.GONE);
                    }

                } else {


                    if (objects.get(childPosition) instanceof Packet) {
                        packetsList.remove(((Packet) objects.get(childPosition)).getId());
                    } else {
                        labelsList.remove(((Label) objects.get(childPosition)).getId());
                    }

                    if (labelsList.isEmpty() && packetsList.isEmpty()) {

                        btnSendInvoice.setVisibility(View.GONE);
                        tvHeaderHint.setVisibility(View.VISIBLE);
                    } else {
                        btnSendInvoice.setVisibility(View.VISIBLE);
                        tvHeaderHint.setVisibility(View.GONE);
                    }
                }
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerViewVolumes.setLayoutManager(mLayoutManager);
        recyclerViewVolumes.setAdapter(collateRVAdapter);

    }


    private void showDialog() {
        final Dialog pointDialog = new Dialog(getContext());
        pointDialog.setContentView(R.layout.fragment_flight);

        ListView listView = (ListView) pointDialog.findViewById(R.id.list_view_flight);
        adapter = new ArrayAdapter<String>(getContext(), R.layout.list_view_item, flightName);
        listView.setAdapter(adapter);

        pointDialog.show();

        listView.setOnItemClickListener((parent, view, position, id) -> {

            SharedPreferences pref1 = getActivity().getSharedPreferences(FLIGHT_SHARED_PREF, 0); // 0 - for private mode

            if (pref1.contains(FLIGHT_ID)) {

                Long flightId = pref1.getLong(FLIGHT_ID, 1L);
                Boolean isDepIndex = true;
                String toDeptIndex = entries.get(position).getDept().getName();
                String fromDeptIndex = entries.get(0).getDept().getName();
                body = new BodyForCreateInvoice(flightId, isDepIndex, toDeptIndex, fromDeptIndex, labelsList, packetsList);


            } else {
                Toast.makeText(getContext(), "Ошибка. Нет flightID", Toast.LENGTH_SHORT).show();
            }


            Toast.makeText(getContext(), "Готово, можете нажать кнопку ОК для закрытия диалога", Toast.LENGTH_SHORT).show();

        });

        Button btnOk = (Button) pointDialog.findViewById(R.id.btn_ok_flight);
        btnOk.setOnClickListener(v -> {
            pointDialog.dismiss();

            retrofitPostCreateInvoice();
        });


        Button btnCancel = (Button) pointDialog.findViewById(R.id.btn_cancel_flight);
        btnCancel.setOnClickListener(v -> {
            pointDialog.dismiss();
        });

    }


    private void retrofitPostCreateInvoice() {
        Retrofit retrofitDestList = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUserClient(Const.Token))
                .build();

        GitHubService gitHubServ = retrofitDestList.create(GitHubService.class);
        gitHubServ.postCreateGeneralInvoice(body).enqueue(new Callback<CreateResponse>() {
            @Override
            public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {

                if (response.body() != null) {

                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals("success")) {
                            Toast.makeText(getContext(), "Общая накладная успешно создана", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(getContext(), response.body().getStatus(), Toast.LENGTH_SHORT).show();
                        }

        //TODO THIS SHOULD UPDATE rv items
/*
                        for (int i = 0; i < packetsList.size(); i++) {

                            if (packetsArrayList.get(i).getPacketListId().equals(String.valueOf(packetsList.get(i)))){
                                packetsArrayList.remove(i);
                            }
                        }

                        for (int i = 0; i < labelsList.size(); i++) {
                            if (labelsArrayList.get(i).getLabelListid().equals(String.valueOf(labelsList.get(i)))){
                                labelsArrayList.remove(i);

                            }
                        }

                        objects.clear();
                        objects.addAll(packetsArrayList);
                        objects.addAll(labelsArrayList);
                        collateRVAdapter.notifyDataSetChanged();*/

//                        ((NavigationActivity) getActivity()).startFragment(new VolumesFragment());
                    }
                }

            }

            @Override
            public void onFailure(Call<CreateResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    ArrayList<Object> objects;
    CollateRVAdapter collateRVAdapter;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!packetsList.isEmpty()) packetsList.clear();
        if (!labelsList.isEmpty()) labelsList.clear();

        if (realm != null && !realm.isClosed()) realm.close();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showVolumesData(CollateResponse volumes) {

        packetsArrayList.addAll(volumes.getDto().getPackets());
        labelsArrayList.addAll(volumes.getDto().getLabels());

        objects = new ArrayList<Object>();
        objects.addAll(packetsArrayList);
        objects.addAll(labelsArrayList);

        collateRVAdapter = new CollateRVAdapter(getActivity(), objects, (childView, isChecked, childPosition) -> {

            if (isChecked) {

                           /* Object tempPacket = objects.get(childPosition);
                            objects.remove(childPosition);
                            objects.add(0, tempPacket);
                            collateRVAdapter.notifyDataSetChanged();*/

                if (objects.get(childPosition) instanceof Packet) {
                    packetsList.add(((Packet) objects.get(childPosition)).getId());


//                                    collateRVAdapter.notifyItemMoved(childPosition, 0);
                } else if (objects.get(childPosition) instanceof Label) {
                    labelsList.add(((Label) objects.get(childPosition)).getId());


                           /*     Label tempLabel = (Label) objects.get(childPosition);
                                objects.remove(childPosition);
                                objects.add(0, tempLabel);*/
                }
//                                    collateRVAdapter.notifyItemMoved(childPosition, 0);
                if (labelsList.isEmpty() && packetsList.isEmpty()) {
                    btnSendInvoice.setVisibility(View.GONE);
                    tvHeaderHint.setVisibility(View.VISIBLE);
                } else {
                    btnSendInvoice.setVisibility(View.VISIBLE);
                    tvHeaderHint.setVisibility(View.GONE);
                }


            } else {

                if (objects.get(childPosition) instanceof Packet) {
                    packetsList.remove(((Packet) objects.get(childPosition)).getId());

                } else {
                    labelsList.remove(((Label) objects.get(childPosition)).getId());
                }

                if (labelsList.isEmpty() && packetsList.isEmpty()) {
                    btnSendInvoice.setVisibility(View.GONE);
                    tvHeaderHint.setVisibility(View.VISIBLE);
                } else {
                    btnSendInvoice.setVisibility(View.VISIBLE);
                    tvHeaderHint.setVisibility(View.GONE);
                }
                //      collateRVAdapter.notifyItemMoved(0, childPosition);
            }

        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerViewVolumes.setLayoutManager(mLayoutManager);
        recyclerViewVolumes.setAdapter(collateRVAdapter);

    }

    @Override
    public void showRoutesEmptyData() {
        ll.setVisibility(View.GONE);
        llbtnHint.setVisibility(View.GONE);
        tvNoDataVolumes.setVisibility(View.VISIBLE);

    }

    @Override
    public void showRoutesError(Throwable throwable) {
        Log.d("MainAccept", throwable.getMessage());
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
