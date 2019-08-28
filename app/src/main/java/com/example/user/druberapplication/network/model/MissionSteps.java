package com.example.user.druberapplication.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MissionSteps implements Parcelable {

    public static final Creator<MissionSteps> CREATOR = new Creator<MissionSteps>() {
        @Override
        public MissionSteps createFromParcel(Parcel in) {
            return new MissionSteps(in);
        }

        @Override
        public MissionSteps[] newArray(int size) {
            return new MissionSteps[size];
        }
    };

    @SerializedName("name")
    private String name;

    @SerializedName("status")
    private String status;

    @SerializedName("assignee")
    private String assignee;

    @SerializedName("date")
    private String date;

    @SerializedName("start_time")
    private String start_time;

    @SerializedName("end_time")
    private String end_time;

    protected MissionSteps(Parcel in) {
        name = in.readString();
        status = in.readString();
        date = in.readString();
        start_time = in.readString();
        end_time = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(status);
        dest.writeString(date);
        dest.writeString(start_time);
        dest.writeString(end_time);
    }
}