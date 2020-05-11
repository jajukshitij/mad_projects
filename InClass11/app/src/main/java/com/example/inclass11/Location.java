package com.example.inclass11;

import java.util.ArrayList;

public class Location {
    ArrayList<LatLongPoint> points;
    String title;

    public Location(ArrayList<LatLongPoint> locationList, String title) {
        this.points = locationList;
        this.title = title;
    }

    public ArrayList<LatLongPoint> getLocationList() {
        return points;
    }

    public void setLocationList(ArrayList<LatLongPoint> locationList) {
        this.points = locationList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
