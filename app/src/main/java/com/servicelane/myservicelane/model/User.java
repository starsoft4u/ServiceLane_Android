package com.servicelane.myservicelane.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.annimon.stream.Stream;
import com.google.gson.JsonObject;
import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.view.BaseActivity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }

        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }
    };

    private int id;
    private int type;
    private List<Common> categoryList;
    private int rate;
    private String email;
    private String firstName;
    private String lastName;
    private String planName;
    private String phone;
    private String whatsApp;
    private String facebook;
    private String webSite;
    private String address;
    private String city;
    private String parish;
    private String zip;
    private String photo;
    private String shortBio;
    private String createdOn;
    private int reviewCnt;
    private int serviceCnt;
    private int portfolioCnt;
    private boolean featured;
    private boolean activate;

    public User(JsonObject json) {
        id = Helper.Int(json, "UserID", 0);
        type = Helper.Int(json, "UserType", 0);
        email = Helper.string(json, "Email", "");
        firstName = Helper.string(json, "FName", "");
        lastName = Helper.string(json, "LName", "");
        planName = Helper.string(json, "PlanName", "");
        phone = Helper.string(json, "Phone", "");
        whatsApp = Helper.string(json, "WhatsApp", "");
        facebook = Helper.string(json, "Facebook", "");
        webSite = Helper.string(json, "Website", "");
        address = Helper.string(json, "Address", "");
        city = Helper.string(json, "City", "");
        parish = Helper.string(json, "Parish", "");
        zip = Helper.string(json, "Zip", "");
        String url = Helper.string(json, "ProfilePhoto", "");
        if (TextUtils.isEmpty(url)) {
            photo = "empty";
        } else {
            try {
                photo = BaseActivity.PROFILE_URL + "/" + URLEncoder.encode(url, "UTF-8");
            } catch (Exception e) {
                photo = BaseActivity.PROFILE_URL + "/" + url;
            }
        }
        shortBio = Helper.string(json, "ShortBio", "");
        rate = Helper.Int(json, "rate", 0);
        createdOn = Helper.string(json, "CreatedOn", "");
        reviewCnt = Helper.Int(json, "reviewCnt", Helper.Int(json, "rvCount"));
        serviceCnt = Helper.Int(json, "pvCount");
        portfolioCnt = Helper.Int(json, "prCount");
        featured = Helper.Int(json, "Featured") == 1;
        activate = Helper.Int(json, "Status", 1) == 1;
        categoryList = Stream.of(json.getAsJsonArray("Category")).map(item -> {
            JsonObject obj = item.getAsJsonObject();
            return new Common(obj.get("ID").getAsInt(), obj.get("Category").getAsString());
        }).toList();
    }

    public User(Parcel parcel) {
        id = parcel.readInt();
        type = parcel.readInt();
        email = parcel.readString();
        firstName = parcel.readString();
        lastName = parcel.readString();
        planName = parcel.readString();
        phone = parcel.readString();
        whatsApp = parcel.readString();
        facebook = parcel.readString();
        webSite = parcel.readString();
        address = parcel.readString();
        city = parcel.readString();
        parish = parcel.readString();
        zip = parcel.readString();
        photo = parcel.readString();
        shortBio = parcel.readString();
        rate = parcel.readInt();
        reviewCnt = parcel.readInt();
        portfolioCnt = parcel.readInt();
        serviceCnt = parcel.readInt();
        createdOn = parcel.readString();
        categoryList = new ArrayList<>();
        parcel.readTypedList(categoryList, Common.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(type);
        parcel.writeString(email);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(planName);
        parcel.writeString(phone);
        parcel.writeString(whatsApp);
        parcel.writeString(facebook);
        parcel.writeString(webSite);
        parcel.writeString(address);
        parcel.writeString(city);
        parcel.writeString(parish);
        parcel.writeString(zip);
        parcel.writeString(photo);
        parcel.writeString(shortBio);
        parcel.writeInt(rate);
        parcel.writeInt(reviewCnt);
        parcel.writeInt(portfolioCnt);
        parcel.writeInt(serviceCnt);
        parcel.writeString(createdOn);
        parcel.writeTypedList(categoryList);
    }

    public String getName() {
        return firstName + " " + lastName;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneCompact() {
        return "1" + phone
                .replace("(", "")
                .replace(")", "")
                .replace("+", "")
                .replace("-", "")
                .replace(" ", "");
    }

    public String getWhatsAppCompact() {
        return "1" + whatsApp
                .replace("(", "")
                .replace(")", "")
                .replace("+", "")
                .replace("-", "")
                .replace(" ", "");
    }

    public String getWhatsApp() {
        return whatsApp;
    }

    public void setWhatsApp(String whatsApp) {
        this.whatsApp = whatsApp;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getParish() {
        return parish;
    }

    public void setParish(String parish) {
        this.parish = parish;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getShortBio() {
        return shortBio;
    }

    public void setShortBio(String shortBio) {
        this.shortBio = shortBio;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public int getReviewCnt() {
        return reviewCnt;
    }

    public void setReviewCnt(int reviewCnt) {
        this.reviewCnt = reviewCnt;
    }

    public int getServiceCnt() {
        return serviceCnt;
    }

    public void setServiceCnt(int serviceCnt) {
        this.serviceCnt = serviceCnt;
    }

    public int getPortfolioCnt() {
        return portfolioCnt;
    }

    public void setPortfolioCnt(int portfolioCnt) {
        this.portfolioCnt = portfolioCnt;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }

    public String getCategory() {
        return TextUtils.join(", ", categoryList);
    }

    public List<Common> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Common> categoryList) {
        this.categoryList = categoryList;
    }
}
