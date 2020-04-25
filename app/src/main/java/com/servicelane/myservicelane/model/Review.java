package com.servicelane.myservicelane.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;
import com.servicelane.myservicelane.Helper;

public class Review implements Parcelable {
    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }

        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }
    };

    private int id;
    private String name;
    private String date;
    private String description;

    public Review(JsonObject json) {
        id = Helper.Int(json, "ID");
        name = Helper.string(json, "ReviewerFName") + " " + Helper.string(json, "ReviewerLName");
        description = Helper.string(json, "ReviewText");
        date = Helper.string(json, "CreatedOn");
    }

    Review(Parcel parcel) {
        id = parcel.readInt();
        name = parcel.readString();
        date = parcel.readString();
        description = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(date);
        parcel.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
