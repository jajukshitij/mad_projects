package com.example.kshitijjaju.group02_hw03;
/*Homework 03
 * MusicData
 * Kshitij Jaju
 * Smruthi Rajagopal*/

import java.io.Serializable;
import java.util.Date;

public class MusicData implements Serializable {
    String artistName,albumName,trackName,albumPrice,genre,artworkUrl;
    Double trackPrice;
    Date date;

    @Override
    public String toString() {
        return "MusicData{" +
                "artistName='" + artistName + '\'' +
                ", albumName='" + albumName + '\'' +
                ", trackName='" + trackName + '\'' +
                ", trackPrice='" + trackPrice + '\'' +
                ", albumPrice='" + albumPrice + '\'' +
                ", date='" + date + '\'' +
                ", genre='" + genre + '\'' +
                ", artworkUrl='" + artworkUrl + '\'' +
                '}';
    }
}
