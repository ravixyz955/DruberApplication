package com.example.user.druberapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.user.druberapplication.network.model.FlightPaths;
import com.example.user.druberapplication.network.model.Mission;
import com.example.user.druberapplication.widget.CustomFlightPathView;

import java.util.ArrayList;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightPathViewHolder> {
    private Context mContext;
    private ArrayList<FlightPaths> flightPaths;
    private int lastPosition = -1;
    private String typeSting;
    private Mission mission;

    public FlightAdapter(Context context, ArrayList<FlightPaths> flightPaths, Mission mission, String typeSting) {
        this.mContext = context;
        this.flightPaths = flightPaths;
        this.typeSting = typeSting;
        this.mission = mission;
    }

    @NonNull
    @Override
    public FlightPathViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomFlightPathView itemView = new CustomFlightPathView(parent.getContext(), flightPaths, typeSting);
        itemView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return new FlightPathViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightPathViewHolder holder, int position) {
        holder.getCustomView().setData(mission, flightPaths.get(position), position);
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


    @Override
    public int getItemCount() {
        return flightPaths.size();
    }

    class FlightPathViewHolder extends RecyclerView.ViewHolder {

        private CustomFlightPathView customView;

        public FlightPathViewHolder(View v) {
            super(v);
            customView = (CustomFlightPathView) v;
        }

        public CustomFlightPathView getCustomView() {
            return customView;
        }
    }
}
