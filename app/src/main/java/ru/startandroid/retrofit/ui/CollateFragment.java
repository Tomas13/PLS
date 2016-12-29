package ru.startandroid.retrofit.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.startandroid.retrofit.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CollateFragment extends Fragment {


    private TextView tvNoDataCollate;
    private RecyclerView rvCollate;

    public CollateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collate, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("G и B накладные");

        tvNoDataCollate = (TextView) view.findViewById(R.id.tv_no_data_collate);
        rvCollate = (RecyclerView) view.findViewById(R.id.rv_fragment_collate);


        return view;
    }

}
