package com.example.user.druberapplication.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.user.druberapplication.fragment.CompletedStatusFragment;
import com.example.user.druberapplication.fragment.InprogressStatusFragment;
import com.example.user.druberapplication.fragment.PendingStatusFragment;
import com.example.user.druberapplication.network.model.Mission;

import java.util.ArrayList;

public class Pager extends FragmentStatePagerAdapter {
    Bundle args0 = new Bundle();
    private String[] tabTitles = new String[]{"Pending", "Inprogress", "Completed"};
    private ArrayList<Mission> missions;

    public Pager(FragmentManager fm, int tabCount, ArrayList<Mission> missions) {
        super(fm);
        this.missions = missions;
        args0.putParcelableArrayList("missions", missions);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PendingStatusFragment pendingFragment = new PendingStatusFragment();
                pendingFragment.setArguments(args0);
                return pendingFragment;
            case 1:
                InprogressStatusFragment inprogressFragment = new InprogressStatusFragment();
                inprogressFragment.setArguments(args0);
                return inprogressFragment;
            case 2:
                CompletedStatusFragment completedFragment = new CompletedStatusFragment();
                completedFragment.setArguments(args0);
                return completedFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}