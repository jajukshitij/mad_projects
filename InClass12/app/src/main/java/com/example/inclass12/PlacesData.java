package com.example.inclass12;

import java.io.Serializable;

public class PlacesData implements Serializable {
    float latitude, longitude;
    String placetitle;

    @Override
    public String toString() {
        return "PlacesData{" +
                "latitude=" + latitude +
                ", longitiude=" + longitude +
                ", placetitle='" + placetitle + '\'' +
                '}';
    }
}
