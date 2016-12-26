package ru.startandroid.retrofit.ui;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.databinding.FragmentRoutesBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class RoutesFragment extends Fragment {


    FragmentRoutesBinding fragmentRoutesBinding;

    public RoutesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        fragmentRoutesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_routes, container, false);
//        View viewRoot = inflater.inflate(R.layout.fragment_routes, container, false);

        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle("Маршруты");



        fragmentRoutesBinding.rvRoutes.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        return fragmentRoutesBinding.getRoot();
    }

}
