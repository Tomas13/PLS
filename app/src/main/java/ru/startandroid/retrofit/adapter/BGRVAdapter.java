package ru.startandroid.retrofit.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.startandroid.retrofit.Model.collatedestination.Label;
import ru.startandroid.retrofit.Model.collatedestination.Packet;
import ru.startandroid.retrofit.R;

/**
 * Created by root on 12/29/16.
 */

public class BGRVAdapter extends RecyclerView.Adapter<BGRVAdapter.BGHolder>{

    private ArrayList<Packet> packetArrayList = new ArrayList<>();
    private ArrayList<Label> labelArrayList = new ArrayList<>();

    public BGRVAdapter(ArrayList<Packet> packetArrayList, ArrayList<Label> labelArrayList) {
        this.packetArrayList = packetArrayList;
        this.labelArrayList = labelArrayList;
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
        return null;
    }

    @Override
    public void onBindViewHolder(BGHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
