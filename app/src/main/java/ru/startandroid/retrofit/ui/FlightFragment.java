package ru.startandroid.retrofit.ui;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.startandroid.retrofit.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FlightFragment extends Fragment {

    ListView listViewFlights;

    public FlightFragment() {
        // Required empty public constructor
    }

    // Array of strings...
    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};
    ArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flight, container, false);

        ArrayList<String> flights = getArguments().getStringArrayList("flightsList");


        listViewFlights = (ListView) view.findViewById(R.id.list_view_flight);

        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.list_view_item, flights);
        listViewFlights.setAdapter(adapter);


        return view;
    }

}
