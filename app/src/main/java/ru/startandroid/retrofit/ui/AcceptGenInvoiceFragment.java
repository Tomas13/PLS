package ru.startandroid.retrofit.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.startandroid.retrofit.Const;
import ru.startandroid.retrofit.Interface.GitHubService;
import ru.startandroid.retrofit.Model.IdsCollate;
import ru.startandroid.retrofit.Model.acceptgen.Oinvoice;
import ru.startandroid.retrofit.Model.collatedestination.CollateResponse;
import ru.startandroid.retrofit.Model.collatedestination.Dto;
import ru.startandroid.retrofit.Model.collatedestination.Label;
import ru.startandroid.retrofit.Model.collatedestination.Packet;
import ru.startandroid.retrofit.Model.destinationlist.ResponseDestinationList;

import static ru.startandroid.retrofit.utils.Singleton.getUserClient;

import ru.startandroid.retrofit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AcceptGenInvoiceFragment extends Fragment {


    private ListView listViewAcceptGen;

    private TextView tvAcceptGen;

    private Button btnCollate;

    List<Long> ids;
    private Realm realm;


    private ArrayAdapter<String> listAdapter;
    private List<String> generalInvoiceIdsList = new ArrayList<>();

    public AcceptGenInvoiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accept_gen_invoice, container, false);

        listViewAcceptGen = (ListView) view.findViewById(R.id.list_view_accept_gen);
        tvAcceptGen = (TextView) view.findViewById(R.id.tv_no_data_accept_gen);
        btnCollate = (Button) view.findViewById(R.id.btn_collate);

        Long id;


        ids = new ArrayList<Long>();

        if (getArguments() != null) {

            id = getArguments().getLong("generalInvoiceId");

            Toast.makeText(getContext(), "id " + id, Toast.LENGTH_SHORT).show();
            retrofitAcceptGeneralInvoice(id);
        } else {

            Toast.makeText(getContext(), "came from menu", Toast.LENGTH_SHORT).show();
            retrofitDestinationList();

        }


        btnCollate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrofitPostCollate();
            }
        });

        return view;
    }


    private void retrofitDestinationList() {
        Retrofit retrofitDestList = new Retrofit.Builder()
                .baseUrl("http://pls-test.kazpost.kz/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUserClient(Const.Token))
                .build();

        GitHubService gitHubServ = retrofitDestList.create(GitHubService.class);
        gitHubServ.getDestionationLists().enqueue(new Callback<ResponseDestinationList>() {
            @Override
            public void onResponse(Call<ResponseDestinationList> call, Response<ResponseDestinationList> response) {
                if (response.isSuccessful() && response.body().getStatus().equals("success")) {

                    for (int i = 0; i < response.body().getDestinationLists().size(); i++) {

                        generalInvoiceIdsList.add(response.body().getDestinationLists().get(i).getDestinationListId());

                        ids.add(response.body().getDestinationLists().get(i).getId());

                    }

                    listAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, generalInvoiceIdsList);

                    listViewAcceptGen.setAdapter(listAdapter);


                    Log.d("acceptGen", generalInvoiceIdsList.get(0).toString());
                    listViewAcceptGen.notify();

                } else {

                    tvAcceptGen.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseDestinationList> call, Throwable t) {
                Log.d("MainAcceptGen", t.getMessage());

            }
        });
    }


    private void retrofitAcceptGeneralInvoice(final Long generalInvoiceId) {

        Retrofit retrofitAcceptGenInvoice = new Retrofit.Builder()
                .baseUrl("http://pls-test.kazpost.kz/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUserClient(Const.Token))
                .build();

        GitHubService gitHubServ = retrofitAcceptGenInvoice.create(GitHubService.class);

        gitHubServ.acceptGeneralInvoice(generalInvoiceId).enqueue(new Callback<Oinvoice>() {
            @Override
            public void onResponse(Call<Oinvoice> call, Response<Oinvoice> response) {

//                Log.d("InvoiceORespo", response.body().getDestinationLists().get(0).getDestinationListId());
                Log.d("MainAcceptGen", response.message());

                if (response.isSuccessful() && response.body().getStatus().equals("success")) {

                    for (int i = 0; i < response.body().getDestinationLists().size(); i++) {

                        generalInvoiceIdsList.add(response.body().getDestinationLists().get(i).getDestinationListId());

                        ids.add(response.body().getDestinationLists().get(i).getId());
                    }

                    listAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, generalInvoiceIdsList);

                    listViewAcceptGen.setAdapter(listAdapter);

                } else {

                    tvAcceptGen.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Oinvoice> call, Throwable t) {
                Log.d("MainAcceptGen", t.getMessage());
            }
        });
    }


    Dto collateDtoObject;

    private void retrofitPostCollate() {
        Retrofit retrofitDestList = new Retrofit.Builder()
                .baseUrl("http://pls-test.kazpost.kz/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUserClient(Const.Token))
                .build();

        IdsCollate idsCol = new IdsCollate(ids);

        GitHubService gitHubServ = retrofitDestList.create(GitHubService.class);
        gitHubServ.postCollateDestinationLists(idsCol).enqueue(new Callback<CollateResponse>() {
            @Override
            public void onResponse(Call<CollateResponse> call, Response<CollateResponse> response) {


                if (response.isSuccessful() && response.body().getStatus().equals("success")) {
                    Log.d("MainAccept", "got response");

                    Log.d("MainAccept", response.body().getStatus());
                    Log.d("MainAccept labels", response.body().getDto().getLabels().size() + " ");
                    Log.d("MainAccept packets", response.body().getDto().getPackets().size() + " ");


                    collateDtoObject = new Dto();

                    collateDtoObject = response.body().getDto();


                    ArrayList<CollateResponse> collateResponsesArrayList = new ArrayList<CollateResponse>();
//                collateResponsesArrayList.addAll(response)

                    // Create the Realm instance
                    realm = Realm.getDefaultInstance();

                    realm.beginTransaction();
                    realm.insert(collateDtoObject);
                    realm.commitTransaction();
                    Log.d("MainAccept", "got response");


                } else {


                    retrogitGetListForVpn();
                    tvAcceptGen.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onFailure(Call<CollateResponse> call, Throwable t) {
                Log.d("MainAccept", t.getMessage());

            }
        });
    }


    private void retrogitGetListForVpn() {
        Retrofit retrofitDestList = new Retrofit.Builder()
                .baseUrl("http://pls-test.kazpost.kz/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUserClient(Const.Token))
                .build();


        GitHubService gitHubServ = retrofitDestList.create(GitHubService.class);
        gitHubServ.getListForVpn().enqueue(new Callback<CollateResponse>() {
            @Override
            public void onResponse(Call<CollateResponse> call, Response<CollateResponse> response) {


                if (response.isSuccessful() && response.body().getStatus().equals("success")) {
                    Log.d("MainAccept", "got response");

                    Log.d("MainAccept", response.body().getStatus());
                    Log.d("MainAccept labels", response.body().getDto().getLabels().size() + " ");
                    Log.d("MainAccept packets", response.body().getDto().getPackets().size() + " ");



                    ArrayList<Packet> packetsArrayList = new ArrayList<>();
                    packetsArrayList.addAll(response.body().getDto().getPackets());

                    ArrayList<Label> labelsArrayList = new ArrayList<>();
                    labelsArrayList.addAll(response.body().getDto().getLabels());




                    // Create the Realm instance
                    realm = Realm.getDefaultInstance();

                    realm.beginTransaction();
                    realm.insert(labelsArrayList);
                    realm.insert(packetsArrayList);
                    realm.commitTransaction();
                    Log.d("MainAccept", "got response");

                    ((NavigationActivity) getActivity()).startFragment(new CollateFragment());


                } else {

                    tvAcceptGen.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onFailure(Call<CollateResponse> call, Throwable t) {
                Log.d("MainAccept", t.getMessage());

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
