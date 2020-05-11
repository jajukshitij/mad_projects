package com.example.inclass12;

import java.io.Serializable;
import java.util.ArrayList;

public class CityData implements Serializable {

    float latitude,longitude;
    String address, tripName, firebaseKey, keyword, date;
    ArrayList<PlacesData> placesDataArrayList;

    @Override
    public String toString() {
        return "CityData{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", tripName='" + tripName + '\'' +
                ", firebaseKey='" + firebaseKey + '\'' +
                ", keyword='" + keyword + '\'' +
                ", placesDataArrayList=" + placesDataArrayList +
                '}';
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }
}
