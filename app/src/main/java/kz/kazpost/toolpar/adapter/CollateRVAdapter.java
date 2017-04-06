package kz.kazpost.toolpar.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import kz.kazpost.toolpar.Model.acceptgen.Destination;
import kz.kazpost.toolpar.R;

/**
 * Created by root on 3/27/17.
 */

public class CollateRVAdapter extends RecyclerView.Adapter<CollateRVAdapter.ViewHolder1> implements RecyclerView.OnItemTouchListener {

    private Context context;
    private Activity activity;
    private GestureDetector mGestureDetector;
    private CollateRVAdapter.OnItemClickListener listener;
    private List<Destination> items;


    private HashMap<Integer, Boolean> isChecked = new HashMap<>();


    public CollateRVAdapter(Activity activity, List<Destination> items, CollateRVAdapter.OnItemClickListener listener) {
        this.context = activity.getBaseContext();
        this.activity = activity;
        this.listener = listener;
        this.items = items;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {

        private TextView tvListId1, tvFromDeptName1, tvFromDeptNameRu1, tvToDeptName1, tvToDeptNameRu1;

        private LinearLayout linearLayout;
        private CheckBox checkBox;


        public ViewHolder1(View v) {
            super(v);
//            tvListId1 = (TextView) itemView.findViewById(R.id.tv_list_id_collate);
//            tvToDeptName1 = (TextView) itemView.findViewById(R.id.tv_to_dept_name_collate);
//            tvToDeptNameRu1 = (TextView) itemView.findViewById(R.id.tv_to_dept_name_ru_collate);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox_collate);
            checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
                isChecked.put(getAdapterPosition(), b);
            });
            linearLayout = (LinearLayout) itemView.findViewById(R.id.ll_rv_collate);
        }
    }


    @Override
    public ViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_collate, parent, false);

        return new ViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder1 holder, int position) {

//        CollateRVAdapter.ViewHolder1 viewHolder1 = (CollateRVAdapter.ViewHolder1) holder;

        Destination destination = items.get(position);
        holder.checkBox.setText(destination.getDestinationListId() + "\t\t\t\t\t" + destination.getToDeNewp().getNameRu() + " ["
                + destination.getToDeNewp().getName() + "]");
//        holder.tvListId1.setText(destination.getDestinationListId());
//        holder.tvFromDeptName1.setText(destination.getFromDep().getName());
//        holder.tvFromDeptNameRu1.setText(destination.getFromDep().getNameRu());
//        holder.tvToDeptName1.setText(destination.getToDeNewp().getName());
//        holder.tvToDeptNameRu1.setText(destination.getToDeNewp().getNameRu());


        holder.checkBox.setChecked(destination.getIsChecked());

        if (holder.checkBox.isChecked()) {
            holder.linearLayout.setBackgroundColor(Color.GREEN);
        } else {
            holder.linearLayout.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.checkBox.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    listener.onCheckedChanged(isChecked, position);
                    if (isChecked) {
                        holder.linearLayout.setBackgroundColor(Color.GREEN);
                    } else {
                        holder.linearLayout.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
        );


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && listener != null && mGestureDetector.onTouchEvent(e)) {
//            listener.onItemClick(childView, rv.getChildAdapterPosition(childView));
            return true;
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    public interface OnItemClickListener {

//        void onItemClick(View childView, int childAdapterPosition);

        void onCheckedChanged(boolean isChecked, int position);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public void setChecked(boolean isChecked, int pos) {
        listener.onCheckedChanged(isChecked, pos);
    }

}
