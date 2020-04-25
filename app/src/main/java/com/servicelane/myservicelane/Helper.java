package com.servicelane.myservicelane;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.servicelane.myservicelane.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helper {
    private static SharedPreferences preferences;

    /**********           Common          ************/
    public static String[] stringArray(Context context, int res) {
        return context.getResources().getStringArray(res);
    }

    public static String string(JsonObject json, String key) {
        return string(json, key, null);
    }

    public static String string(JsonObject json, String key, String defaultValue) {
        return !json.has(key) || json.get(key).isJsonNull() ? defaultValue : json.get(key).getAsString();
    }

    public static String string(Context context, int res, Object... args) {
        return context.getResources().getString(res, args);
    }

    public static boolean bool(JsonObject json, String key) {
        return bool(json, key, false);
    }

    public static boolean bool(JsonObject json, String key, boolean defaultValue) {
        return !json.has(key) || json.get(key).isJsonNull() || json.get(key).getAsString().isEmpty() ? defaultValue : json.get(key).getAsBoolean();
    }

    public static double Double(JsonObject json, String key) {
        return Double(json, key, 0);
    }

    public static double Double(JsonObject json, String key, double defaultValue) {
        return !json.has(key) || json.get(key).isJsonNull() || json.get(key).getAsString().isEmpty() ? defaultValue : json.get(key).getAsDouble();
    }

    public static int Int(JsonObject json, String key) {
        return Int(json, key, 0);
    }

    public static int Int(JsonObject json, String key, int defaultValue) {
        return !json.has(key) || json.get(key).isJsonNull() || json.get(key).getAsString().isEmpty() ? defaultValue : json.get(key).getAsInt();
    }

    public static long Long(JsonObject json, String key) {
        return Long(json, key, 0);
    }

    public static long Long(JsonObject json, String key, long defaultValue) {
        return !json.has(key) || json.get(key).isJsonNull() || json.get(key).getAsString().isEmpty() ? defaultValue : json.get(key).getAsLong();
    }

    public static String date(String from, String to) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            Date d = sd.parse(from);
            SimpleDateFormat sd1 = new SimpleDateFormat(to, Locale.getDefault());
            return sd1.format(d);
        } catch (Exception e) {
            return from;
        }
    }

    public static int pixel(Context context, int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
    }

    public static int dip(Context context, int pixel) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(pixel / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /************** Storage ************************/

    public static void initialize(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences("ServiceLaneStoreKey", 0);
    }

    public enum StoreKey {
        ME, LAUNCH_COUNT;

        public static void clearAll() {
            preferences.edit().clear().apply();
        }

        public int getInt(int defaultVal) {
            return preferences.getInt(this.toString(), defaultVal);
        }

        public void setInt(int val) {
            preferences.edit().putInt(this.toString(), val).apply();
        }

        public boolean getBool(boolean defaultValue) {
            return preferences.getBoolean(this.toString(), defaultValue);
        }

        public void setBool(boolean val) {
            preferences.edit().putBoolean(this.toString(), val).apply();
        }

        public User getUser() {
            String val = preferences.getString(this.toString(), null);
            return val == null ? null : new Gson().fromJson(val, User.class);
        }

        public void setUser(User val) {
            preferences.edit().putString(this.toString(), val == null ? null : new Gson().toJson(val)).apply();
        }

        public void clear() {
            preferences.edit().remove(this.toString()).apply();
        }
    }
}
