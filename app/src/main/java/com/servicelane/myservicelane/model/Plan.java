package com.servicelane.myservicelane.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;
import com.servicelane.myservicelane.Helper;

public class Plan implements Parcelable {
    public static final Creator<Plan> CREATOR = new Creator<Plan>() {
        @Override
        public Plan[] newArray(int size) {
            return new Plan[size];
        }

        @Override
        public Plan createFromParcel(Parcel source) {
            return new Plan(source);
        }
    };

    private int id;
    private String product;
    private double price;
    private String startOn;
    private String expireOn;

    public Plan() {
        id = 0;
        product = "Select plan";
        price = 0;
        startOn = "";
        expireOn = "";
    }

    public Plan(JsonObject json) {
        id = Helper.Int(json, "ID");
        product = Helper.string(json, "Product");
        price = Helper.Double(json, "Price");
        startOn = Helper.string(json, "StartDate");
        expireOn = Helper.string(json, "EndDate");
    }

    public Plan(Parcel parcel) {
        id = parcel.readInt();
        product = parcel.readString();
        price = parcel.readDouble();
        startOn = parcel.readString();
        expireOn = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(product);
        parcel.writeDouble(price);
        parcel.writeString(startOn);
        parcel.writeString(expireOn);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return product;
    }

    public String title() {
        String unit = "";
        if (product.toLowerCase().contains("day")) {
            unit = "/day";
        } else if (product.toLowerCase().contains("week")) {
            unit = "/wk";
        } else if (product.toLowerCase().contains("month")) {
            unit = "/mo";
        } else if (product.toLowerCase().contains("year")) {
            unit = "/yr";
        }
        return product + " ($" + price + unit + ")";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdString() {
        return String.valueOf(id);
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStartOn() {
        return startOn;
    }

    public void setStartOn(String startOn) {
        this.startOn = startOn;
    }

    public String getExpireOn() {
        return expireOn;
    }

    public void setExpireOn(String expireOn) {
        this.expireOn = expireOn;
    }
}
