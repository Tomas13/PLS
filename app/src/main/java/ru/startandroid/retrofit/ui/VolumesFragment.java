package ru.startandroid.retrofit.ui;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import ru.startandroid.retrofit.Model.BodyForCreateInvoice;
import ru.startandroid.retrofit.Model.RealmLong;
import ru.startandroid.retrofit.Model.SendInvoice;
import ru.startandroid.retrofit.Model.acceptgen.LabelList;
import ru.startandroid.retrofit.Model.acceptgen.PacketList;
import ru.startandroid.retrofit.Model.routes.Entry;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.adapter.CollateRVAdapter;
import ru.startandroid.retrofit.view.VolumesView;

import static ru.startandroid.retrofit.Const.CURRENT_ROUTE_POSITION;
import static ru.startandroid.retrofit.Const.FLIGHT_ID;
import static ru.startandroid.retrofit.Const.FLIGHT_SHARED_PREF;
import static ru.startandroid.retrofit.Const.TRANSPONST_LIST_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class VolumesFragment extends Fragment implements VolumesView {

    @BindView(R.id.ll)
    LinearLayout ll;

    @BindView(R.id.ll_btn_hint)
    LinearLayout llbtnHint;

    @BindView(R.id.tv_no_data_volumes)
    TextView tvNoDataVolumes;

    @BindView(R.id.rv_fragment_volumes)
    RecyclerView recyclerViewVolumes;

    @BindView(R.id.btn_send_invoice)
    Button btnSendInvoice;

    @BindView(R.id.tv_header_hint)
    TextView tvHeaderHint;


    private List<Long> packetsList = new ArrayList<>();
    private List<Long> labelsList = new ArrayList<>();
    private Realm realm;
    private RealmQuery<Entry> queryData;
    private RealmQuery<PacketList> queryPacket;
    private RealmQuery<LabelList> queryLabel;
    private RealmQuery<SendInvoice> querySendInvoice;
    private ArrayAdapter<String> adapter;
    private List<Entry> entries;
    private ArrayList<String> flightName;
    private BodyForCreateInvoice body;
    private ArrayList<Object> objects;
    private CollateRVAdapter collateRVAdapter;
    ArrayList<Object> chosen;

    public VolumesFragment() {
        // Required empty public constructor
    }

    private void init() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Ёмкости");

        flightName = new ArrayList<>();
        entries = new ArrayList<>();
        realm = Realm.getDefaultInstance();
        queryData = realm.where(Entry.class);
        queryLabel = realm.where(LabelList.class);
        queryPacket = realm.where(PacketList.class);
        querySendInvoice = realm.where(SendInvoice.class);

        if (!queryLabel.findAll().isEmpty() || !queryPacket.findAll().isEmpty()) {
            inflateWithRealmNew();
        }

        if (!queryData.findAll().isEmpty()) {
            for (int i = 0; i < queryData.findAll().distinct("index").size(); i++) {
                entries.add(queryData.findAll().get(i));
            }
        }

        btnSendInvoice.setOnClickListener(v -> showDialog());
        chosen = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_volumes, container, false);
        ButterKnife.bind(this, rootView);

        init();

        return rootView;
    }

    private void inflateWithRealmNew() {

        List<LabelList> label = new ArrayList<>();
        List<PacketList> packet = new ArrayList<>();


        for (int i = 0; i < queryLabel.findAll().size(); i++) {
            if (queryLabel.findAll().size() > 0 && queryLabel.findAll().get(i).getIsCollated() != null
                    && queryLabel.findAll().get(i).getAddedToInvoice() == null) {
                label.add(queryLabel.findAll().get(i));
            }
        }


        for (int j = 0; j < queryPacket.findAll().size(); j++) {
            if (queryPacket.findAll().size() > 0 && queryPacket.findAll().get(j).getIsCollated() != null
                    && queryPacket.findAll().get(j).getAddedToInvoice() == null) {
                packet.add(queryPacket.findAll().get(j));
            }
        }

        objects = new ArrayList<>();
        objects.addAll(label);
        objects.addAll(packet);
        collateRVAdapter = createAdapter();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerViewVolumes.setLayoutManager(mLayoutManager);
        recyclerViewVolumes.setAdapter(collateRVAdapter);

    }


    private void showDialog() {
        final Dialog pointDialog = new Dialog(getContext());
        pointDialog.setContentView(R.layout.fragment_flight);
        pointDialog.setCancelable(false);

        ListView listView = (ListView) pointDialog.findViewById(R.id.list_view_flight);


        if (!querySendInvoice.findAll().isEmpty()) {
            flightName.add(querySendInvoice.findAll().last().getBodyForCreateInvoice().getToDepIndex());
        }

        adapter = new ArrayAdapter<>(getContext(), R.layout.list_view_item, flightName);
//        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_activated_1, flightName);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);

        pointDialog.show();

        listView.setOnItemClickListener((parent, view, position, id) -> {

            pointDialog.setTitle(flightName.get(position));
            listView.setItemChecked(position, true);

            SharedPreferences pref1 = getActivity().getSharedPreferences(FLIGHT_SHARED_PREF, 0); // 0 - for private mode
            int currentRoutePosition = pref1.getInt(CURRENT_ROUTE_POSITION, 0);

            if (pref1.contains(FLIGHT_ID)) {

                Long flightId = pref1.getLong(FLIGHT_ID, 0);
                Long tlid = pref1.getLong(TRANSPONST_LIST_ID, 0);

                String toDeptIndex = entries.get(currentRoutePosition + 1).getDept().getName();
                String fromDeptIndex = entries.get(currentRoutePosition).getDept().getName();

                RealmList<RealmLong> labelLongList = new RealmList<>();
                RealmList<RealmLong> packetLongList = new RealmList<>();

                for (int i = 0; i < labelsList.size(); i++) {
                    RealmLong realmLong = new RealmLong(labelsList.get(i));
                    labelLongList.add(realmLong);
                }

                for (int i = 0; i < packetsList.size(); i++) {
                    RealmLong realmLong = new RealmLong(packetsList.get(i));
                    packetLongList.add(realmLong);
                }

                body = new BodyForCreateInvoice(flightId, tlid, true, toDeptIndex, fromDeptIndex, labelLongList, packetLongList);


//                bodyWithout = new BodyForCreateInvoiceWithout(flightId, tlid, true, toDeptIndex, fromDeptIndex, labelsList, packetsList);


            } else {
                Toast.makeText(getContext(), "Ошибка. Нет flightID", Toast.LENGTH_SHORT).show();
            }


        });

        Button btnOk = (Button) pointDialog.findViewById(R.id.btn_ok_flight);
        btnOk.setOnClickListener(v -> {

                    RealmResults<BodyForCreateInvoice> queryBody = realm.where(BodyForCreateInvoice.class).findAll();
                    realm.executeTransaction(
                            realm -> {
                                realm.insertOrUpdate(body);
//                                queryBody.deleteAllFromRealm();
                            }

                    );

                    updateItemsRV();

                    pointDialog.dismiss();

                }

        );


        Button btnCancel = (Button) pointDialog.findViewById(R.id.btn_cancel_flight);
        btnCancel.setOnClickListener(v -> pointDialog.dismiss());

    }

    private void updateItemsRV() {
        //update items in rv
        for (int i = 0; i < chosen.size(); i++) {
            objects.remove(chosen.get(i));
        }

        collateRVAdapter.notifyDataSetChanged();

    }


    private CollateRVAdapter createAdapter() {

        return new CollateRVAdapter(getActivity(), objects, (childView, isChecked, childPosition) -> {

            if (isChecked) {

                chosen.add(objects.get(childPosition));

                if (objects.get(childPosition) instanceof PacketList) {
                    packetsList.add(((PacketList) objects.get(childPosition)).getId());
                    realm.executeTransaction(realm1 -> {

                        ((PacketList) objects.get(childPosition)).setAddedToInvoice(true);
                    });
                }

                if (objects.get(childPosition) instanceof LabelList) {
                    labelsList.add(((LabelList) objects.get(childPosition)).getId());
                    realm.executeTransaction(realm1 -> {
                        ((LabelList) objects.get(childPosition)).setAddedToInvoice(true);
                    });
                }

                checkLabelPacketListEmpty();

            } else {

                chosen.remove(objects.get(childPosition));


                if (objects.get(childPosition) instanceof PacketList) {
                    packetsList.remove(((PacketList) objects.get(childPosition)).getId());
                    realm.executeTransaction(realm1 -> {
                        ((PacketList) objects.get(childPosition)).setAddedToInvoice(false);
                    });
                } else {
                    labelsList.remove(((LabelList) objects.get(childPosition)).getId());
                    realm.executeTransaction(realm1 -> {
                        ((LabelList) objects.get(childPosition)).setAddedToInvoice(false);
                    });
                }

                checkLabelPacketListEmpty();
            }
        });
    }


    private void checkLabelPacketListEmpty() {
        if (labelsList.isEmpty() && packetsList.isEmpty()) {
            hideButtonShowText(true);
        } else {
            hideButtonShowText(false);
        }
    }

    private void hideButtonShowText(boolean b) {
        if (b) {
            btnSendInvoice.setVisibility(View.GONE);
            tvHeaderHint.setVisibility(View.VISIBLE);
        } else {
            btnSendInvoice.setVisibility(View.VISIBLE);
            tvHeaderHint.setVisibility(View.GONE);
        }
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
        showErrorDialog(throwable.getMessage());
    }

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


    public void showErrorDialog(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(error)
                .setPositiveButton(R.string.ok, (dialog, id) -> dialog.dismiss());
        builder
                .create().show();
    }

}
