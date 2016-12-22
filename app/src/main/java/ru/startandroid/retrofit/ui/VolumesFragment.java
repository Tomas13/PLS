package ru.startandroid.retrofit.ui;


import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.startandroid.retrofit.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class VolumesFragment extends Fragment implements View.OnClickListener {


    Button btnSendInvoice;
    TextView tvHeaderHint, ftv_ll_first, stv_ll_first, ttv_ll_first,
            ftv_ll_second, stv_ll_second, ttv_ll_second,
            ftv_ll_third, stv_ll_third, ttv_ll_third;

    LinearLayout firstLL, secondLL, thirdLL;

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


        btnSendInvoice = (Button) rootView.findViewById(R.id.btn_send_invoice);
        tvHeaderHint = (TextView) rootView.findViewById(R.id.tv_header_hint);

        firstLL = (LinearLayout) rootView.findViewById(R.id.ll_first);
        secondLL = (LinearLayout) rootView.findViewById(R.id.ll_second);
        thirdLL = (LinearLayout) rootView.findViewById(R.id.ll_third);

        ftv_ll_first = (TextView) rootView.findViewById(R.id.ftv_ll_first);
        stv_ll_first = (TextView) rootView.findViewById(R.id.stv_ll_first);
        ttv_ll_first = (TextView) rootView.findViewById(R.id.ttv_ll_first);

        ftv_ll_second = (TextView) rootView.findViewById(R.id.ftv_ll_second);
        stv_ll_second = (TextView) rootView.findViewById(R.id.stv_ll_second);
        ttv_ll_second = (TextView) rootView.findViewById(R.id.ttv_ll_second);


        ftv_ll_third = (TextView) rootView.findViewById(R.id.ftv_ll_third);
        stv_ll_third = (TextView) rootView.findViewById(R.id.stv_ll_third);
        ttv_ll_third = (TextView) rootView.findViewById(R.id.ttv_ll_third);

        firstLL.setOnClickListener(this);
        secondLL.setOnClickListener(this);
        thirdLL.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_first:
                tvHeaderHint.setVisibility(View.GONE);
                btnSendInvoice.setVisibility(View.VISIBLE);

                ftv_ll_first.setTextColor(Color.GREEN);
                stv_ll_first.setTextColor(Color.GREEN);
                ttv_ll_first.setTextColor(Color.GREEN);

                break;

            case R.id.ll_second:
                tvHeaderHint.setVisibility(View.GONE);
                btnSendInvoice.setVisibility(View.VISIBLE);

                ftv_ll_second.setTextColor(Color.GREEN);
                stv_ll_second.setTextColor(Color.GREEN);
                ttv_ll_second.setTextColor(Color.GREEN);

                break;

            case R.id.ll_third:
                tvHeaderHint.setVisibility(View.GONE);
                btnSendInvoice.setVisibility(View.VISIBLE);

                ftv_ll_third.setTextColor(Color.GREEN);
                stv_ll_third.setTextColor(Color.GREEN);
                ttv_ll_third.setTextColor(Color.GREEN);

                break;

        }
    }
}
