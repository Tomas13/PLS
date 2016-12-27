package ru.startandroid.retrofit.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textservice.TextInfo;
import android.widget.TextView;

import java.util.ArrayList;

import ru.startandroid.retrofit.Model.routes.Routes;
import ru.startandroid.retrofit.R;

/**
 * Created by root on 12/27/16.
 */

public class RoutesRVAdapter extends RecyclerView.Adapter<RoutesRVAdapter.RoutesHolder> {
    private ArrayList<Routes> mRoutes;

    public static class RoutesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvIndex, tvName, tvDeparture, tvArrival;

        public RoutesHolder(View view) {
            super(view);

            tvIndex = (TextView) view.findViewById(R.id.tv_index);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvDeparture = (TextView) view.findViewById(R.id.tv_departure_time);
            tvArrival = (TextView) view.findViewById(R.id.tv_arrival_time);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("Routes", "Click on " + v.getId());
        }
    }


    public RoutesRVAdapter(ArrayList<Routes> routes) {
        mRoutes = routes;
    }

    @Override
    public RoutesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_routes, parent, false);
        return new RoutesHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RoutesHolder holder, int position) {
        Routes route = mRoutes.get(position);

        Log.d("Main", "onBIND " + route.getStatus() + " ");
        holder.tvIndex.setText(route.getIndex(position) + "");
        holder.tvArrival.setText(route.getArrival(position));
        holder.tvDeparture.setText(route.getArrival(position));
        holder.tvName.setText(route.getName(position));

    }


    @Override
    public int getItemCount() {
        return mRoutes.size();
    }


}
