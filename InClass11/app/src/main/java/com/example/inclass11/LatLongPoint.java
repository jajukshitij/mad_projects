package com.example.inclass11;

public class LatLongPoint {
    float latitude;
    float longitude;


    public LatLongPoint(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "LatLongPoint{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
