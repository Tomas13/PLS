package ru.startandroid.retrofit.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import ru.startandroid.retrofit.Model.geninvoice.GeneralInvoice;
import ru.startandroid.retrofit.R;


public class InvoiceRVAdapter extends RecyclerView.Adapter<InvoiceRVAdapter.InvoiceHolder> {
    private List<GeneralInvoice> mGeneralInvoice;

    public static class InvoiceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvGeneralInvoiceID, tvFromDeptName, tvFromDeptNameRu;
        Button btnRetreive;

        public InvoiceHolder(View view) {
            super(view);

            btnRetreive = (Button) view.findViewById(R.id.btn_retrieve);
            tvGeneralInvoiceID = (TextView) view.findViewById(R.id.tv_general_invoice_id);
            tvFromDeptName = (TextView) view.findViewById(R.id.tv_from_dept_name);
            tvFromDeptNameRu = (TextView) view.findViewById(R.id.tv_from_dept_name_ru);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("Routes", "Click on " + v.getId());
        }
    }


    public InvoiceRVAdapter(List<GeneralInvoice> routes) {
        mGeneralInvoice = routes;
    }

    @Override
    public InvoiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_invoice, parent, false);
        return new InvoiceHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(InvoiceHolder holder, int position) {
        GeneralInvoice generalInvoice = mGeneralInvoice.get(position);

//        Log.d("Main", "onBIND " + route..getStatus() + " ");
        holder.tvGeneralInvoiceID.setText(generalInvoice.getGeneralInvoiceId());
        holder.tvFromDeptName.setText(generalInvoice.getFromDep().getName());
        holder.tvFromDeptNameRu.setText(generalInvoice.getFromDep().getNameRu());

//        holder.btnRetreive.setText(generalInvoice.getDept().getNameRu());

    }


    @Override
    public int getItemCount() {
        return mGeneralInvoice.size();
    }


}
