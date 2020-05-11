package com.example.kshitijjaju.a801075892_midterm;

import java.io.Serializable;
import java.util.Date;

public class MusicData implements Serializable {
    String trackName, albumName, artistName, trackShareUrl;
    Date updatedTime;

    @Override
    public String toString() {
        return "MusicData{" +
                "trackName='" + trackName + '\'' +
                ", albumName='" + albumName + '\'' +
                ", artistName='" + artistName + '\'' +
                ", updatedTime='" + updatedTime + '\'' +
                ", trackShareUrl='" + trackShareUrl + '\'' +
                '}';
    }
}
