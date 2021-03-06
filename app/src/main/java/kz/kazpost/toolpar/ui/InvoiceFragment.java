package kz.kazpost.toolpar.ui;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import kz.kazpost.toolpar.AppJobManager;
import kz.kazpost.toolpar.Model.BodyForCreateInvoice;
import kz.kazpost.toolpar.Model.BodyForCreateInvoiceWithout;
import kz.kazpost.toolpar.Model.RealmLong;
import kz.kazpost.toolpar.Model.SendInvoice;
import kz.kazpost.toolpar.Model.acceptgen.Example;
import kz.kazpost.toolpar.Model.acceptgen.LabelList;
import kz.kazpost.toolpar.Model.acceptgen.PacketList;
import kz.kazpost.toolpar.Model.geninvoice.GeneralInvoice;
import kz.kazpost.toolpar.Model.geninvoice.InvoiceMain;
import kz.kazpost.toolpar.Model.routes.Entry;
import kz.kazpost.toolpar.R;
import kz.kazpost.toolpar.adapter.InvoiceRVAdapter;
import kz.kazpost.toolpar.adapter.InvoiceRVAdapterSend;
import kz.kazpost.toolpar.base.BaseFragment;
import kz.kazpost.toolpar.events.AcceptEmptyEvent;
import kz.kazpost.toolpar.events.AcceptGenInvoiceEvent;
import kz.kazpost.toolpar.events.AccessTokenEvent;
import kz.kazpost.toolpar.events.InvoiceEvent;
import kz.kazpost.toolpar.events.ShowGeneralInvoiceEvent;
import kz.kazpost.toolpar.jobs.GetAccessTokenJob;
import kz.kazpost.toolpar.jobs.PostCreateInvoiceJob;
import kz.kazpost.toolpar.presenter.InvoicePresenter;
import kz.kazpost.toolpar.presenter.InvoicePresenterImpl;
import kz.kazpost.toolpar.view.InvoiceView;

import static kz.kazpost.toolpar.Const.AccessTokenConst;
import static kz.kazpost.toolpar.Const.CURRENT_ROUTE_POSITION;
import static kz.kazpost.toolpar.Const.FAKE;
import static kz.kazpost.toolpar.Const.FLIGHT_ID;
import static kz.kazpost.toolpar.Const.FLIGHT_SHARED_PREF;
import static kz.kazpost.toolpar.Const.INVOICE_NAME;
import static kz.kazpost.toolpar.Const.INVOICE_PREF;
import static kz.kazpost.toolpar.Const.NUMBER_OF_CITIES;
import static kz.kazpost.toolpar.Const.TRANSPONST_LIST_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class InvoiceFragment extends BaseFragment implements InvoiceView {

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
//    private InvoicePresenter presenter;
    private Realm realm;
    private List<SendInvoice> sendInvoiceList;
    private List<Entry> entries = new ArrayList<>();
    private ArrayList<String> flightName = new ArrayList<>();
    private AlertDialog alertDialog;
    private RealmQuery<BodyForCreateInvoice> queryBody;
    private int currentRoutePosition;
    private int fake;

    private int maxRouteNumber;
    private SharedPreferences pref;
    private BodyForCreateInvoiceWithout body;
    private BodyForCreateInvoice bodyRealm;
    private JobManager jobManager;
    private InvoiceRVAdapterSend adapterSend;

    @Inject
    InvoicePresenter<InvoiceView> presenter;

    public InvoiceFragment() {
        // Required empty public constructor
    }

    private void init() {
        getBaseActivity().setTitle("Общие накладные");
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Общие накладные");

        sendInvoiceList = new ArrayList<>();
        realm = Realm.getDefaultInstance();
        realm.setAutoRefresh(true);

        RealmResults<SendInvoice> realmResults = realm.where(SendInvoice.class).findAll();
        if (realmResults.size() > 0) sendInvoiceList.add(realmResults.last());
//        for (int i = 0; i < realmResults.size(); i++) {
//            sendInvoiceList.add(realmResults.get(i));
//        }

//        presenter = new InvoicePresenterImpl(this);

        pref = getActivity().getApplicationContext().getSharedPreferences(FLIGHT_SHARED_PREF, 0); // 0 - for private mode
        maxRouteNumber = pref.getInt(NUMBER_OF_CITIES, 0);
        currentRoutePosition = pref.getInt(CURRENT_ROUTE_POSITION, 1);

        fake = pref.getInt(FAKE, 0);

        jobManager = AppJobManager.getJobManager();

        //FOR SENDING
        RealmQuery<Entry> queryData = realm.where(Entry.class);
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
        // Inflate the login_new for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_invoice, container, false);
        ButterKnife.bind(this, viewRoot);
        init();
        setHasOptionsMenu(true);

        createConfigAdapterSend();

//        presenter.loadGeneralInvoice();

        jobManager.addJobInBackground(new GetAccessTokenJob());

        return viewRoot;
    }

    private void createConfigAdapterSend() {

        if (queryBody.findAll().size() == 0) prepareBodyForPost();

        if (!sendInvoiceList.isEmpty()) {

            adapterSend = new InvoiceRVAdapterSend(getActivity(), sendInvoiceList, ((childView, childAdapterPosition) -> {

                if (queryBody.findAll().size() > 0) body = realmToBody(bodyRealm);

                //TODO
                sendInvoiceList.remove(childAdapterPosition);
                adapterSend.notifyDataSetChanged();
                //TODO

                showProgress();


                jobManager.addJobInBackground(new GetAccessTokenJob());

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


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccessTokenEvent(AccessTokenEvent accessTokenEvent) {
        AccessTokenConst = accessTokenEvent.getLoginResponse().getAccessToken();
        Log.d("Access2", AccessTokenConst);
        presenter.loadGeneralInvoice(AccessTokenConst);

//        jobManager.addJobInBackground(new LoadGeneralInvoiceJob());
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

        if (labelIds.size() >= 1) {
            for (int w = labelIds.size() - 1; w >= 0; w--) {
                int j = labelIds.get(w);
                realm.executeTransaction(realm -> realm.where(LabelList.class).findAll().get(j).deleteFromRealm());

            }
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

        if (packetIds.size() >= 1) {
            for (int w = packetIds.size() - 1; w >= 0; w--) {
                int j = packetIds.get(w);
                realm.executeTransaction(realm -> realm.where(PacketList.class).findAll().get(j).deleteFromRealm());
            }
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

        if (flightName.size() >= fake + 1) {

            SendInvoice sendInvoice;

            if (flightName.size() == fake + 1) {
                String toName = flightName.get(fake);
                String fromName = flightName.get(fake - 1);
                sendInvoice = new SendInvoice();
                sendInvoice.setTo(toName);
                sendInvoice.setFrom(fromName);
//            sendInvoice.setBodyForCreateInvoice(bodyRealm);

            } else {

                String toName = flightName.get(fake + 1);
                String fromName = flightName.get(fake);
                sendInvoice = new SendInvoice();
                sendInvoice.setTo(toName);
                sendInvoice.setFrom(fromName);
//            sendInvoice.setBodyForCreateInvoice(bodyRealm);
            }

            realm.executeTransaction(realm -> realm.insert(sendInvoice));

            if (currentRoutePosition > 0 && currentRoutePosition < maxRouteNumber) {
                currentRoutePosition++;
                Log.d("InvoiceFragment", " currentRoutePos  createEmpty " + currentRoutePosition);

                pref.edit().putInt(CURRENT_ROUTE_POSITION, currentRoutePosition).apply();
            }

        } else {
            Toast.makeText(getContext(), "END OF ROUTE", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void showGeneralInvoice(InvoiceMain invoiceMain) {

        Log.d("Access2Inoice", "got to showGen");
        List<GeneralInvoice> generalInvoiceList = new ArrayList<>();
        generalInvoiceList.addAll(invoiceMain.getGeneralInvoices());

        invoiceRVAdapter = new InvoiceRVAdapter(getActivity(), generalInvoiceList, (childView, childAdapterPosition) -> {

            if (currentRoutePosition + 1 < flightName.size()) {    //if all current route points are passed, we can't accept new O-invoice

                Long generalInvoiceId = generalInvoiceList.get(childAdapterPosition).getId();
                presenter.retrofitAcceptGeneralInvoice(generalInvoiceId, AccessTokenConst);

                showProgress();
//            jobManager.addJobInBackground(new AcceptGeneralInvoiceJob(generalInvoiceId));

                createEmptyInvoice();

                generalInvoiceList.remove(childAdapterPosition);
                invoiceRVAdapter.notifyDataSetChanged();


                getAndSaveCurrentTimeForInvoiceName();


            } else {

                showLastPointSnackBar();

            }
            //startCollateFragment();
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvInvoice.setLayoutManager(mLayoutManager);
        rvInvoice.setAdapter(invoiceRVAdapter);


    }


    private void showLastPointSnackBar() {
        Snackbar snackbar = Snackbar.make(rvInvoice, lastPoint, Snackbar.LENGTH_LONG);

        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(2);  // show multiple line
        snackbar.show();
    }

    private void getAndSaveCurrentTimeForInvoiceName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.FRANCE);
        Date now = new Date();
        String timeOfEvent = simpleDateFormat.format(now);

        SharedPreferences ref = getActivity().getSharedPreferences(INVOICE_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();
        editor.putString(INVOICE_NAME, timeOfEvent);
        editor.apply();
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

            Log.d("InvoiceFragment", " prepareBody pos " + currentRoutePosition);
            Log.d("InvoiceFragment", " prepareBody fake " + fake);


            if (entries.size() > 0){

                if (entries.size() == currentRoutePosition) {
                    toDeptIndex = entries.get(currentRoutePosition - 1).getDept().getName();
                    fromDeptIndex = entries.get(currentRoutePosition - 2).getDept().getName();

                } else if (entries.size() > currentRoutePosition) {

                    if (currentRoutePosition == 0) {
                        toDeptIndex = entries.get(currentRoutePosition + 1).getDept().getName();
                        fromDeptIndex = entries.get(currentRoutePosition).getDept().getName();

                    } else {
                        toDeptIndex = entries.get(currentRoutePosition).getDept().getName();
                        fromDeptIndex = entries.get(currentRoutePosition - 1).getDept().getName();
                    }
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
//        int current = pref.getInt(CURRENT_ROUTE_POSITION, 0);

        if (fake < maxRouteNumber) {
            fake++;
            pref.edit().putInt(FAKE, fake).apply();
        }

//        current++;

        if (currentRoutePosition == 0) {

            currentRoutePosition++;
            pref.edit().putInt(CURRENT_ROUTE_POSITION, currentRoutePosition).apply();
        }

//        copyRoutePosition++;


//        pref.edit().putInt(CURRENT_ROUTE_POSITION, current).apply();

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
        Fragment fragment = new CollateNewFragment();
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
//        showEmptyToast(emptyEvent.getEmptyMessage());
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.invoice, menu);
//        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_refresh) {
            refreshDataOnFragment();
        }
        return super.onOptionsItemSelected(item);

    }

    private void refreshDataOnFragment() {
        jobManager.addJobInBackground(new GetAccessTokenJob());
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
        setRVclickable(false);
    }

    @Override
    public void hideProgress() {
        progressInvoice.setVisibility(View.GONE);
        setRVclickable(true);
    }


    private void setRVclickable(boolean clickBool) {
        rvInvoice.setClickable(clickBool);
        rvSendInvoice.setClickable(clickBool);
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
