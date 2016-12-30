package ru.startandroid.retrofit.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.startandroid.retrofit.Model.collatedestination.Dto;
import ru.startandroid.retrofit.Model.collatedestination.Label;
import ru.startandroid.retrofit.Model.collatedestination.Packet;
import ru.startandroid.retrofit.R;

/**
 * Created by root on 12/29/16.
 */

public class CollateRVAdapter extends RecyclerView.Adapter<CollateRVAdapter.BGHolder>{

//    private ArrayList<Dto> labelArrayList = new ArrayList<>();

    private Dto dtoObject;

    public CollateRVAdapter(Dto dto) {
        this.dtoObject = dto;
    }

    public static class BGHolder extends RecyclerView.ViewHolder{

        private TextView tvListId, tvFromDeptName, tvFromDeptNameRu, tvToDeptName, tvToDeptNameRu;

        public BGHolder(View itemView) {
            super(itemView);

            tvListId = (TextView) itemView.findViewById(R.id.tv_list_id);
            tvFromDeptName = (TextView) itemView.findViewById(R.id.tv_from_dept_name);
            tvFromDeptNameRu = (TextView) itemView.findViewById(R.id.tv_from_dept_name_ru);
            tvToDeptName = (TextView) itemView.findViewById(R.id.tv_to_dept_name);
            tvToDeptNameRu = (TextView) itemView.findViewById(R.id.tv_to_dept_name_ru);

        }
    }



    @Override
    public BGHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_history, parent, false);
        return new BGHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(BGHolder holder, int position) {

        holder.tvListId.setText(dtoObject.getLabels().get(position).getLabelListid());
        holder.tvFromDeptName.setText(dtoObject.getLabels().get(position).getFromDep().getName());
        holder.tvFromDeptNameRu.setText(dtoObject.getLabels().get(position).getFromDep().getNameRu());
        holder.tvToDeptName.setText(dtoObject.getLabels().get(position).getToDep().getName());
        holder.tvToDeptNameRu.setText(dtoObject.getLabels().get(position).getToDep().getNameRu());


        holder.tvListId.setText(dtoObject.getPackets().get(position).getPacketListId());
        holder.tvFromDeptName.setText(dtoObject.getPackets().get(position).getFromDep().getName());
        holder.tvFromDeptNameRu.setText(dtoObject.getPackets().get(position).getFromDep().getNameRu());
        holder.tvToDeptName.setText(dtoObject.getPackets().get(position).getToDep().getName());
        holder.tvToDeptNameRu.setText(dtoObject.getPackets().get(position).getToDep().getNameRu());


//
//        for (int i = position; i < dtoObject.getPackets().size(); i++) {
//
//            holder.tvListId.setText(dtoObject.getPackets().get(i).getPacketListId());
//            holder.tvFromDeptName.setText(dtoObject.getPackets().get(i).getFromDep().getName());
//            holder.tvFromDeptNameRu.setText(dtoObject.getPackets().get(i).getFromDep().getNameRu());
//            holder.tvToDeptName.setText(dtoObject.getPackets().get(i).getToDep().getName());
//            holder.tvToDeptNameRu.setText(dtoObject.getPackets().get(i).getToDep().getNameRu());
//
//        }


//        for (int j = position; j < dtoObject.getLabels().size(); j++) {
//
//            holder.tvListId.setText(dtoObject.getPackets().get(j).getPacketListId());
//            holder.tvFromDeptName.setText(dtoObject.getPackets().get(j).getFromDep().getName());
//            holder.tvFromDeptNameRu.setText(dtoObject.getPackets().get(j).getFromDep().getNameRu());
//            holder.tvToDeptName.setText(dtoObject.getPackets().get(j).getToDep().getName());
//            holder.tvToDeptNameRu.setText(dtoObject.getPackets().get(j).getToDep().getNameRu());
//
//        }
    }

    @Override
    public int getItemCount() {
        return dtoObject.getLabels().size() + dtoObject.getPackets().size();
    }

}
