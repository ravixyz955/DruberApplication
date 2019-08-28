package com.example.user.druberapplication.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MMission implements Parcelable {

    public static final Creator<MMission> CREATOR = new Creator<MMission>() {
        @Override
        public MMission createFromParcel(Parcel in) {
            return new MMission(in);
        }

        @Override
        public MMission[] newArray(int size) {
            return new MMission[size];
        }
    };
    @SerializedName("name")
    private String name;
    @SerializedName("_id")
    private String _id;

    protected MMission(Parcel in) {
        name = in.readString();
        _id = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        dest.writeString(name);
        dest.writeString(_id);
    }
}
