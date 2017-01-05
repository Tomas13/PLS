package ru.startandroid.retrofit.ui;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.startandroid.retrofit.R;

import static ru.startandroid.retrofit.Const.FLIGHT_ROUTES;
import static ru.startandroid.retrofit.Const.FLIGHT_SHARED_PREF;
import static ru.startandroid.retrofit.Const.ROUTES;


/**
 * A simple {@link Fragment} subclass.
 */
public class FlightFragment extends Fragment {

    ListView listViewFlights;

    public FlightFragment() {
        // Required empty public constructor
    }

    ArrayAdapter adapter;
    ArrayList<String> flights;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flight, container, false);

        flights = getArguments().getStringArrayList("flightsList");


        Button btnOk = (Button) view.findViewById(R.id.btn_ok_flight);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), NavigationActivity.class));

            }
        });


        createDialog();

        listViewFlights = (ListView) view.findViewById(R.id.list_view_flight);

//        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.list_view_item, flights);
//        listViewFlights.setAdapter(adapter);

        listViewFlights.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Toast.makeText(getActivity().getApplicationContext(), "Saving " + flights.get(position), Toast.LENGTH_SHORT).show();
                //Save Flight Id to shared preferences
                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences(FLIGHT_SHARED_PREF, 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("FLIGHT_POS", position);
                editor.apply();


            }
        });

        return view;
    }

    private void createDialog(){
        final Dialog flightDialog = new Dialog(getContext());
        flightDialog.setContentView(R.layout.fragment_flight);

        ListView listView = (ListView) flightDialog.findViewById(R.id.list_view_flight);
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.list_view_item, flights);
        listView.setAdapter(adapter);

        flightDialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getActivity().getApplicationContext(), "Saving " + flights.get(position), Toast.LENGTH_SHORT).show();
                //Save Flight Id to shared preferences
                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("FLIGHT_PREF", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("FLIGHT_POS", position);
                editor.apply();


            }
        });

        Button btnOk = (Button) flightDialog.findViewById(R.id.btn_ok_flight);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flightDialog.dismiss();
            }
        });

    }
}
