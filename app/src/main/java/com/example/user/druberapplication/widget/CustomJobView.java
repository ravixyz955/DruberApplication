package com.example.user.druberapplication.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.druberapplication.FlightPathsActivity;
import com.example.user.druberapplication.R;
import com.example.user.druberapplication.constant.Constant;
import com.example.user.druberapplication.network.model.FlightPaths;
import com.example.user.druberapplication.network.model.Mission;
import com.example.user.druberapplication.network.model.MissionSteps;
import com.example.user.druberapplication.network.model.User;
import com.example.user.druberapplication.utils.DataUtils;
import com.example.user.druberapplication.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ViewConstructor")
public class CustomJobView extends LinearLayout {

    @BindView(R.id.mission_id)
    TextView mission_id;
    @BindView(R.id.project_id)
    TextView project_id;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.mission_name)
    TextView mission_name;
    @BindView(R.id.time)
    TextView mission_time;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.mission_icon)
    ImageView mission_icon;
    ArrayList<FlightPaths> fp = new ArrayList<>();
    private Context mContext;
    private String type;
    private Mission mission;
    private int projectPositon;
    private boolean isInProgress;
    private ArrayList<Mission> missions, parentMissions;

    public CustomJobView(Context context, ArrayList<Mission> parentMissions, ArrayList<Mission> missions, String type) {
        super(context);
        this.mContext = context;
        this.type = type;
        this.parentMissions = parentMissions;
        this.missions = missions;
        init();
    }

    @SuppressLint("RestrictedApi")
    private void init() {
        View itemView = inflate(getContext(), R.layout.mission_item_layout, this);
        ButterKnife.bind(this, itemView);

        if (type.contains(Constant.PENDING)) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                ((AppCompatImageView) mission_icon).setSupportBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOrange)));
            } else {
                ViewCompat.setBackgroundTintList(mission_icon, ColorStateList.valueOf(getResources().getColor(R.color.colorOrange)));
            }
        } else if (type.contains(Constant.INPROGRESS)) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                ((AppCompatImageView) mission_icon).setSupportBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlue)));
            } else {
                ViewCompat.setBackgroundTintList(mission_icon, ColorStateList.valueOf(getResources().getColor(R.color.colorBlue)));
            }
        } else if (type.contains(Constant.COMPLETE)) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                ((AppCompatImageView) mission_icon).setSupportBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGreen)));
            } else {
                ViewCompat.setBackgroundTintList(mission_icon, ColorStateList.valueOf(getResources().getColor(R.color.colorGreen)));
            }
        }

        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parentMissions.size() > 0) {
                    for (Mission inprogressmission : parentMissions) {
                        if (mission != null) {
                            MissionSteps missionStep = inprogressmission.getMissionSteps().get(0);
                            if (missionStep.getStatus() != null && missionStep.getStatus().equalsIgnoreCase(Constant.INPROGRESS) &&
                                    type.equalsIgnoreCase(Constant.PENDING)) {
                                Utils.inprogressDialog(mContext, "Please complete inprogress jobs");
                                isInProgress = true;
                                break;
                            }
                        }
                    }
                }
//                if (!isInProgress) {
//                    Intent intent = new Intent(mContext, JobOptionsActivity.class);
//                    intent.putExtra("type", type);
//                    intent.putExtra(Mission.class.getName(), mission);
//                    (mContext).startActivity(intent);
//                }
                if (!isInProgress) {
                    if (mission != null && mission.getFlightPaths() != null) {
                        if (mission.getFlightPaths().size() > 0) {
                            fp = mission.getFlightPaths();
                            Intent intent = new Intent(mContext, FlightPathsActivity.class);
                            intent.putExtra("type", type);
                            intent.putExtra(Mission.class.getName(), mission);
                            intent.putParcelableArrayListExtra("getFlightPaths", fp);
                            (mContext).startActivity(intent);
                        } else {
                            Utils.showToast(mContext, "No Paths Found!");
                        }
                    } else {
                        Utils.showToast(mContext, "No Paths Found!");
                    }
                }
            }
        });
    }

    public void setData(Mission mission, int projectPosition) {
        this.mission = mission;
        this.projectPositon = projectPosition;
        ArrayList<MissionSteps> missionSteps = mission.getMissionSteps();
        String startTime, endTime, finalTime = null;
        MissionSteps missionStep = missionSteps.get(0);
        mission_id.setText(mission.getMission().getName());
        if (mission.getProject() != null)
            project_id.setText(mission.getProject().getName());
        try {
            mission_name.setText(missionStep.getName());
            User user = DataUtils.getUser(mContext);
            if (user != null) {
                user_name.setText(user.getFullName());
            } else {
                user_name.setText("-");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        date.setText(getParseDate(missionStep.getDate(), Constant.DATE));
//        date.setText(missionStep.getDate());
        startTime = getParseDate(missionStep.getStart_time(), Constant.TIME);
        endTime = getParseDate(missionStep.getEnd_time(), Constant.TIME);
        if (startTime != null && endTime != null)
            finalTime = startTime + " - " + endTime;
        else if (startTime != null)
            finalTime = startTime;
        else if (endTime != null)
            finalTime = endTime;
        mission_time.setText(finalTime);
    }

    private String getParseDate(String createdAt, String dateStr) {
        String date1 = null;
        if (createdAt != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date;
            if (createdAt != null && dateStr != null && dateStr.contains(Constant.DATE)) {
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                sdf1.setTimeZone(TimeZone.getTimeZone("IST"));
                try {
                    date = sdf.parse(createdAt);
                    date1 = sdf1.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (createdAt != null && dateStr != null && dateStr.contains(Constant.TIME)) {
                SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm", Locale.US);
                try {
                    date = sdf.parse(createdAt);
                    date1 = sdf1.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                date1 = "-";
            }
        } else {
            date1 = "-";
        }
        return date1;
    }
}