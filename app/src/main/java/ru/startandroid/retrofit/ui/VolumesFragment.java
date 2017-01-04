package ru.startandroid.retrofit.ui;


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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.startandroid.retrofit.Const;
import ru.startandroid.retrofit.Interface.GitHubService;
import ru.startandroid.retrofit.Model.collatedestination.CollateResponse;
import ru.startandroid.retrofit.Model.collatedestination.Label;
import ru.startandroid.retrofit.Model.collatedestination.Packet;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.adapter.CollateRVAdapter;

import static ru.startandroid.retrofit.utils.Singleton.getUserClient;


/**
 * A simple {@link Fragment} subclass.
 */
public class VolumesFragment extends Fragment {


    Button btnSendInvoice;
    TextView tvHeaderHint, tvNoDataVolumes, ftv_ll_first, stv_ll_first, ttv_ll_first,
            ftv_ll_second, stv_ll_second, ttv_ll_second,
            ftv_ll_third, stv_ll_third, ttv_ll_third;

    LinearLayout firstLL, secondLL, thirdLL;

    RecyclerView recyclerViewVolumes;

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


        tvNoDataVolumes = (TextView) rootView.findViewById(R.id.tv_no_data_volumes);
        recyclerViewVolumes = (RecyclerView) rootView.findViewById(R.id.rv_fragment_volumes);
        btnSendInvoice = (Button) rootView.findViewById(R.id.btn_send_invoice);
        tvHeaderHint = (TextView) rootView.findViewById(R.id.tv_header_hint);

        retrofitGetListForVpn();

        return rootView;
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
                            Toast.makeText(getContext(), "YEY " + isChecked + " pos " +  childPosition, Toast.LENGTH_SHORT).show();




                        }

                    });

                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                    recyclerViewVolumes.setLayoutManager(mLayoutManager);
                    recyclerViewVolumes.setAdapter(collateRVAdapter);



                    /*recyclerViewVolumes.addOnItemTouchListener(new CollateRVAdapter(getActivity(), objects, new CollateRVAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View childView, int childAdapterPosition) {


                            if (objects.get(childAdapterPosition) instanceof Label) {

                                if (((Label) objects.get(childAdapterPosition)).getClickStatus().equals("Click")) {

                                    ((Label) objects.get(childAdapterPosition)).setClickStatus("UnClick");

                                    Toast.makeText(getContext(), ((Label) objects.get(childAdapterPosition)).getClickStatus()
                                            , Toast.LENGTH_SHORT).show();

                                } else {

                                    ((Label) objects.get(childAdapterPosition)).setClickStatus("Click");

                                    Toast.makeText(getContext(), ((Label) objects.get(childAdapterPosition)).getClickStatus()
                                            , Toast.LENGTH_SHORT).show();


                                }

                            } else if (objects.get(childAdapterPosition) instanceof Packet) {
                                if (((Packet) objects.get(childAdapterPosition)).getClickStatus().equals("Click")) {

                                    ((Packet) objects.get(childAdapterPosition)).setClickStatus("UnClick");

                                    Toast.makeText(getContext(), ((Packet) objects.get(childAdapterPosition)).getClickStatus()
                                            , Toast.LENGTH_SHORT).show();
                                } else {
                                    ((Packet) objects.get(childAdapterPosition)).setClickStatus("Click");

                                    Toast.makeText(getContext(), ((Packet) objects.get(childAdapterPosition)).getClickStatus()
                                            , Toast.LENGTH_SHORT).show();
                                }
                            }

                            collateRVAdapter.notifyDataSetChanged();


                        }
                    }));
*/
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

}
