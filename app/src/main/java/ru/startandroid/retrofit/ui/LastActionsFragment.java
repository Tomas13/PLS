package ru.startandroid.retrofit.ui;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.databinding.FragmentLastActionsBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class LastActionsFragment extends Fragment {


    public LastActionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        FragmentLastActionsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_last_actions,
                container, false);
//        View viewRoot = inflater.inflate(R.layout.fragment_last_actions, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Последние действия");


        getHistory();


        return binding.getRoot();
    }

    private void getHistory() {

    }


}
