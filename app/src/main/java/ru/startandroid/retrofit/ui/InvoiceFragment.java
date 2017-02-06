package ru.startandroid.retrofit.ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.http.POST;
import ru.startandroid.retrofit.Model.BodyForCreateInvoice;
import ru.startandroid.retrofit.Model.BodyForCreateInvoiceWithout;
import ru.startandroid.retrofit.Model.CreateResponse;
import ru.startandroid.retrofit.Model.RealmLong;
import ru.startandroid.retrofit.Model.SendInvoice;
import ru.startandroid.retrofit.Model.acceptgen.Example;
import ru.startandroid.retrofit.Model.geninvoice.GeneralInvoice;
import ru.startandroid.retrofit.Model.geninvoice.InvoiceMain;
import ru.startandroid.retrofit.Model.routes.Entry;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.adapter.InvoiceRVAdapter;
import ru.startandroid.retrofit.adapter.InvoiceRVAdapterSend;
import ru.startandroid.retrofit.events.PostBodyEvent;
import ru.startandroid.retrofit.models.NetworkService;
import ru.startandroid.retrofit.presenter.InvoicePresenter;
import ru.startandroid.retrofit.presenter.InvoicePresenterImpl;
import ru.startandroid.retrofit.view.InvoiceView;

import static ru.startandroid.retrofit.Const.CURRENT_ROUTE_POSITION;
import static ru.startandroid.retrofit.Const.FLIGHT_ID;
import static ru.startandroid.retrofit.Const.FLIGHT_SHARED_PREF;
import static ru.startandroid.retrofit.Const.NUMBER_OF_CITIES;
import static ru.startandroid.retrofit.Const.TRANSPONST_LIST_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class InvoiceFragment extends Fragment implements InvoiceView {

    private TextView tvNoDataInvoice;
    private TableRow tableRowInvoice;
    private InvoicePresenter presenter;
    private ProgressBar progressInvoice;
    private RecyclerView rvInvoice;
    private RecyclerView rvSendInvoice;
    private Realm realm;
    private List<SendInvoice> sendInvoiceList;
    private List<Entry> entries = new ArrayList<>();
    private RealmQuery<Entry> queryData;
    private ArrayList<String> flightName = new ArrayList<>();

    private int currentRoutePosition;
    private int maxRouteNumber;
    SharedPreferences pref;

    //    InvoiceRVAdapter adapterGet;
    InvoiceRVAdapterSend adapterSend;


    public InvoiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View viewRoot = inflater.inflate(R.layout.fragment_invoice, container, false);

        sendInvoiceList = new ArrayList<>();

        tvNoDataInvoice = (TextView) viewRoot.findViewById(R.id.tv_no_data_invoice);
        tableRowInvoice = (TableRow) viewRoot.findViewById(R.id.tablerow_invoice);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Накладные");

        rvInvoice = (RecyclerView) viewRoot.findViewById(R.id.rv_invoice_fragment);
        rvSendInvoice = (RecyclerView) viewRoot.findViewById(R.id.rv_send_invoice);
        progressInvoice = (ProgressBar) viewRoot.findViewById(R.id.progress_invoice);

        realm = Realm.getDefaultInstance();

        RealmResults<SendInvoice> realmResults = realm.where(SendInvoice.class).findAll();
        for (int i = 0; i < realmResults.size(); i++) {
            sendInvoiceList.add(realmResults.get(i));
        }


        pref = getActivity().getApplicationContext().getSharedPreferences(FLIGHT_SHARED_PREF, 0); // 0 - for private mode
        maxRouteNumber = pref.getInt(NUMBER_OF_CITIES, 0);
        currentRoutePosition = pref.getInt(CURRENT_ROUTE_POSITION, 0);

        //FOR SENDING
        queryData = realm.where(Entry.class);
        if (!queryData.findAll().isEmpty()) {
            for (int i = 0; i < queryData.findAll().distinct("index").size(); i++) {
                entries.add(queryData.findAll().get(i));
            }
        }
        for (int i = 1; i < entries.size(); i++) {
            flightName.add(entries.get(i).getDept().getNameRu());
        }


//        prepareBodyForPost();


        if (!sendInvoiceList.isEmpty()) {


            adapterSend = new InvoiceRVAdapterSend(getActivity(), sendInvoiceList, ((childView, childAdapterPosition) -> {


                //если дальше чем первый пункт, но не последний
                if (currentRoutePosition > 0 && currentRoutePosition < maxRouteNumber) {
//                    createEmptyInvoice();
                }

                presenter.postCreateInvoice(body);

//                Toast.makeText(getContext(), "Это для отправки накладной, функционал дорабатывается", Toast.LENGTH_SHORT).show();

            }));

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            rvSendInvoice.setLayoutManager(mLayoutManager);
            rvSendInvoice.setAdapter(adapterSend);
        }

        presenter = new InvoicePresenterImpl(this, new NetworkService());
        presenter.loadGeneralInvoice();

        return viewRoot;
    }

    public void prepareBodyForPost() {

        SharedPreferences pref1 = getActivity().getSharedPreferences(FLIGHT_SHARED_PREF, 0); // 0 - for private mode
        if (pref1.contains(FLIGHT_ID) && pref1.contains(TRANSPONST_LIST_ID)) {

            Long flightId = pref1.getLong(FLIGHT_ID, 0);
            Long tlid = pref1.getLong(TRANSPONST_LIST_ID, 0);

            ArrayList<Long> labelsList = new ArrayList<>();
            ArrayList<Long> packetsList = new ArrayList<>();
            String toDeptIndex = entries.get(currentRoutePosition + 1).getDept().getName();
            String fromDeptIndex = entries.get(currentRoutePosition).getDept().getName();

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


        } else {
            Toast.makeText(getContext(), "Ошибка. Нет flightID", Toast.LENGTH_SHORT).show();

        }
        //FOR SENDING
    }


    public static void setBody(BodyForCreateInvoiceWithout body) {
        InvoiceFragment.body = body;
    }

    public static BodyForCreateInvoiceWithout body;
    public static BodyForCreateInvoice bodyRealm;


    private void createEmptyInvoice() {

        if (currentRoutePosition > 0 && currentRoutePosition < maxRouteNumber) {
            currentRoutePosition++;
            pref.edit().putInt(NUMBER_OF_CITIES, currentRoutePosition).apply();
        }

        String toName = flightName.get(currentRoutePosition);
        SendInvoice sendInvoice = new SendInvoice();
        sendInvoice.setWhere(toName);
        sendInvoice.setBodyForCreateInvoice(bodyRealm);
        realm.executeTransaction(realm -> realm.insert(sendInvoice));
    }

    @Override
    public void getPostResponse(CreateResponse createResponse) {
        if (createResponse != null) {

            if (createResponse.getStatus().equals("success")) {

//                //update items in rv
//                for (int i = 0; i < chosen.size(); i++) {
//                    objects.remove(chosen.get(i));
//                }
//
//                collateRVAdapter.notifyDataSetChanged();


                Toast.makeText(getContext(), "Общая накладная успешно создана", Toast.LENGTH_SHORT).show();

            } else {
                showEmptyToast(createResponse.getStatus());

            }

        }
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
    public void showGeneralInvoice(InvoiceMain invoiceMain) {

        final List<GeneralInvoice> generalInvoiceList = new ArrayList<>();

        generalInvoiceList.addAll(invoiceMain.getGeneralInvoices());


        InvoiceRVAdapter invoiceRVAdapter = new InvoiceRVAdapter(getActivity(), generalInvoiceList, (childView, childAdapterPosition) -> {

//            Bundle bundle = new Bundle();
//            bundle.putLong("generalInvoiceId", generalInvoiceList.get(childAdapterPosition).getId());
//            Fragment fragment = new CollateFragment();
//            fragment.setArguments(bundle);

            Long generalInvoiceId = generalInvoiceList.get(childAdapterPosition).getId();
            presenter.retrofitAcceptGeneralInvoice(generalInvoiceId);

            if (currentRoutePosition == 0) {

//                createEmptyInvoice();
            } else if (currentRoutePosition == maxRouteNumber) {

            }

//            ((NavigationActivity) getActivity()).startFragment(fragment);

        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvInvoice.setLayoutManager(mLayoutManager);
        rvInvoice.setAdapter(invoiceRVAdapter);

    }

    private List<String> generalInvoiceIdsList = new ArrayList<>();
    private List<Long> ids = new ArrayList<>();

    @Override
    public void showGeneralInvoiceId(Example destinations) {

        for (int i = 0; i < destinations.getDestinations().size(); i++) {
            generalInvoiceIdsList.add(destinations.getDestinations().get(i).getDestinationListId());
            ids.add(destinations.getDestinations().get(i).getId());
        }

        realm.executeTransaction(realm -> {
            realm.insert(destinations);
        });

//        listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, generalInvoiceIdsList);
//        listViewAcceptGen.setAdapter(listAdapter);
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


    private AlertDialog alertDialog;

    @Override
    public void showEmptyToast(String message) {
        String handledStatus = presenter.handleStatus(message);

        alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(handledStatus)
                .setPositiveButton("Ok", (dialog, which) -> {
                    dismissDialog();
                }).create();

        alertDialog.show();

    }

    private void dismissDialog() {
        alertDialog.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
        if (!realm.isClosed()) realm.close();
    }
}
