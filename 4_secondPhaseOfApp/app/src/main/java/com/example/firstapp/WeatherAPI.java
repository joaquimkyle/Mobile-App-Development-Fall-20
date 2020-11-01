package com.example.firstapp;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WeatherAPI {

    @GET("lat={lat}&lon={lon}&appid={API key}")
    Call<WeatherResponse> getWeather(@Path("lat") double lat, @Path("lon") double lon);
}
