package com.example.a4_secondphaseofapp.ui.weather;

import android.net.Uri;

public class WeatherModel {
    private String mLon;
    private String mLat;
    private String mTemp;
    private String mIconPath;
    private String mName;

    public WeatherModel() {
        mLon = "0";
        mLat = "0";
        mTemp = "0";
        mIconPath = "0";
        mName = "0";
    }

    public String getLon() {
        return mLon;
    }

    public Double getLonValue() {
        return Double.parseDouble(mLon);
    }

    public void setLon(double lon) {
        mLon = String.valueOf(lon);
    }

    public String getLat() {
        return mLat;
    }

    public Double getLatValue() {
        return Double.parseDouble(mLat);
    }

    public void setLat(double lat) {
        mLat = String.valueOf(lat);
    }

    public String getTemp() {
        return mTemp;
    }

    public void setTemp(String temp) {
        temp = String.valueOf((int)Double.parseDouble(temp));
        mTemp = (temp + "°");
    }

    public String getIcon() {
        return mIconPath;
    }

    public Uri getIconPath() {
        return Uri.parse(mIconPath);
    }

    public void setIcon(String icon) {
        mIconPath = icon;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
