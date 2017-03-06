package ru.startandroid.retrofit.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.birbit.android.jobqueue.JobManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import ru.startandroid.retrofit.AppJobManager;
import ru.startandroid.retrofit.Model.BodyForCreateInvoice;
import ru.startandroid.retrofit.Model.BodyForCreateInvoiceWithout;
import ru.startandroid.retrofit.Model.CreateResponse;
import ru.startandroid.retrofit.Model.RealmLong;
import ru.startandroid.retrofit.Model.SendInvoice;
import ru.startandroid.retrofit.Model.acceptgen.Example;
import ru.startandroid.retrofit.Model.acceptgen.LabelList;
import ru.startandroid.retrofit.Model.acceptgen.PacketList;
import ru.startandroid.retrofit.Model.geninvoice.GeneralInvoice;
import ru.startandroid.retrofit.Model.geninvoice.InvoiceMain;
import ru.startandroid.retrofit.Model.routes.Entry;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.adapter.InvoiceRVAdapter;
import ru.startandroid.retrofit.adapter.InvoiceRVAdapterSend;
import ru.startandroid.retrofit.events.AcceptEmptyEvent;
import ru.startandroid.retrofit.events.AcceptGenInvoiceEvent;
import ru.startandroid.retrofit.events.InvoiceEvent;
import ru.startandroid.retrofit.events.ShowGeneralInvoiceEvent;
import ru.startandroid.retrofit.jobs.PostCreateInvoiceJob;
import ru.startandroid.retrofit.models.NetworkService;
import ru.startandroid.retrofit.presenter.InvoicePresenter;
import ru.startandroid.retrofit.presenter.InvoicePresenterImpl;
import ru.startandroid.retrofit.view.InvoiceView;

import static ru.startandroid.retrofit.Const.CURRENT_ROUTE_POSITION;
import static ru.startandroid.retrofit.Const.FLIGHT_ID;
import static ru.startandroid.retrofit.Const.FLIGHT_SHARED_PREF;
import static ru.startandroid.retrofit.Const.INVOICE_NAME;
import static ru.startandroid.retrofit.Const.INVOICE_PREF;
import static ru.startandroid.retrofit.Const.NUMBER_OF_CITIES;
import static ru.startandroid.retrofit.Const.TRANSPONST_LIST_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class InvoiceFragment extends Fragment implements InvoiceView {

    @BindView(R.id.tv_no_data_invoice)
    TextView tvNoDataInvoice;

    @BindView(R.id.tablerow_invoice)
    TableRow tableRowInvoice;

    @BindView(R.id.progress_invoice)
    ProgressBar progressInvoice;

    @BindView(R.id.rv_invoice_fragment)
    RecyclerView rvInvoice;

    @BindView(R.id.rv_send_invoice)
    RecyclerView rvSendInvoice;

    @BindString(R.string.last_point)
    String lastPoint;

    private InvoiceRVAdapter invoiceRVAdapter;
    private InvoicePresenter presenter;
    private Realm realm;
    private List<SendInvoice> sendInvoiceList;
    private List<Entry> entries = new ArrayList<>();
    private RealmQuery<Entry> queryData;
    private ArrayList<String> flightName = new ArrayList<>();
    private AlertDialog alertDialog;
    private RealmQuery<BodyForCreateInvoice> queryBody;
    private int currentRoutePosition;
    private int maxRouteNumber;
    private SharedPreferences pref;
    private BodyForCreateInvoiceWithout body;
    private BodyForCreateInvoice bodyRealm;
    private JobManager jobManager;

    InvoiceRVAdapterSend adapterSend;

    public InvoiceFragment() {
        // Required empty public constructor
    }

    private void init() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Накладные");

        sendInvoiceList = new ArrayList<>();
        realm = Realm.getDefaultInstance();
        realm.setAutoRefresh(true);

        RealmResults<SendInvoice> realmResults = realm.where(SendInvoice.class).findAll();
        if (realmResults.size() > 0) sendInvoiceList.add(realmResults.last());
//        for (int i = 0; i < realmResults.size(); i++) {
//            sendInvoiceList.add(realmResults.get(i));
//        }

        presenter = new InvoicePresenterImpl(this, new NetworkService());

        pref = getActivity().getApplicationContext().getSharedPreferences(FLIGHT_SHARED_PREF, 0); // 0 - for private mode
        maxRouteNumber = pref.getInt(NUMBER_OF_CITIES, 0);
        currentRoutePosition = pref.getInt(CURRENT_ROUTE_POSITION, 1);

        jobManager = AppJobManager.getJobManager();

        //FOR SENDING
        queryData = realm.where(Entry.class);
        if (!queryData.findAll().isEmpty()) {
            for (int i = 0; i < queryData.findAll().distinct("index").size(); i++) {
                entries.add(queryData.findAll().get(i));
            }
        }
        for (int i = 0; i < entries.size(); i++) {
            flightName.add(entries.get(i).getDept().getNameRu());
//            flightName.add(timeOfEvent);
        }


        queryBody = realm.where(BodyForCreateInvoice.class);
        for (int i = 0; i < queryBody.findAll().size(); i++) {
            bodyRealm = queryBody.findAll().last();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_invoice, container, false);
        ButterKnife.bind(this, viewRoot);
        init();

        if (queryBody.findAll().size() == 0) prepareBodyForPost();

        if (!sendInvoiceList.isEmpty()) {

            adapterSend = new InvoiceRVAdapterSend(getActivity(), sendInvoiceList, ((childView, childAdapterPosition) -> {

                if (queryBody.findAll().size() > 0) body = realmToBody(bodyRealm);

                //TODO
                sendInvoiceList.remove(childAdapterPosition);
                adapterSend.notifyDataSetChanged();
                //TODO

                showProgress();

                jobManager.addJobInBackground(new PostCreateInvoiceJob(body));

                updateCurrentRoutePosition();

                removeSentPacketsAndLabels();

//                removeRealm();

                realm.executeTransaction(realm -> realm.where(RealmLong.class).findAll().deleteAllFromRealm());

//                presenter.postCreateInvoice(body);

            }));

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            rvSendInvoice.setLayoutManager(mLayoutManager);
            rvSendInvoice.setAdapter(adapterSend);
        } else {
            realm.executeTransaction(realm -> {
                realm.where(SendInvoice.class).findAll().deleteAllFromRealm();
                realm.where(BodyForCreateInvoice.class).findAll().deleteAllFromRealm();
//                    queryBody.findAll().deleteAllFromRealm();
            });
        }

        presenter.loadGeneralInvoice();

//        jobManager.addJobInBackground(new LoadGeneralInvoiceJob());

        return viewRoot;
    }

    private void removeSentPacketsAndLabels() {
        RealmResults<BodyForCreateInvoice> bodyResults = realm.where(BodyForCreateInvoice.class).findAll();

        ///16.02.17 remove  labels that were sent
        List<Long> listLongs = new ArrayList<>();
        for (int k = 0; k < bodyResults.size(); k++) {
            RealmList<RealmLong> realmLongs = bodyResults.get(k).getLabelIds();
            for (int i = 0; i < realmLongs.size(); i++) {
                listLongs.add(realmLongs.get(i).getaLong());
            }
        }


        ArrayList<Integer> labelIds = new ArrayList<>();

        //remove label from realm after send
        for (int j = 0; j < realm.where(LabelList.class).findAll().size(); j++) {
            for (int i = 0; i < listLongs.size(); i++) {
                if (realm.where(LabelList.class).findAll().get(j).getId().equals(listLongs.get(i))) {
                    labelIds.add(j);
                }
            }
        }

        for (int w = labelIds.size() - 1; w >= 0; w--) {
            int j = labelIds.get(w);
            realm.executeTransaction(realm -> realm.where(LabelList.class).findAll().get(j).deleteFromRealm());

        }


        List<Long> listLongsPacket = new ArrayList<>();
        for (int k = 0; k < bodyResults.size(); k++) {
            RealmList<RealmLong> realmLongsPacket = bodyResults.get(k).getPacketIds();
            for (int i = 0; i < realmLongsPacket.size(); i++) {
                listLongsPacket.add(realmLongsPacket.get(i).getaLong());
            }
        }

        ArrayList<Integer> packetIds = new ArrayList<>();


        for (int j = 0; j < realm.where(PacketList.class).findAll().size(); j++) {
            for (int i = 0; i < listLongsPacket.size(); i++) {
                if (realm.where(PacketList.class).findAll().get(j).getId().equals(listLongsPacket.get(i))) {
                    packetIds.add(j);
                }
            }

        }
        ///16.02.17
        for (int w = packetIds.size() - 1; w >= 0; w--) {
            int j = packetIds.get(w);
            realm.executeTransaction(realm -> realm.where(PacketList.class).findAll().get(j).deleteFromRealm());
        }
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onPostCreateInvoiceJob(InvoiceEvent invoiceEvent) {
        if (invoiceEvent.getCreateResponse() != null) {
            if (invoiceEvent.getCreateResponse().getStatus().equals("success")) {

                Toast.makeText(getContext(), "Общая накладная успешно создана", Toast.LENGTH_SHORT).show();

                hideProgress();
                removeRealm();


            } else {
                showEmptyToast(invoiceEvent.getCreateResponse().getStatus());
            }
        }
    }

    private void createEmptyInvoice() {


        if (flightName.size() >= currentRoutePosition + 1) {

            SendInvoice sendInvoice;

            if (flightName.size() == currentRoutePosition + 1) {
                String toName = flightName.get(currentRoutePosition);
                String fromName = flightName.get(currentRoutePosition - 1);
                sendInvoice = new SendInvoice();
                sendInvoice.setTo(toName);
                sendInvoice.setFrom(fromName);
//            sendInvoice.setBodyForCreateInvoice(bodyRealm);

            } else {


                String toName = flightName.get(currentRoutePosition + 1);
                String fromName = flightName.get(currentRoutePosition);
                sendInvoice = new SendInvoice();
                sendInvoice.setTo(toName);
                sendInvoice.setFrom(fromName);
//            sendInvoice.setBodyForCreateInvoice(bodyRealm);
            }

            realm.executeTransaction(realm -> realm.insert(sendInvoice));

            if (currentRoutePosition > 0 && currentRoutePosition < maxRouteNumber) {
                currentRoutePosition++;
                pref.edit().putInt(CURRENT_ROUTE_POSITION, currentRoutePosition).apply();
            }

        } else {
            Toast.makeText(getContext(), "END OF ROUTE", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void showGeneralInvoice(InvoiceMain invoiceMain) {

        List<GeneralInvoice> generalInvoiceList = new ArrayList<>();
        generalInvoiceList.addAll(invoiceMain.getGeneralInvoices());

        invoiceRVAdapter = new InvoiceRVAdapter(getActivity(), generalInvoiceList, (childView, childAdapterPosition) -> {

            if (currentRoutePosition < flightName.size()) {    //if all current route points are passed, we can't accept new O-invoice

                Long generalInvoiceId = generalInvoiceList.get(childAdapterPosition).getId();
                presenter.retrofitAcceptGeneralInvoice(generalInvoiceId);

                showProgress();
//            jobManager.addJobInBackground(new AcceptGeneralInvoiceJob(generalInvoiceId));

                createEmptyInvoice();

                generalInvoiceList.remove(childAdapterPosition);
                invoiceRVAdapter.notifyDataSetChanged();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.FRANCE);
                Date now = new Date();
                String timeOfEvent = simpleDateFormat.format(now);


                SharedPreferences ref = getActivity().getSharedPreferences(INVOICE_PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = ref.edit();
                editor.putString(INVOICE_NAME, timeOfEvent);
                editor.apply();
            } else {
                Snackbar snackbar = Snackbar.make(rvInvoice, lastPoint, Snackbar.LENGTH_LONG);

                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setMaxLines(2);  // show multiple line
                snackbar.show();
            }
            //startCollateFragment();
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvInvoice.setLayoutManager(mLayoutManager);
        rvInvoice.setAdapter(invoiceRVAdapter);


    }


    private BodyForCreateInvoiceWithout realmToBody(BodyForCreateInvoice bodyRealm) {

        BodyForCreateInvoiceWithout bodyForCreateInvoiceWithout = new BodyForCreateInvoiceWithout();

        bodyForCreateInvoiceWithout.setFlightId(bodyRealm.getFlightId());
        bodyForCreateInvoiceWithout.setFromDepIndex(bodyRealm.getFromDepIndex());
        bodyForCreateInvoiceWithout.setIsDepIndex(bodyRealm.getIsDepIndex());
        bodyForCreateInvoiceWithout.setToDepIndex(bodyRealm.getToDepIndex());
        bodyForCreateInvoiceWithout.setTlId(bodyRealm.getTlId());

        if (bodyRealm.getLabelIds().size() > 0) {

            RealmList<RealmLong> labelL = bodyRealm.getLabelIds();
            List<Long> labelLong = new ArrayList<>();
            for (int i = 0; i < labelL.size(); i++) {
                RealmLong realmLong = labelL.get(i);
                labelLong.add(realmLong.getaLong());
            }
            bodyForCreateInvoiceWithout.setLabelIds(labelLong);
        } else {
            bodyForCreateInvoiceWithout.setLabelIds(new ArrayList<>());

        }


        if (bodyRealm.getPacketIds().size() > 0) {

            RealmList<RealmLong> packetL = bodyRealm.getPacketIds();
            List<Long> packetLong = new ArrayList<>();

            for (int i = 0; i < packetL.size(); i++) {
                RealmLong realmLong = packetL.get(i);

                packetLong.add(realmLong.getaLong());
            }

            bodyForCreateInvoiceWithout.setPacketIds(packetLong);
        } else {
            bodyForCreateInvoiceWithout.setPacketIds(new ArrayList<>());

        }

        return bodyForCreateInvoiceWithout;
    }

    public void prepareBodyForPost() {

        SharedPreferences pref1 = getActivity().getSharedPreferences(FLIGHT_SHARED_PREF, 0); // 0 - for private mode
        if (pref1.contains(FLIGHT_ID) && pref1.contains(TRANSPONST_LIST_ID)) {

            Long flightId = pref1.getLong(FLIGHT_ID, 0);
            Long tlid = pref1.getLong(TRANSPONST_LIST_ID, 0);

            ArrayList<Long> labelsList = new ArrayList<>();
            ArrayList<Long> packetsList = new ArrayList<>();

            String toDeptIndex = "InvoiceFrag 434";
            String fromDeptIndex = "FromDep 435";

            if (entries.size() == currentRoutePosition){
                    toDeptIndex = entries.get(currentRoutePosition - 1).getDept().getName();
                    fromDeptIndex = entries.get(currentRoutePosition - 2).getDept().getName();

            } else if (entries.size() > currentRoutePosition + 1) {


                if (currentRoutePosition == 0) {
                    toDeptIndex = entries.get(currentRoutePosition + 1).getDept().getName();
                    fromDeptIndex = entries.get(currentRoutePosition).getDept().getName();

                } else  {
                    toDeptIndex = entries.get(currentRoutePosition).getDept().getName();
                    fromDeptIndex = entries.get(currentRoutePosition - 1).getDept().getName();
                }

            }



            body = new BodyForCreateInvoiceWithout(flightId, tlid, true, toDeptIndex, fromDeptIndex, labelsList, packetsList);


            //for saving body in realm
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

            bodyRealm = new BodyForCreateInvoice(flightId, tlid, true, toDeptIndex, fromDeptIndex, labelLongList, packetLongList);
            //end for saving body in realm

            realm.executeTransaction(realm -> realm.copyToRealm(bodyRealm));

        } else {
            Toast.makeText(getContext(), "Ошибка. Нет flightID", Toast.LENGTH_SHORT).show();

        }
        //FOR SENDING
    }

    private void updateCurrentRoutePosition() {
        int current = pref.getInt(CURRENT_ROUTE_POSITION, 0);
        current++;
        currentRoutePosition++;
        pref.edit().putInt(CURRENT_ROUTE_POSITION, current).apply();
    }

    private void removeRealm() {
        realm.executeTransaction(realm -> {
            queryBody.findAll().deleteAllFromRealm();
            RealmResults<SendInvoice> sendInvoiceRealm = realm.where(SendInvoice.class).findAll();
            sendInvoiceRealm.deleteAllFromRealm();
        });


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInvoiceErrorEvent(Throwable message) {
        showEmptyToast(message.getMessage());
        hideProgress();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onLoadGeneralInvoice(ShowGeneralInvoiceEvent event) {

    }

    private void startCollateFragment() {
        Fragment fragment = new CollateFragment();
        ((NavigationActivity) getActivity()).startFragment(fragment);
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onAcceptGenInvoiceResponce(AcceptGenInvoiceEvent acceptGenInvoiceEvent) {

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onAcceptErrorEvent(Throwable throwable) {
        showEmptyToast(throwable.getMessage());
        hideProgress();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onAcceptEmptyEvent(AcceptEmptyEvent emptyEvent) {
        showEmptyToast(emptyEvent.getEmptyMessage());
        showRoutesEmptyData();
        hideProgress();
    }


    @Override
    public void showRoutesEmptyData() {
        tvNoDataInvoice.setVisibility(View.VISIBLE);
        tableRowInvoice.setVisibility(View.GONE);
    }

    @Override
    public void showRoutesError(Throwable throwable) {
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyToast(String message) {
        String handledStatus = presenter.handleStatus(message);

        alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(handledStatus)
                .setPositiveButton("Ok", (dialog, which) -> {
                    dismissDialog();
                }).create();


        alertDialog.show();

        hideProgress();


    }

    private void dismissDialog() {
        alertDialog.dismiss();
    }


    @Override
    public void showGeneralInvoiceId(Example example) {
//        for (int i = 0; i < example.getDestinations().size(); i++) {
//            generalInvoiceIdsList.add(example.getDestinations().get(i).getDestinationListId());
//            ids.add(example.getDestinations().get(i).getId());
//        }

        hideProgress();
        realm.executeTransaction(realm -> {
            realm.insert(example);
        });

        startCollateFragment();
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
    public void showProgress() {
        progressInvoice.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressInvoice.setVisibility(View.GONE);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
        if (!realm.isClosed()) realm.close();
    }



    /*  @Override
      public void getPostResponse(CreateResponse createResponse) {
          if (createResponse != null) {
              if (createResponse.getStatus().equals("success")) {

                  Toast.makeText(getContext(), "Общая накладная успешно создана", Toast.LENGTH_SHORT).show();


                  hideProgress();
  //                removeRealm();


              } else {
                  showEmptyToast(createResponse.getStatus());

              }

          }
      }
  */
}
