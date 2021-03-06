package kz.kazpost.toolpar.ui;


import android.app.Dialog;
import android.content.Context;
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
import kz.kazpost.toolpar.Model.BodyForCreateInvoice;
import kz.kazpost.toolpar.Model.RealmLong;
import kz.kazpost.toolpar.Model.SendInvoice;
import kz.kazpost.toolpar.Model.acceptgen.LabelList;
import kz.kazpost.toolpar.Model.acceptgen.PacketList;
import kz.kazpost.toolpar.Model.routes.Entry;
import kz.kazpost.toolpar.R;
import kz.kazpost.toolpar.adapter.VolumesRVAdapter;
import kz.kazpost.toolpar.base.BaseFragment;
import kz.kazpost.toolpar.view.VolumesView;

import static kz.kazpost.toolpar.Const.CURRENT_ROUTE_POSITION;
import static kz.kazpost.toolpar.Const.FLIGHT_ID;
import static kz.kazpost.toolpar.Const.FLIGHT_SHARED_PREF;
import static kz.kazpost.toolpar.Const.INVOICE_NAME;
import static kz.kazpost.toolpar.Const.INVOICE_PREF;
import static kz.kazpost.toolpar.Const.TRANSPONST_LIST_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class VolumesFragment extends BaseFragment implements VolumesView {

    @BindView(R.id.ll)
    LinearLayout ll;

    @BindView(R.id.ll_btn_hint)
    LinearLayout llbtnHint;

    @BindView(R.id.tv_no_data_volumes)
    TextView tvNoDataVolumes;

    @BindView(R.id.rv_fragment_volumes)
    RecyclerView recyclerViewVolumes;

    @BindView(R.id.btn_attach_to_invoice)
    Button btnAttachToInvoice;

    @BindView(R.id.tv_header_hint)
    TextView tvHeaderHint;

    private List<Long> packetsList = new ArrayList<>();
    private List<Long> labelsList = new ArrayList<>();
    private Realm realm;
    private RealmQuery<PacketList> queryPacket;
    private RealmQuery<LabelList> queryLabel;
    private RealmQuery<SendInvoice> querySendInvoice;
    private List<Entry> entries;
    private ArrayList<String> flightName;
    private BodyForCreateInvoice body;
    private ArrayList<Object> objects;
    private VolumesRVAdapter volumesRVAdapter;
    ArrayList<Object> chosen;

    public VolumesFragment() {
        // Required empty public constructor
    }

    private void init() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Ёмкости");

        flightName = new ArrayList<>();
        entries = new ArrayList<>();
        realm = Realm.getDefaultInstance();
        RealmQuery<Entry> queryData = realm.where(Entry.class);
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

        SharedPreferences ref = getActivity().getSharedPreferences(INVOICE_PREF, Context.MODE_PRIVATE);

        flightName.add("O" + ref.getString(INVOICE_NAME, "default_name"));

        btnAttachToInvoice.setOnClickListener(v -> {

//            if (querySendInvoice.findAll().size() > 0 && querySendInvoice.findAll().last().getBodyForCreateInvoice() != null) {
            if (querySendInvoice.findAll().size() > 0) {
//                flightName.add(querySendInvoice.findAll().last().getBodyForCreateInvoice().getToDepIndex());


                showDialog();

            } else {
                Toast.makeText(getContext(), R.string.no_send_invoice, Toast.LENGTH_SHORT).show();
            }

        });
        chosen = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the login_new for this fragment
        View rootView = inflater.inflate(R.layout.fragment_volumes, container, false);
        ButterKnife.bind(this, rootView);

        init();

        return rootView;
    }

    private void inflateWithRealmNew() {

        List<LabelList> label = new ArrayList<>();
        List<PacketList> packet = new ArrayList<>();


        for (int i = 0; i < queryLabel.findAll().size(); i++) {
            if (queryLabel.findAll().size() > 0 && queryLabel.findAll().get(i).getIsCollated()
                    && !queryLabel.findAll().get(i).getAddedToInvoice()) {
                label.add(queryLabel.findAll().get(i));
            }
        }


        for (int j = 0; j < queryPacket.findAll().size(); j++) {
            if (queryPacket.findAll().size() > 0 && queryPacket.findAll().get(j).getIsCollated()
                    && !queryPacket.findAll().get(j).getAddedToInvoice()) {
                packet.add(queryPacket.findAll().get(j));
            }
        }

        objects = new ArrayList<>();
        objects.addAll(label);
        objects.addAll(packet);
        volumesRVAdapter = createAdapter();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerViewVolumes.setLayoutManager(mLayoutManager);
        recyclerViewVolumes.setAdapter(volumesRVAdapter);

    }

    RealmList<RealmLong> labelLongList = new RealmList<>();
    RealmList<RealmLong> packetLongList = new RealmList<>();

    private void showDialog() {
        final Dialog pointDialog = new Dialog(getContext());
        pointDialog.setContentView(R.layout.fragment_flight);
        pointDialog.setCancelable(false);

        ListView listView = (ListView) pointDialog.findViewById(R.id.list_view_flight);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_view_item, flightName);
//        adapter = new ArrayAdapter<String>(getContext(), android.R.login_new.simple_list_item_activated_1, flightName);
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


                String toDeptIndex = "toDepIndex"; //entries.get(currentRoutePosition + 1).getDept().getName();
                String fromDeptIndex = "fromDepIndex"; //entries.get(currentRoutePosition).getDept().getName();

                if (entries.size() == currentRoutePosition) {
                    toDeptIndex = entries.get(currentRoutePosition - 1).getDept().getName();
                    fromDeptIndex = entries.get(currentRoutePosition - 2).getDept().getName();

                } else if (entries.size() > currentRoutePosition + 1) {


                    if (currentRoutePosition == 0) {
                        toDeptIndex = entries.get(currentRoutePosition + 1).getDept().getName();
                        fromDeptIndex = entries.get(currentRoutePosition).getDept().getName();

                    } else {
                        toDeptIndex = entries.get(currentRoutePosition).getDept().getName();
                        fromDeptIndex = entries.get(currentRoutePosition - 1).getDept().getName();
                    }

                }


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

//                    RealmResults<BodyForCreateInvoice> queryBody = realm.where(BodyForCreateInvoice.class).findAll();
                    realm.executeTransaction(
                            realm -> {
                                realm.copyToRealm(body);
//                                queryBody.deleteAllFromRealm();

                                for (int i = 0; i < queryLabel.findAll().size(); i++) {

                                    for (int j = 0; j < labelLongList.size(); j++) {
                                        if (queryLabel.findAll().get(i).getId().equals(labelLongList.get(j).getaLong())) {
                                            queryLabel.findAll().get(i).setAddedToInvoice(true);
                                        }

                                    }
                                }

                                for (int i = 0; i < queryPacket.findAll().size(); i++) {

                                    for (int j = 0; j < packetLongList.size(); j++) {
                                        if (queryPacket.findAll().get(i).getId().equals(packetLongList.get(j).getaLong())) {
                                            queryPacket.findAll().get(i).setAddedToInvoice(true);
                                        }

                                    }
                                }
                            }
                    );

                    updateItemsRV();
                    pointDialog.dismiss();
                }
        );


        Button btnCancel = (Button) pointDialog.findViewById(R.id.btn_cancel_flight);
        btnCancel.setOnClickListener(v -> {

            realm.executeTransaction(realm -> {
                ///17.02.17
                for (int i = 0; i < queryLabel.findAll().size(); i++) {

                    for (int j = 0; j < labelLongList.size(); j++) {
                        if (queryLabel.findAll().get(i).getId().equals(labelLongList.get(j).getaLong())) {
                            queryLabel.findAll().get(i).setAddedToInvoice(false);
                        }

                    }
                }

                for (int i = 0; i < queryPacket.findAll().size(); i++) {

                    for (int j = 0; j < packetLongList.size(); j++) {
                        if (queryPacket.findAll().get(i).getId().equals(packetLongList.get(j).getaLong())) {
                            queryPacket.findAll().get(i).setAddedToInvoice(false);
                        }

                    }
                }
                ///17.02.17

            });
            pointDialog.dismiss();
        });

    }

    private void updateItemsRV() {
        //update items in rv
        for (int i = 0; i < chosen.size(); i++) {
            objects.remove(chosen.get(i));
        }

        volumesRVAdapter.notifyDataSetChanged();
    }


    private VolumesRVAdapter createAdapter() {

        return new VolumesRVAdapter(getActivity(), objects, (childView, isChecked, childPosition) -> {

            if (isChecked) {

                chosen.add(objects.get(childPosition));

                if (objects.get(childPosition) instanceof PacketList) {
                    packetsList.add(((PacketList) objects.get(childPosition)).getId());
                }

                if (objects.get(childPosition) instanceof LabelList) {
                    labelsList.add(((LabelList) objects.get(childPosition)).getId());
                }

                checkLabelPacketListEmpty();

            } else {

                chosen.remove(objects.get(childPosition));


                if (objects.get(childPosition) instanceof PacketList) {
                    packetsList.remove(((PacketList) objects.get(childPosition)).getId());
                } else {
                    labelsList.remove(((LabelList) objects.get(childPosition)).getId());
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
            btnAttachToInvoice.setVisibility(View.GONE);
            tvHeaderHint.setVisibility(View.VISIBLE);
        } else {
            btnAttachToInvoice.setVisibility(View.VISIBLE);
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

    public void showErrorDialog(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setMessage(error)
                .setPositiveButton(R.string.ok, (dialog, id) -> dialog.dismiss())
                .create()
                .show();
    }

}
