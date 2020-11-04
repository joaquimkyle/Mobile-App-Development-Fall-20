package com.example.a4_secondphaseofapp.ui.weather;

import androidx.lifecycle.ViewModel;

public class WeatherViewModel extends ViewModel {

    public WeatherRepository weatherRepository = new WeatherRepository();
    public WeatherLiveData wldCurrentLocation, wldWhereFrom, wldLikeToBe, wldFamilyFrom = null;

    public WeatherViewModel() {
        wldCurrentLocation = weatherRepository.getFireStoreLiveData("currentLocation");
        wldWhereFrom = weatherRepository.getFireStoreLiveData("whereFrom");
        wldLikeToBe = weatherRepository.getFireStoreLiveData("likeToBe");
        wldFamilyFrom = weatherRepository.getFireStoreLiveData("familyFrom");
    }
}