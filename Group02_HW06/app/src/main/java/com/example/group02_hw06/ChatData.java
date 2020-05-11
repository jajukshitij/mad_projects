package com.example.group02_hw06;

import java.util.Date;

public class ChatData {

    String chatMessage, imageUrl, senderName, firebaseKey, userId;
    Date chatTime;

    @Override
    public String toString() {
        return "ChatData{" +
                "chatMessage='" + chatMessage + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", senderName='" + senderName + '\'' +
                ", firebaseKey='" + firebaseKey + '\'' +
                ", userId='" + userId + '\'' +
                ", chatTime=" + chatTime +
                '}';
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }
}
