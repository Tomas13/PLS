package ru.startandroid.retrofit.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import ru.startandroid.retrofit.Model.collatedestination.Label;
import ru.startandroid.retrofit.Model.collatedestination.Packet;
import ru.startandroid.retrofit.R;


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


        // Create the Realm instance
        realm = Realm.getDefaultInstance();

        // Build the query looking at all users:
        RealmQuery<Packet> queryPackets = realm.where(Packet.class);

        ArrayList<Packet> packetArrayList = new ArrayList<>();

        for (int i = 0; i < queryPackets.findAll().size(); i++) {
            packetArrayList.add(queryPackets.findAll().get(i));
        }


        RealmQuery<Label> queryLabels = realm.where(Label.class);
        ArrayList<Label> labelArrayList = new ArrayList<>();

        for (int i = 0; i < queryLabels.findAll().size(); i++) {
            labelArrayList.add(queryLabels.findAll().get(i));
        }




        tvNoDataCollate = (TextView) view.findViewById(R.id.tv_no_data_collate);
        rvCollate = (RecyclerView) view.findViewById(R.id.rv_fragment_collate);


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
