package com.example.a4_secondphaseofapp.ui.weather;

import android.util.Log;

import java.util.List;
import java.util.Locale;

public class WeatherResponse {
    private final static String ICON_ADDR = "https://openweathermap.org/img/w/";

    static class Weather {
        String description;
        String icon;
    }

    static class Main {
        double temp;
    }

    List<Weather> weather;
    Main main;
    String name;

    String getTemperature() {
        return String.format(Locale.getDefault(), "%.2f", main.temp);
    }
    public String getIconAddress() {
        return ICON_ADDR + weather.get(0).icon + ".png";
    }

    public String getDescription() {
        if (weather != null && weather.size() > 0)
            return weather.get(0).description;
        return null;
    }
}