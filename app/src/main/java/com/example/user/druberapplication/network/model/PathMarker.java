package com.example.user.druberapplication.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PathMarker {

    @SerializedName("features")
    private List<Features> features;
    @SerializedName("type")
    private String type;

    public List<Features> getFeatures() {
        return features;
    }

    public void setFeatures(List<Features> features) {
        this.features = features;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class Features {
        @SerializedName("geometry")
        private Geometry geometry;
        @SerializedName("properties")
        private Properties properties;
        @SerializedName("type")
        private String type;

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        public Properties getProperties() {
            return properties;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class Geometry {
        @SerializedName("hgrid")
        private Hgrid hgrid;
        @SerializedName("vgrid")
        private Vgrid vgrid;

        public Hgrid getHgrid() {
            return hgrid;
        }

        public void setHgrid(Hgrid hgrid) {
            this.hgrid = hgrid;
        }

        public Vgrid getVgrid() {
            return vgrid;
        }

        public void setVgrid(Vgrid vgrid) {
            this.vgrid = vgrid;
        }
    }

    public static class Hgrid {
        @SerializedName("coordinates")
        private List<List<Double>> coordinates;
        @SerializedName("type")
        private String type;

        public List<List<Double>> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<List<Double>> coordinates) {
            this.coordinates = coordinates;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class Vgrid {
        @SerializedName("coordinates")
        private List<List<Double>> coordinates;
        @SerializedName("type")
        private String type;

        public List<List<Double>> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<List<Double>> coordinates) {
            this.coordinates = coordinates;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class Properties {
    }
}
