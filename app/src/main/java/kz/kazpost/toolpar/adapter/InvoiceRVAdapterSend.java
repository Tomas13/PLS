package kz.kazpost.toolpar.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import kz.kazpost.toolpar.Model.SendInvoice;
import kz.kazpost.toolpar.R;

import static kz.kazpost.toolpar.Const.INVOICE_NAME;
import static kz.kazpost.toolpar.Const.INVOICE_PREF;


public class InvoiceRVAdapterSend extends RecyclerView.Adapter<InvoiceRVAdapterSend.InvoiceHolder> implements RecyclerView.OnItemTouchListener  {
    private List<SendInvoice> sendInvoice;

//for on click

    Context context;
    Activity activity;
    GestureDetector mGestureDetector;
    private OnItemClickListener listener;

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


    public InvoiceRVAdapterSend(List<SendInvoice> routes) {
        sendInvoice = routes;
    }


    public InvoiceRVAdapterSend(Activity activity, List<SendInvoice> routes, InvoiceRVAdapterSend.OnItemClickListener listener) {
        this.context = activity.getBaseContext();
        this.activity = activity;
        this.listener = listener;
        this.sendInvoice = routes;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public InvoiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_invoice, parent, false);
        return new InvoiceHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(InvoiceHolder holder, final int position) {
        SendInvoice generalInvoice = sendInvoice.get(position);


        SharedPreferences ref = activity.getSharedPreferences(INVOICE_PREF, Context.MODE_PRIVATE);

        String timeOfEvent = "O" + ref.getString(INVOICE_NAME, "default");

//        Log.d("Main", "onBIND " + route..getStatus() + " ");
        holder.tvGeneralInvoiceID.setText(timeOfEvent);
        holder.tvFromDeptName.setText(generalInvoice.getFrom());
        holder.tvFromDeptNameRu.setText(generalInvoice.getTo());
//        holder.tvFromDeptNameRu.setText(generalInvoice);

        holder.btnRetreive.setText("Отправить");
        holder.btnRetreive.setOnClickListener(v -> listener.onItemClick(v, position));
    }


    @Override
    public int getItemCount() {
        return sendInvoice.size();
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
