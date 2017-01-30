package ru.startandroid.retrofit.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.startandroid.retrofit.Model.SendInvoice;
import ru.startandroid.retrofit.Model.geninvoice.GeneralInvoice;
import ru.startandroid.retrofit.Model.geninvoice.InvoiceMain;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.adapter.InvoiceRVAdapter;
import ru.startandroid.retrofit.adapter.InvoiceRVAdapterSend;
import ru.startandroid.retrofit.models.NetworkService;
import ru.startandroid.retrofit.presenter.InvoicePresenter;
import ru.startandroid.retrofit.presenter.InvoicePresenterImpl;
import ru.startandroid.retrofit.view.InvoiceView;

/**
 * A simple {@link Fragment} subclass.
 */
public class InvoiceFragment extends Fragment implements InvoiceView {

    private TextView tvNoDataInvoice;
    private TableRow tableRowInvoice;
    private InvoicePresenter presenter;
    private ProgressBar progressInvoice;
    private RecyclerView rvInvoice;
    private Realm realm;
    private List<SendInvoice> sendInvoiceList;

    private List<Object> objectList;

    public InvoiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View viewRoot = inflater.inflate(R.layout.fragment_invoice, container, false);

        sendInvoiceList = new ArrayList<>();

        objectList = new ArrayList<>();

        tvNoDataInvoice = (TextView) viewRoot.findViewById(R.id.tv_no_data_invoice);
        tableRowInvoice = (TableRow) viewRoot.findViewById(R.id.tablerow_invoice);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Накладные");

        rvInvoice = (RecyclerView) viewRoot.findViewById(R.id.rv_invoice_fragment);
        progressInvoice = (ProgressBar) viewRoot.findViewById(R.id.progress_invoice);

        presenter = new InvoicePresenterImpl(this, new NetworkService());
        presenter.loadGeneralInvoice();


        realm = Realm.getDefaultInstance();

        RealmResults<SendInvoice> realmResults = realm.where(SendInvoice.class).findAll();
        for (int i = 0; i < realmResults.size(); i++) {
            sendInvoiceList.add(realmResults.get(i));
        }

        if (!sendInvoiceList.isEmpty()) {

            objectList.add(sendInvoiceList);
            /*InvoiceRVAdapter adapterSend = new InvoiceRVAdapter(getActivity(), objectList, ((childView, childAdapterPosition) -> {

                Toast.makeText(getContext(), "Send action", Toast.LENGTH_SHORT).show();

            }));

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            rvInvoice.setLayoutManager(mLayoutManager);
            rvInvoice.setAdapter(adapterSend);*/
        }

        return viewRoot;
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

        final List<GeneralInvoice> generalInvoiceList = new ArrayList<GeneralInvoice>();

        generalInvoiceList.addAll(invoiceMain.getGeneralInvoices());

        objectList.add(generalInvoiceList);

        InvoiceRVAdapter invoiceRVAdapter = new InvoiceRVAdapter(getActivity(), objectList, (childView, childAdapterPosition) -> {

            Bundle bundle = new Bundle();
            bundle.putLong("generalInvoiceId", generalInvoiceList.get(childAdapterPosition).getId());
            Fragment fragment = new AcceptGenInvoiceFragment();
            fragment.setArguments(bundle);

            ((NavigationActivity) getActivity()).startFragment(fragment);

        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvInvoice.setLayoutManager(mLayoutManager);
        rvInvoice.setAdapter(invoiceRVAdapter);

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
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
        if (!realm.isClosed()) realm.close();
    }
}
