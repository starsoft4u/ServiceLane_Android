package com.servicelane.myservicelane.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;
import com.servicelane.myservicelane.Helper;

public class ServicePackage implements Parcelable {
    public static final Creator<ServicePackage> CREATOR = new Creator<ServicePackage>() {
        @Override
        public ServicePackage[] newArray(int size) {
            return new ServicePackage[size];
        }

        @Override
        public ServicePackage createFromParcel(Parcel source) {
            return new ServicePackage(source);
        }
    };

    private int id;
    private String name;
    private String description;
    private String rate;
    private Duration duration;

    public ServicePackage(JsonObject json) {
        id = Helper.Int(json, "ID");
        name = Helper.string(json, "ServiceName");
        description = Helper.string(json, "ServiceDescription");
        rate = Helper.string(json, "Rate");
        duration = Duration.fromUnit(Helper.string(json, "Per"));
    }

    private ServicePackage(Parcel parcel) {
        name = parcel.readString();
        description = parcel.readString();
        rate = parcel.readString();
        duration = Duration.fromUnit(parcel.readString());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(rate);
        parcel.writeString(duration.unit());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public enum Duration {
        NONE, HOURLY, DAILY, WEEKLY, MONTHLY, YEARLY, FIXED_RATE ;

        public static Duration fromUnit(String unit) {
            if (HOURLY.unit().equals(unit)) return HOURLY;
            else if (DAILY.unit().equals(unit)) return DAILY;
            else if (WEEKLY.unit().equals(unit)) return WEEKLY;
            else if (MONTHLY.unit().equals(unit)) return MONTHLY;
            else if (YEARLY.unit().equals(unit)) return YEARLY;
            else if (FIXED_RATE.unit().equals(unit)) return FIXED_RATE;
            else return NONE;
        }

        public static Duration fromTitle(String title) {
            if (HOURLY.title().equals(title)) return HOURLY;
            else if (DAILY.title().equals(title)) return DAILY;
            else if (WEEKLY.title().equals(title)) return WEEKLY;
            else if (MONTHLY.title().equals(title)) return MONTHLY;
            else if (YEARLY.title().equals(title)) return YEARLY;
            else if (FIXED_RATE.title().equals(title)) return FIXED_RATE;
            else return NONE;
        }

        public String title() {
            switch (this) {
                case HOURLY:
                    return "Hour";
                case DAILY:
                    return "Day";
                case WEEKLY:
                    return "Week";
                case MONTHLY:
                    return "Month";
                case YEARLY:
                    return "Year";
                case FIXED_RATE:
                    return "Fixed rate";
                default:
                    return "Duration";
            }
        }

        public String unit() {
            switch (this) {
                case HOURLY:
                    return "hr";
                case DAILY:
                    return "day";
                case WEEKLY:
                    return "wk";
                case MONTHLY:
                    return "mo";
                case YEARLY:
                    return "yr";
                case FIXED_RATE:
                    return "fixed";
                default:
                    return "";
            }
        }
    }
}
