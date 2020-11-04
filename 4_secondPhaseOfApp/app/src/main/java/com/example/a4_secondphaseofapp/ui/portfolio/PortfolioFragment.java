package com.example.a4_secondphaseofapp.ui.portfolio;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.a4_secondphaseofapp.R;
import com.example.a4_secondphaseofapp.ui.weather.WeatherAPI;
import com.example.a4_secondphaseofapp.ui.weather.WeatherResponse;
import com.example.a4_secondphaseofapp.ui.weather.WeatherViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PortfolioFragment extends Fragment {

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://finnhub.io/api/v1/stock/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    PortfolioAPI papi = retrofit.create(PortfolioAPI.class);
    private PortfolioViewModel portfolioViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        portfolioViewModel =
                new ViewModelProvider(this).get(PortfolioViewModel.class);
        View root = inflater.inflate(R.layout.fragment_portfolio, container, false);

        final TextView companyOneName = root.findViewById(R.id.companyOneName);
        portfolioViewModel.companyOne.getName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                companyOneName.setText(s);
            }
        });
        final TextView companyOneTicker = root.findViewById(R.id.companyOneTicker);
        portfolioViewModel.companyOne.getTicker().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                companyOneTicker.setText(s);
            }
        });
        final ImageView companyOneLogo = root.findViewById(R.id.companyOneLogo);
        portfolioViewModel.companyOne.getLogo().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (!portfolioViewModel.companyOne.getLogo().getValue().isEmpty()) {
                    Picasso.get().load(portfolioViewModel.companyOne.getLogo().getValue()).into(companyOneLogo);
                }
            }
        });
        final TextView companyOneShare = root.findViewById(R.id.companyOneShare);
        portfolioViewModel.companyOne.getShareOut().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                companyOneShare.setText(s);
            }
        });

        getCompany(portfolioViewModel);

        return root;
    }

    private void getCompany(final PortfolioViewModel portfolioViewModel) {
        String token = "bug81ef48v6qf6lcct50";
        String ticker = portfolioViewModel.companyOne.getTicker().getValue();
        Call call = papi.getCompany(
                ticker, token
        );

        call.enqueue(new Callback<PortfolioResponse>() {
            @Override
            public void onResponse(Call<PortfolioResponse> call, Response<PortfolioResponse> response) {

                if (response.isSuccessful()) {
                    PortfolioResponse portfolioResponse = response.body();
                    portfolioViewModel.companyOne.setName(portfolioResponse.name);
                    portfolioViewModel.companyOne.setLogo(portfolioResponse.logo);
                    portfolioViewModel.companyOne.setShareOutstanding(String.valueOf(portfolioResponse.shareOutstanding));
                } else {
                    return;
                }
            }

            @Override
            public void onFailure(Call<PortfolioResponse> call, Throwable t) {
                Log.d("checkWeather", "Error!");
                t.printStackTrace();
            }
        });
    }
}