package ru.startandroid.retrofit.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmQuery;
import ru.startandroid.retrofit.Model.collatedestination.Dto;
import ru.startandroid.retrofit.Model.collatedestination.Label;
import ru.startandroid.retrofit.Model.collatedestination.Packet;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.adapter.CollateRVAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class CollateFragment extends Fragment {


    private TextView tvNoDataCollate;
    private RecyclerView rvCollate;

    private Realm realm;

    public CollateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collate, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("G и B накладные");

        rvCollate = (RecyclerView) view.findViewById(R.id.rv_fragment_collate);
        tvNoDataCollate = (TextView) view.findViewById(R.id.tv_no_data_collate);

        // Create the Realm instance
        realm = Realm.getDefaultInstance();

        // Build the query looking at all users:
        RealmQuery<Packet> queryPacket = realm.where(Packet.class);
        List<Packet> listPackets = queryPacket.findAll();

        RealmQuery<Label> queryLabel = realm.where(Label.class);
        List<Label> listLabels= queryLabel.findAll();

        ArrayList<Object> items = new ArrayList<>();

        items.addAll(listLabels);
        items.addAll(listPackets);


        tvNoDataCollate = (TextView) view.findViewById(R.id.tv_no_data_collate);
        rvCollate = (RecyclerView) view.findViewById(R.id.rv_fragment_collate);

//        CollateRVAdapter collateRVAdapter = new CollateRVAdapter(items);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvCollate.setLayoutManager(mLayoutManager);
//        rvCollate.setAdapter(collateRVAdapter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
