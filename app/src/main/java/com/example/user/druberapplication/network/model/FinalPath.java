package com.example.user.druberapplication.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mapbox.geojson.BoundingBox;

public class FinalPath implements Parcelable {

    public static final Parcelable.Creator<FinalPath> CREATOR = new Parcelable.Creator<FinalPath>() {
        @Override
        public FinalPath createFromParcel(Parcel source) {
            return new FinalPath(source);
        }

        @Override
        public FinalPath[] newArray(int size) {
            return new FinalPath[size];
        }
    };
    @SerializedName("__v")
    private String V;
    @SerializedName("speed")
    private String speed;
    @SerializedName("altitude")
    private String altitude;
    @SerializedName("frontoverlap")
    private String frontoverlap;
    @SerializedName("sideoverlap")
    private String sideoverlap;
    @SerializedName("gridtype")
    private String gridtype;
    @SerializedName("geometry")
    private Geometry geometry;
    @SerializedName("_id")
    private String Id;

    public FinalPath() {
    }

    protected FinalPath(Parcel in) {
        this.V = in.readString();
        this.speed = in.readString();
        this.altitude = in.readString();
        this.frontoverlap = in.readString();
        this.sideoverlap = in.readString();
        this.gridtype = in.readString();
        this.geometry = in.readParcelable(Geometry.class.getClassLoader());
        this.Id = in.readString();
    }

    public String getV() {
        return V;
    }

    public void setV(String V) {
        this.V = V;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getFrontoverlap() {
        return frontoverlap;
    }

    public void setFrontoverlap(String frontoverlap) {
        this.frontoverlap = frontoverlap;
    }

    public String getSideoverlap() {
        return sideoverlap;
    }

    public void setSideoverlap(String sideoverlap) {
        this.sideoverlap = sideoverlap;
    }

    public String getGridtype() {
        return gridtype;
    }

    public void setGridtype(String gridtype) {
        this.gridtype = gridtype;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.V);
        dest.writeString(this.speed);
        dest.writeString(this.altitude);
        dest.writeString(this.frontoverlap);
        dest.writeString(this.sideoverlap);
        dest.writeString(this.gridtype);
        dest.writeParcelable(this.geometry, flags);
        dest.writeString(this.Id);
    }

    public static class Geometry implements Parcelable, com.mapbox.geojson.Geometry {
        public static final Creator<Geometry> CREATOR = new Creator<Geometry>() {
            @Override
            public Geometry createFromParcel(Parcel in) {
                return new Geometry(in);
            }

            @Override
            public Geometry[] newArray(int size) {
                return new Geometry[size];
            }
        };
        @SerializedName("coordinates")
        private Object coordinates;
        @SerializedName("type")
        private String type;

        protected Geometry(Parcel in) {
            type = in.readString();
        }

        public Object getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(Object coordinates) {
            this.coordinates = coordinates;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(type);
        }

        @Override
        public String type() {
            return null;
        }

        @Override
        public String toJson() {
            return null;
        }

        @Override
        public BoundingBox bbox() {
            return null;
        }
    }
}
