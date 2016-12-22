package ru.startandroid.retrofit.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.startandroid.retrofit.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RoutesFragment extends Fragment {


    public RoutesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View viewRoot = inflater.inflate(R.layout.fragment_routes, container, false);

        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle("Маршруты");


        return viewRoot;
    }

}
