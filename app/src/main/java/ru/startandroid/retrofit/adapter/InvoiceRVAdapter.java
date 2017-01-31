package ru.startandroid.retrofit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.startandroid.retrofit.Model.SendInvoice;
import ru.startandroid.retrofit.Model.geninvoice.GeneralInvoice;
import ru.startandroid.retrofit.R;
import ru.startandroid.retrofit.ui.NavigationActivity;


public class InvoiceRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerView.OnItemTouchListener  {
    private List<GeneralInvoice> mGeneralInvoice;

    private List<Object> objectList;
    private final int GENINVOICE  = 0, SENDINVOICE = 1;

//for on click

    private Context context;
    private Activity activity;
    private GestureDetector mGestureDetector;
    private OnItemClickListener listener;

    public static class SendInvoiceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvGeneralInvoiceID, tvFromDeptName, tvFromDeptNameRu;
        Button btnRetreive;

        public SendInvoiceHolder(View view) {
            super(view);

            btnRetreive = (Button) view.findViewById(R.id.btn_retrieve);
            tvGeneralInvoiceID = (TextView) view.findViewById(R.id.tv_general_invoice_id);
            tvFromDeptName = (TextView) view.findViewById(R.id.tv_from_dept_name);
            tvFromDeptNameRu = (TextView) view.findViewById(R.id.tv_from_dept_name_ru);
            //view.setOnClickListener(this);

//            btnRetreive.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("Routes", "Click on " + getAdapterPosition() + " id " +v.getId());

//            v.getRootView().getContext().getApplicationContext().startActivity(new Intent(v.getContext(), NavigationActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }


    public static class InvoiceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvGeneralInvoiceID, tvFromDeptName, tvFromDeptNameRu;
        Button btnRetreive;

        public InvoiceHolder(View view) {
            super(view);

            btnRetreive = (Button) view.findViewById(R.id.btn_retrieve);
            tvGeneralInvoiceID = (TextView) view.findViewById(R.id.tv_general_invoice_id);
            tvFromDeptName = (TextView) view.findViewById(R.id.tv_from_dept_name);
            tvFromDeptNameRu = (TextView) view.findViewById(R.id.tv_from_dept_name_ru);
            //view.setOnClickListener(this);

//            btnRetreive.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("Routes", "Click on " + getAdapterPosition() + " id " +v.getId());

//            v.getRootView().getContext().getApplicationContext().startActivity(new Intent(v.getContext(), NavigationActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }


   /* public InvoiceRVAdapter(Activity activity, List<GeneralInvoice> routes, InvoiceRVAdapter.OnItemClickListener listener) {
        this.context = activity.getBaseContext();
        this.activity = activity;
        this.listener = listener;
        this.mGeneralInvoice = routes;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }*/

    public InvoiceRVAdapter(Activity activity, List<Object> objectList, InvoiceRVAdapter.OnItemClickListener listener) {
        this.context = activity.getBaseContext();
        this.activity = activity;
        this.listener = listener;
        this.objectList = objectList;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == GENINVOICE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_invoice, parent, false);
            return new InvoiceRVAdapter.InvoiceHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_invoice, parent, false);
            return new InvoiceRVAdapter.SendInvoiceHolder(view);
        }


//        View inflatedView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_rv_invoice, parent, false);
//        return new InvoiceHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        int viewType = holder.getItemViewType();

        if (viewType == GENINVOICE) {
            InvoiceHolder viewHolder1 = (InvoiceHolder) holder;

            GeneralInvoice generalInvoice = (GeneralInvoice) objectList.get(position);

//        Log.d("Main", "onBIND " + route..getStatus() + " ");
            viewHolder1.tvGeneralInvoiceID.setText(generalInvoice.getGeneralInvoiceId());
            viewHolder1.tvFromDeptName.setText(generalInvoice.getFromDep().getName());
            viewHolder1.tvFromDeptNameRu.setText(generalInvoice.getFromDep().getNameRu());

            viewHolder1.btnRetreive.setOnClickListener(v -> listener.onItemClick(v, position));


        } else if (viewType == SENDINVOICE){
            SendInvoiceHolder viewHolder1 = (SendInvoiceHolder) holder;

            SendInvoice sendInvoice = (SendInvoice) objectList.get(position);

//
//        Log.d("Main", "onBIND " + route..getStatus() + " ");
            viewHolder1.tvGeneralInvoiceID.setText("O20170130110809123");
            viewHolder1.tvFromDeptName.setText(sendInvoice.getWhere());
//        viewHolder1.tvFromDeptNameRu.setText(generalInvoice.getFromDep().getNameRu());

            viewHolder1.btnRetreive.setText("Отправить");
            viewHolder1.btnRetreive.setOnClickListener(v -> listener.onItemClick(v, position));

        }

    }


    @Override
    public int getItemViewType(int position) {
        if (objectList.get(position) instanceof GeneralInvoice) {
            return GENINVOICE;
        } else if (objectList.get(position) instanceof SendInvoice) {
            return SENDINVOICE;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }



    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && listener != null && mGestureDetector.onTouchEvent(e)) {
            listener.onItemClick(childView, rv.getChildAdapterPosition(childView));
            return  true;
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface OnItemClickListener {

        void onItemClick(View childView, int childAdapterPosition);
    }

}
