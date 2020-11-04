package com.example.a4_secondphaseofapp.ui.weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.a4_secondphaseofapp.R;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherFragment extends Fragment {

    LocationManager locationManager;

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    WeatherAPI wapi = retrofit.create(WeatherAPI.class);
    private WeatherViewModel weatherViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        weatherViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(WeatherViewModel.class);

        View root = inflater.inflate(R.layout.fragment_weather, container, false);

        //check location permissions and request location updates
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 15);
        } else {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 2 * 60 * 1000, 10, locationListenerGPS);
            locationListenerGPS.onLocationChanged(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }

        //current location data
        final ImageView iconCurrentLocation = root.findViewById(R.id.iconCurrentLocation);
        final TextView tempCurrentLocation = root.findViewById(R.id.tempCurrentLocation);
        final TextView textCurrentLocation = root.findViewById(R.id.textCurrentLocation);
        weatherViewModel.wldCurrentLocation.observe(getViewLifecycleOwner(), Observable -> {});
        weatherViewModel.wldCurrentLocation.weatherData.observe(getViewLifecycleOwner(), wm -> {
            checkWeather(weatherViewModel.wldCurrentLocation);
            Picasso.get().load(wm.getIconPath()).into(iconCurrentLocation);
            tempCurrentLocation.setText(wm.getTemp());
            textCurrentLocation.setText(wm.getName());

        });

        //where i'm from location data
        final ImageView iconWhereFrom = root.findViewById(R.id.iconWhereFrom);
        final TextView tempWhereFrom = root.findViewById(R.id.tempWhereFrom);
        final TextView textWhereFrom = root.findViewById(R.id.textWhereFrom);
        weatherViewModel.wldWhereFrom.observe(getViewLifecycleOwner(), Observable -> {});
        weatherViewModel.wldWhereFrom.weatherData.observe(getViewLifecycleOwner(), wm -> {
            checkWeather(weatherViewModel.wldWhereFrom);
            Picasso.get().load(wm.getIconPath()).into(iconWhereFrom);
            tempWhereFrom.setText(wm.getTemp());
            textWhereFrom.setText(wm.getName());

        });


        //where i'd like to be location data
        final ImageView iconLikeToBe = root.findViewById(R.id.iconLikeToBe);
        final TextView tempLikeToBe = root.findViewById(R.id.tempLikeToBe);
        final TextView textLikeToBe = root.findViewById(R.id.textLikeToBe);
        weatherViewModel.wldLikeToBe.observe(getViewLifecycleOwner(), Observable -> {});
        weatherViewModel.wldLikeToBe.weatherData.observe(getViewLifecycleOwner(), wm -> {
            checkWeather(weatherViewModel.wldLikeToBe);
            Picasso.get().load(wm.getIconPath()).into(iconLikeToBe);
            tempLikeToBe.setText(wm.getTemp());
            textLikeToBe.setText(wm.getName());
        });


        //where family from location data
        final ImageView iconFamilyFrom = root.findViewById(R.id.iconFamilyFrom);
        final TextView tempFamilyFrom = root.findViewById(R.id.tempFamilyFrom);
        final TextView textFamilyFrom = root.findViewById(R.id.textFamilyFrom);
        weatherViewModel.wldFamilyFrom.observe(getViewLifecycleOwner(), Observable -> {});
        weatherViewModel.wldFamilyFrom.weatherData.observe(getViewLifecycleOwner(), wm -> {
            checkWeather(weatherViewModel.wldFamilyFrom);
            Picasso.get().load(wm.getIconPath()).into(iconFamilyFrom);
            tempFamilyFrom.setText(wm.getTemp());
            textFamilyFrom.setText(wm.getName());
        });

        return root;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 15: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getActivity(), "permission granted", Toast.LENGTH_LONG).show();
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, 2 * 60 * 1000, 10, locationListenerGPS);
                    locationListenerGPS.onLocationChanged(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                } else {
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    private void checkWeather(final WeatherLiveData weatherLiveData) {
        String appid = "ec966d69af562febfc5659b20528dbd7";
        String units = "imperial";
        Call call = wapi.getWeather(
                weatherLiveData.weatherData.getValue().getLonValue(),
                weatherLiveData.weatherData.getValue().getLatValue(),
                units,
                appid
        );
        Log.d("weather", call.request().toString());

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {

                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    Map<String, Object> data = new HashMap<>();
                    data.put("temp", weatherResponse.getTemperature());
                    data.put("icon", weatherResponse.getIconAddress());
                    data.put("name", weatherResponse.name);
                    weatherLiveData.getDocumentReference().set(data, SetOptions.merge());
                } else {
                    return;
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.d("checkWeather", "Error!");
                t.printStackTrace();
            }
        });
    }

    private final LocationListener locationListenerGPS = new LocationListener() {
        public void onLocationChanged(Location location) {
            Map<String, Object> data = new HashMap<>();
            data.put("lon", String.valueOf(location.getLongitude()));
            data.put("lat", String.valueOf(location.getLatitude()));
            weatherViewModel.weatherRepository.getDocument("currentLocation").set(data, SetOptions.merge());
            checkWeather(weatherViewModel.wldCurrentLocation);

            if(getActivity() == null) {
                return;
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "GPS Provider update", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };
}

