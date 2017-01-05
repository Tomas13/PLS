package ru.startandroid.retrofit.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.startandroid.retrofit.Model.History;
import ru.startandroid.retrofit.R;

import static ru.startandroid.retrofit.Const.ACCEPT_HISTORY_STATUS;
import static ru.startandroid.retrofit.Const.COLLATE_HISTORY_STATUS;
import static ru.startandroid.retrofit.Const.CREATED_HISTORY_STATUS;

/**
 * Created by root on 12/28/16.
 */

public class HistoryRVAdapter extends RecyclerView.Adapter<HistoryRVAdapter.HistoryHolder> {

    private List<History> historyList = new ArrayList<>();

    public HistoryRVAdapter(List<History> histories) {
        this.historyList = histories;
    }

    public static class HistoryHolder extends RecyclerView.ViewHolder {

        private TextView tvListId, tvFromDeptName, tvFromDeptNameRu, tvToDeptName, tvToDeptNameRu;

        private ImageView tvImage;

        public HistoryHolder(View itemView) {
            super(itemView);

            tvImage = (ImageView) itemView.findViewById(R.id.image_item_rv);

            tvListId = (TextView) itemView.findViewById(R.id.tv_list_id);
            tvFromDeptName = (TextView) itemView.findViewById(R.id.tv_from_dept_name);
            tvFromDeptNameRu = (TextView) itemView.findViewById(R.id.tv_from_dept_name_ru);
            tvToDeptName = (TextView) itemView.findViewById(R.id.tv_to_dept_name);
            tvToDeptNameRu = (TextView) itemView.findViewById(R.id.tv_to_dept_name_ru);

        }
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_history, parent, false);
        return new HistoryHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {

        History history = historyList.get(position);

        holder.tvListId.setText(history.getListId());
        holder.tvFromDeptName.setText(history.getFromDep().getName());
        holder.tvFromDeptNameRu.setText(history.getFromDep().getNameRu());
        holder.tvToDeptName.setText(history.getToDep().getName());
        holder.tvToDeptNameRu.setText(history.getToDep().getNameRu());

        switch (history.getStatus()) {
            case ACCEPT_HISTORY_STATUS:
                holder.tvImage.setImageResource(R.drawable.ic_cloud_download_black_18dp);
                holder.tvImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "Статус - Принят", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case COLLATE_HISTORY_STATUS:
                holder.tvImage.setImageResource(R.drawable.ic_cached_black_18dp);
                holder.tvImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "Статус - В процессе сличения", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case CREATED_HISTORY_STATUS:
                holder.tvImage.setImageResource(R.drawable.ic_done_black_18dp);
                holder.tvImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "Статус - Создан", Toast.LENGTH_SHORT).show();
                    }
                });
                break;


        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }


}
