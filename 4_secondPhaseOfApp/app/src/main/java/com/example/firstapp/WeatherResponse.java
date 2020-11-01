package com.example.firstapp;

import java.util.List;

public class WeatherResponse {
    private final static String ICON_ADDR = "http://openweathermap.org/img/w/";

    static class Weather {
        String description;
        String icon;
    }

    static class Main {
        float temp;
    }

    List<Weather> weather;
    Main main;
    String name;

    String getTemperatureInFahrenheit() {
        float temp = ((main.temp - 273.15f) * (9/5)) + 32;
        return String.format("%.2f", temp);
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