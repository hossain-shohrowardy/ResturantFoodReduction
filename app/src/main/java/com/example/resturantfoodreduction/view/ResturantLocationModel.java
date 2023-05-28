package com.example.resturantfoodreduction.view;

public class ResturantLocationModel {
    Double longitude;
    Double latitude;

    public ResturantLocationModel() {
    }

    public ResturantLocationModel(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
