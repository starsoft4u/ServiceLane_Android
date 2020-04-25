package com.servicelane.myservicelane.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.view.BaseActivity;

import java.net.URLEncoder;

public class Portfolio implements Parcelable {
    public static final Creator<Portfolio> CREATOR = new Creator<Portfolio>() {
        @Override
        public Portfolio[] newArray(int size) {
            return new Portfolio[size];
        }

        @Override
        public Portfolio createFromParcel(Parcel source) {
            return new Portfolio(source);
        }
    };

    private int id;
    private String image;
    private String uploadedAt;

    public Portfolio(JsonObject json) {
        id = Helper.Int(json, "ID");
        String url = Helper.string(json, "Filename", "");
        if (TextUtils.isEmpty(url)) {
            image = "";
        } else {
            try {
                image = BaseActivity.PORTFOLIO_URL + "/" + URLEncoder.encode(url, "UTF-8");
            } catch (Exception e) {
                image = BaseActivity.PORTFOLIO_URL + "/" + url;
            }
        }
        uploadedAt = Helper.string(json, "CreatedOn");
    }

    Portfolio(Parcel parcel) {
        id = parcel.readInt();
        image = parcel.readString();
        uploadedAt = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(image);
        parcel.writeString(uploadedAt);
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(String uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
