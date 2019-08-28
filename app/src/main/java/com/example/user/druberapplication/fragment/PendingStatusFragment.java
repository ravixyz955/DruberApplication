package com.example.user.druberapplication.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.druberapplication.R;
import com.example.user.druberapplication.adapter.MissionsListAdapter;
import com.example.user.druberapplication.constant.Constant;
import com.example.user.druberapplication.network.model.Mission;
import com.example.user.druberapplication.network.model.MissionSteps;
import com.example.user.druberapplication.utils.ItemOffsetDecoration;

import java.util.ArrayList;

public class PendingStatusFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pending_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<Mission> adapterList = new ArrayList<>();
        ArrayList<Mission> pendingmissions = null;

        Bundle extras = getArguments();
        ArrayList<Mission> missions = extras.getParcelableArrayList("missions");
        RecyclerView missions_recycler_view = view.findViewById(R.id.missions_recyler_view);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.nav_header_vertical_spacing);
        missions_recycler_view.addItemDecoration(itemDecoration);
        missions_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (missions.size() > 0) {

            pendingmissions = new ArrayList<>(missions);
            for (Mission pendingmission : pendingmissions) {
                MissionSteps missionStep = pendingmission.getMissionSteps().get(0);
                if (missionStep.getStatus() == null)
                    adapterList.add(pendingmission);
            }
            missions_recycler_view.setAdapter(new MissionsListAdapter(getActivity(), missions, adapterList, Constant.PENDING));
        }
    }
}
