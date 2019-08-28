package com.example.user.druberapplication.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Mission implements Parcelable {

    public static final Creator<Mission> CREATOR = new Creator<Mission>() {
        @Override
        public Mission createFromParcel(Parcel in) {
            return new Mission(in);
        }

        @Override
        public Mission[] newArray(int size) {
            return new Mission[size];
        }
    };

    @SerializedName("_id")
    private String _id;

    @SerializedName("mission")
    private MMission mission;

    @SerializedName("project")
    private Project project;

    @SerializedName("currentStep")
    private CurrentStep currentStep;

    @SerializedName("steps")
    private ArrayList<MissionSteps> missionSteps;

    @SerializedName("paths")
    private ArrayList<FlightPaths> flightPaths;

    protected Mission(Parcel in) {
        mission = in.readParcelable(MMission.class.getClassLoader());
        project = in.readParcelable(Project.class.getClassLoader());
        _id = in.readString();
        missionSteps = new ArrayList<>();
        in.readTypedList(getMissionSteps(), MissionSteps.CREATOR);
    }

    public ArrayList<FlightPaths> getFlightPaths() {
        return flightPaths;
    }

    public void setFlightPaths(ArrayList<FlightPaths> flightPaths) {
        this.flightPaths = flightPaths;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public MMission getMission() {
        return mission;
    }

    public void setMission(MMission mission) {
        this.mission = mission;
    }

    public ArrayList<MissionSteps> getMissionSteps() {
        return missionSteps;
    }

    public void setMissionSteps(ArrayList<MissionSteps> missionSteps) {
        this.missionSteps = missionSteps;
    }

    public CurrentStep getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(CurrentStep currentStep) {
        this.currentStep = currentStep;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable(mission, flags);
        dest.writeParcelable(project, flags);
        dest.writeString(_id);
        dest.writeTypedList(missionSteps);
    }
}