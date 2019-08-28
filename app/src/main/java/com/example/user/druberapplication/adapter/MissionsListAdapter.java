package com.example.user.druberapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.user.druberapplication.network.model.Mission;
import com.example.user.druberapplication.widget.CustomJobView;

import java.util.ArrayList;

public class MissionsListAdapter extends RecyclerView.Adapter<MissionsListAdapter.MissionsViewHolder> {
    private Context mContext;
    private ArrayList<Mission> missions, parentMissions;
    private int lastPosition = -1;
    private String type;

    public MissionsListAdapter(Context mContext, ArrayList<Mission> parentMissions, ArrayList<Mission> missions, String type) {
        this.mContext = mContext;
        this.missions = missions;
        this.parentMissions = parentMissions;
        this.type = type;
    }

    @NonNull
    @Override
    public MissionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomJobView itemView = new CustomJobView(parent.getContext(), parentMissions, missions, type);
        itemView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return new MissionsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MissionsViewHolder holder, int position) {
        holder.getCustomView().setData(missions.get(position), position);
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
        return missions.size();
    }

    class MissionsViewHolder extends RecyclerView.ViewHolder {

        private CustomJobView customView;

        public MissionsViewHolder(View v) {
            super(v);
            customView = (CustomJobView) v;
        }

        public CustomJobView getCustomView() {
            return customView;
        }
    }
}