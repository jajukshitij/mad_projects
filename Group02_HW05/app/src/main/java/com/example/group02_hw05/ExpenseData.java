package com.example.group02_hw05;

import java.io.Serializable;

public class ExpenseData implements Serializable {
    String name, cost, date, receipt, firebaseKey;

    @Override
    public String toString() {
        return "ExpenseData{" +
                "name='" + name + '\'' +
                ", cost='" + cost + '\'' +
                ", date='" + date + '\'' +
                ", receipt='" + receipt + '\'' +
                '}';
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }
}
