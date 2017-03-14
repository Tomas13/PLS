package ru.startandroid.retrofit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.startandroid.retrofit.Model.routes.Entry;
import ru.startandroid.retrofit.R;

import static ru.startandroid.retrofit.Const.CURRENT_ROUTE_POSITION;
import static ru.startandroid.retrofit.Const.FLIGHT_SHARED_PREF;

/**
 * Created by root on 12/27/16.
 */

public class RoutesRVAdapter extends RecyclerView.Adapter<RoutesRVAdapter.RoutesHolder> implements RecyclerView.OnItemTouchListener {
    private List<Entry> mRoutes;

    //for on click

    Context context;
    Activity activity;
    GestureDetector mGestureDetector;
    private OnItemClickListener listener;


    public static class RoutesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvNumber, tvIndex, tvName, tvDeparture, tvArrival;

        public RoutesHolder(View view) {
            super(view);

            tvNumber = (TextView) view.findViewById(R.id.tv_number_routes);
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


    public RoutesRVAdapter(List<Entry> routes, Context context) {
        mRoutes = routes;
        this.context = context;
    }


    public RoutesRVAdapter(Activity activity, List<Entry> routes, OnItemClickListener listener) {
        this.context = activity.getBaseContext();
        this.activity = activity;
        this.listener = listener;
        this.mRoutes = routes;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }


    @Override
    public RoutesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_routes, parent, false);
        return new RoutesHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RoutesHolder holder, int position) {
        Entry flight = mRoutes.get(position);

//        SharedPreferences pref = context.getSharedPreferences(FLIGHT_SHARED_PREF, Context.MODE_PRIVATE);
//        int currentPos = pref.getInt(CURRENT_ROUTE_POSITION, 0);

        holder.tvNumber.setText(String.valueOf(position + 1));
        holder.tvIndex.setText(flight.getDept().getName());// getIndex(position) + "");
        holder.tvArrival.setText(flight.getArrival());
        holder.tvDeparture.setText(flight.getDeparture());
        holder.tvName.setText(flight.getDept().getNameRu());

      /*  if (position == currentPos) {
            holder.tvNumber.setTextColor(Color.BLUE);
            holder.tvIndex.setTextColor(Color.BLUE);
            holder.tvArrival.setTextColor(Color.BLUE);
            holder.tvDeparture.setTextColor(Color.BLUE);
            holder.tvName.setTextColor(Color.BLUE);


            holder.tvNumber.setText(String.valueOf(position + 1));
            holder.tvIndex.setText(flight.getDept().getName());// getIndex(position) + "");
            holder.tvArrival.setText(flight.getArrival());
            holder.tvDeparture.setText(flight.getDeparture());
            holder.tvName.setText(flight.getDept().getNameRu());

        } else {

        }*/


    }


    @Override
    public int getItemCount() {
        return mRoutes.size();
    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && listener != null && mGestureDetector.onTouchEvent(e)) {
            listener.onItemClick(childView, rv.getChildAdapterPosition(childView));
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
