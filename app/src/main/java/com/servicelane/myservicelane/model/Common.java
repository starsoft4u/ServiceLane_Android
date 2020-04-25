package com.servicelane.myservicelane.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Common implements Parcelable {
    public static final Creator<Common> CREATOR = new Creator<Common>() {
        @Override
        public Common[] newArray(int size) {
            return new Common[size];
        }

        @Override
        public Common createFromParcel(Parcel source) {
            return new Common(source);
        }
    };

    private int id;
    private String name;

    public Common(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Common(Parcel parcel) {
        setId(parcel.readInt());
        setName(parcel.readString());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeString(getName());
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

    public String getIdStr() {
        return String.valueOf(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
