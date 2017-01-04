package ru.startandroid.retrofit.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.startandroid.retrofit.Model.collatedestination.Dto;
import ru.startandroid.retrofit.Model.collatedestination.Label;
import ru.startandroid.retrofit.Model.collatedestination.Packet;
import ru.startandroid.retrofit.R;

/**
 * Created by root on 12/29/16.
 */

public class CollateRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    private List<Object> items;

    private final int LABEL = 0, PACKET = 1;

    private Dto dtoObject;

    public CollateRVAdapter(ArrayList<Object> objects) {

        items = objects;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LABEL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_history, parent, false);
            return new ViewHolder1(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_history, parent, false);
            return new ViewHolder1(view);
        }
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {

        private TextView tvListId1, tvFromDeptName1, tvFromDeptNameRu1, tvToDeptName1, tvToDeptNameRu1;


        public ViewHolder1(View v) {
            super(v);
            tvListId1 = (TextView) itemView.findViewById(R.id.tv_list_id);
            tvFromDeptName1 = (TextView) itemView.findViewById(R.id.tv_from_dept_name);
            tvFromDeptNameRu1 = (TextView) itemView.findViewById(R.id.tv_from_dept_name_ru);
            tvToDeptName1 = (TextView) itemView.findViewById(R.id.tv_to_dept_name);
            tvToDeptNameRu1 = (TextView) itemView.findViewById(R.id.tv_to_dept_name_ru);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {

        private TextView tvListId2, tvFromDeptName2, tvFromDeptNameRu2, tvToDeptName2, tvToDeptNameRu2;


        public ViewHolder2(View v) {
            super(v);
            tvListId2 = (TextView) itemView.findViewById(R.id.tv_list_id);
            tvFromDeptName2 = (TextView) itemView.findViewById(R.id.tv_from_dept_name);
            tvFromDeptNameRu2 = (TextView) itemView.findViewById(R.id.tv_from_dept_name_ru);
            tvToDeptName2 = (TextView) itemView.findViewById(R.id.tv_to_dept_name);
            tvToDeptNameRu2 = (TextView) itemView.findViewById(R.id.tv_to_dept_name_ru);
        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType = holder.getItemViewType();

        if (viewType == LABEL){
            ViewHolder1 viewHolder1 = (ViewHolder1) holder;

            Label label = (Label) items.get(position);
            viewHolder1.tvListId1.setText(label.getLabelListid());
            viewHolder1.tvFromDeptName1.setText(label.getFromDep().getName());
            viewHolder1.tvFromDeptNameRu1.setText(label.getFromDep().getNameRu());
            viewHolder1.tvToDeptName1.setText(label.getToDep().getName());
            viewHolder1.tvToDeptName1.setText(label.getToDep().getNameRu());

        } else if (viewType == PACKET){

            ViewHolder1 viewHolder1 = (ViewHolder1) holder;

            Packet packet = (Packet) items.get(position);
            viewHolder1.tvListId1.setText(packet.getPacketListId());
            viewHolder1.tvFromDeptName1.setText(packet.getFromDep().getName());
            viewHolder1.tvFromDeptNameRu1.setText(packet.getFromDep().getNameRu());
            viewHolder1.tvToDeptName1.setText(packet.getToDep().getName());
            viewHolder1.tvToDeptName1.setText(packet.getToDep().getNameRu());

        }

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Label) {
            return LABEL;
        } else if (items.get(position) instanceof String) {
            return PACKET;
        }
        return -1;
    }


}
