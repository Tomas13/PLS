package ru.startandroid.retrofit.ui;


import android.app.Dialog;
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

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.startandroid.retrofit.Const;
import ru.startandroid.retrofit.Interface.GitHubService;
import ru.startandroid.retrofit.Model.Datum;
import ru.startandroid.retrofit.Model.collatedestination.CollateResponse;
import ru.startandroid.retrofit.Model.collatedestination.Label;
import ru.startandroid.retrofit.Model.collatedestination.Packet;
import ru.startandroid.retrofit.Model.routes.Entry;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.adapter.CollateRVAdapter;

import static ru.startandroid.retrofit.utils.Singleton.getUserClient;


/**
 * A simple {@link Fragment} subclass.
 */
public class VolumesFragment extends Fragment {


    List<Long> packetsList = new ArrayList<>();
    List<Long> labelsList = new ArrayList<>();

    List<Object> finalObjects;

    private Realm realm;


    Button btnSendInvoice;
    TextView tvHeaderHint, tvNoDataVolumes, ftv_ll_first, stv_ll_first, ttv_ll_first,
            ftv_ll_second, stv_ll_second, ttv_ll_second,
            ftv_ll_third, stv_ll_third, ttv_ll_third;

    LinearLayout firstLL, secondLL, thirdLL;
    RealmQuery<Entry> queryData;

    RecyclerView recyclerViewVolumes;

    public VolumesFragment() {
        // Required empty public constructor
    }


    ArrayAdapter<String> adapter;

    List<Entry> entries;

    ArrayList<String> flightName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_volumes, container, false);

        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle("Ёмкости");


        flightName = new ArrayList<>();
        entries = new ArrayList<>();

        // Create the Realm instance
        realm = Realm.getDefaultInstance();
        // Build the query looking at all users:
        queryData = realm.where(Entry.class);


        if (!queryData.findAll().isEmpty()){
            for (int i = 0; i < queryData.findAll().size(); i++) {
                entries.add(queryData.findAll().get(i));
            }
        }


        for (int i = 0; i < entries.size(); i++) {
            flightName.add(entries.get(i).getDept().getNameRu());
        }




        tvNoDataVolumes = (TextView) rootView.findViewById(R.id.tv_no_data_volumes);
        recyclerViewVolumes = (RecyclerView) rootView.findViewById(R.id.rv_fragment_volumes);
        btnSendInvoice = (Button) rootView.findViewById(R.id.btn_send_invoice);
        tvHeaderHint = (TextView) rootView.findViewById(R.id.tv_header_hint);

        btnSendInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        retrofitGetListForVpn();

        return rootView;
    }



    private void showDialog() {
        final Dialog pointDialog = new Dialog(getContext());
        pointDialog.setContentView(R.layout.fragment_flight);





        ListView listView = (ListView) pointDialog.findViewById(R.id.list_view_flight);
        adapter = new ArrayAdapter<String>(getContext(), R.layout.list_view_item, flightName);
        listView.setAdapter(adapter);

        pointDialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Toast.makeText(getContext(), "Готово, можете нажать кнопку ОК для закрытия диалога", Toast.LENGTH_SHORT).show();

            }
        });

        Button btnOk = (Button) pointDialog.findViewById(R.id.btn_ok_flight);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pointDialog.dismiss();

            }
        });

    }


    private void retrofitGetListForVpn() {
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
                    Log.d("MainVolumes", "got response");

                    Log.d("MainVolumes", response.body().getStatus());
                    Log.d("MainVolumes labels", response.body().getDto().getLabels().size() + " ");
                    Log.d("MainVolumes packets", response.body().getDto().getPackets().size() + " ");

                    Log.d("MainVolumes la", response.body().getDto().getLabels().get(0).getLabelListid());

                    ArrayList<Packet> packetsArrayList = new ArrayList<>();
                    packetsArrayList.addAll(response.body().getDto().getPackets());

                    ArrayList<Label> labelsArrayList = new ArrayList<>();
                    labelsArrayList.addAll(response.body().getDto().getLabels());


                    final ArrayList<Object> objects = new ArrayList<Object>();
                    objects.addAll(packetsArrayList);
                    objects.addAll(labelsArrayList);

                    final CollateRVAdapter collateRVAdapter = new CollateRVAdapter(getActivity(), objects, new CollateRVAdapter.OnItemCheckedListener() {
                        @Override
                        public void onCheckedChanged(View childView, boolean isChecked, int childPosition) {
                            Toast.makeText(getContext(), " pos " + childPosition, Toast.LENGTH_SHORT).show();

                            if (isChecked) {
                                if (objects.get(childPosition) instanceof Packet) {
                                    packetsList.add(((Packet) objects.get(childPosition)).getId());

                                } else if (objects.get(childPosition) instanceof Label) {
                                    labelsList.add(((Label) objects.get(childPosition)).getId());
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


                    // Create the Realm instance
//                    realm = Realm.getDefaultInstance();
//                    realm.beginTransaction();
//                    realm.insert(labelsArrayList);
//                    realm.insert(packetsArrayList);
//                    realm.commitTransaction();
                    Log.d("MainAccept", "got response");

//                    ((NavigationActivity) getActivity()).startFragment(new CollateFragment());


                } else {

                    tvNoDataVolumes.setVisibility(View.VISIBLE);
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
        if (!packetsList.isEmpty()) packetsList.clear();
        if (!labelsList.isEmpty()) labelsList.clear();

        if (realm != null && !realm.isClosed()) realm.close();
    }
}
