package com.example.user.druberapplication.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FlightPaths implements Parcelable {

    public static final Creator<FlightPaths> CREATOR = new Creator<FlightPaths>() {
        @Override
        public FlightPaths createFromParcel(Parcel source) {
            return new FlightPaths(source);
        }

        @Override
        public FlightPaths[] newArray(int size) {
            return new FlightPaths[size];
        }
    };
    @SerializedName("path")
    private String path;
    @SerializedName("_id")
    private String _id;
    @SerializedName("start_time")
    private String start_time;
    @SerializedName("end_time")
    private String end_time;
    @SerializedName("latlng")
    private String latlng;

    public FlightPaths() {
    }

    protected FlightPaths(Parcel in) {
        this.path = in.readString();
        this._id = in.readString();
        this.start_time = in.readString();
        this.end_time = in.readString();
        this.latlng = in.readString();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this._id);
        dest.writeString(this.start_time);
        dest.writeString(this.end_time);
        dest.writeString(this.latlng);
    }
}
