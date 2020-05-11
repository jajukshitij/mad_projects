package com.example.kshitijjaju.inclass08;

import android.os.Parcel;
import android.os.Parcelable;

public class ExpenseData implements Parcelable {
    String name, category, amount, date,firbaseKey;

    @Override
    public String toString() {
        return "ExpenseData{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", amount='" + amount + '\'' +
                ", date='" + date + '\'' +
                ", firebaseKey=' "+ firbaseKey + '\'' +
                '}';
    }

    public String getFirbaseKey() {
        return firbaseKey;
    }

    public void setFirbaseKey(String firbaseKey) {
        this.firbaseKey = firbaseKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.category);
        dest.writeString(this.amount);
        dest.writeString(this.date);
        /*dest.writeStringArray(new String[] {this.name,
                this.category,
                this.amount,
                this.date});*/

    }
    public ExpenseData(){

    }

    public ExpenseData(Parcel in){
       /* String[] data = new String[4];

        in.readStringArray(data);*/
       in.readInt();
        // the order needs to be the same as in writeToParcel() method
        this.name = in.readString();
        this.category = in.readString();
        this.amount = in.readString();
        this.date = in.readString();
    }

    public static final Creator CREATOR = new Creator() {
        public ExpenseData createFromParcel(Parcel in) {
            return new ExpenseData(in);
        }

        public ExpenseData[] newArray(int size) {
            return new ExpenseData[size];
        }
    };
}
