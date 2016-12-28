package ru.startandroid.retrofit.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.startandroid.retrofit.Const;
import ru.startandroid.retrofit.Interface.GitHubService;
import ru.startandroid.retrofit.Model.acceptgen.Oinvoice;
import ru.startandroid.retrofit.Model.geninvoice.GeneralInvoice;
import ru.startandroid.retrofit.Model.geninvoice.InvoiceMain;
import ru.startandroid.retrofit.Model.routes.Routes;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.adapter.InvoiceRVAdapter;

import static ru.startandroid.retrofit.utils.Singleton.getUserClient;


/**
 * A simple {@link Fragment} subclass.
 */
public class InvoiceFragment extends Fragment {


    public InvoiceFragment() {
        // Required empty public constructor
    }


    RecyclerView rvInvoice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View viewRoot = inflater.inflate(R.layout.fragment_invoice, container, false);

        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle("Накладные");

        rvInvoice = (RecyclerView) viewRoot.findViewById(R.id.rv_invoice_fragment);

        getGeneralInvoice();

        return viewRoot;
    }

    private void getGeneralInvoice() {
        Retrofit retrofitInvoice = new Retrofit.Builder()
                .baseUrl("http://pls-test.kazpost.kz/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUserClient(Const.Token))
                .build();

        GitHubService gitHubServ = retrofitInvoice.create(GitHubService.class);

        final Call<InvoiceMain> callEdges =
                gitHubServ.getGeneralInvoice();

        callEdges.enqueue(new Callback<InvoiceMain>() {
            @Override
            public void onResponse(Call<InvoiceMain> call, Response<InvoiceMain> response) {

                Log.d("InvoiceFrag", "got in onresponse");

                if (response.isSuccessful() && response.body().getStatus().equals("success")) {

                    final List<GeneralInvoice> generalInvoiceList = new ArrayList<GeneralInvoice>();

                    generalInvoiceList.addAll(response.body().getGeneralInvoices());

                    InvoiceRVAdapter invoiceRVAdapter = new InvoiceRVAdapter(getActivity(), generalInvoiceList, new InvoiceRVAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View childView, int childAdapterPosition) {

                            Toast.makeText(getContext(), "SHIT OH " + generalInvoiceList.get(childAdapterPosition).getGeneralInvoiceId(), Toast.LENGTH_SHORT).show();


                            Bundle bundle = new Bundle();
                            bundle.putLong("generalInvoiceId", generalInvoiceList.get(childAdapterPosition).getId());
                            Fragment fragment = new AcceptGenInvoiceFragment();
                            fragment.setArguments(bundle);

                            ((NavigationActivity) getActivity()).startFragment(fragment);


                        }
                    });

                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                    rvInvoice.setLayoutManager(mLayoutManager);
                    rvInvoice.setAdapter(invoiceRVAdapter);

                  /*  rvInvoice.addOnItemTouchListener(new InvoiceRVAdapter(getActivity(), generalInvoiceList,
                            new InvoiceRVAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View childView, int childAdapterPosition) {

                                    Log.d("MainRoute", " pos is " + childAdapterPosition + " id " + childView.getId() + " btn " + R.id.btn_retrieve);
//                                    Toast.makeText(getActivity(), " pos is " + childView.getId() + " id " + R.id.btn_retrieve, Toast.LENGTH_SHORT).show();

                                    if (childView.getId() == R.id.btn_retrieve){
                                        Toast.makeText(getActivity(), "btn number " + childAdapterPosition, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }));*/

                }
            }

            @Override
            public void onFailure(Call<InvoiceMain> call, Throwable t) {
                Log.d("Main", t.getMessage());
            }
        });
    }


}
