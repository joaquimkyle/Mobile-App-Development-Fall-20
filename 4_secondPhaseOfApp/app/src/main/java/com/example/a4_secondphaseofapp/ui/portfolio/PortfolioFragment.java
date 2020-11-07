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
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(PortfolioViewModel.class);

        View root = inflater.inflate(R.layout.fragment_portfolio, container, false);

        final TextView companyOneName = root.findViewById(R.id.companyOneName);
        final TextView companyOneTicker = root.findViewById(R.id.companyOneTicker);
        final ImageView companyOneLogo = root.findViewById(R.id.companyOneLogo);
        final TextView companyOneShare = root.findViewById(R.id.companyOneShare);
        portfolioViewModel.companyOne.observe(getViewLifecycleOwner(), Observable -> {});
        portfolioViewModel.companyOne.portfolioData.observe(getViewLifecycleOwner(), pm -> {
            getCompany(portfolioViewModel.companyOne);
            companyOneName.setText(pm.getName());
            companyOneTicker.setText(pm.getTicker());
            companyOneShare.setText(pm.getShareOutstanding());
            Picasso.get().load(pm.getLogo()).into(companyOneLogo);
        });

        final TextView companyTwoName = root.findViewById(R.id.companyTwoName);
        final TextView companyTwoTicker = root.findViewById(R.id.companyTwoTicker);
        final ImageView companyTwoLogo = root.findViewById(R.id.companyTwoLogo);
        final TextView companyTwoShare = root.findViewById(R.id.companyTwoShare);
        portfolioViewModel.companyTwo.observe(getViewLifecycleOwner(), Observable -> {});
        portfolioViewModel.companyTwo.portfolioData.observe(getViewLifecycleOwner(), pm -> {
            getCompany(portfolioViewModel.companyTwo);
            companyTwoName.setText(pm.getName());
            companyTwoTicker.setText(pm.getTicker());
            companyTwoShare.setText(pm.getShareOutstanding());
            Picasso.get().load(pm.getLogo()).into(companyTwoLogo);
        });

        final TextView companyThreeName = root.findViewById(R.id.companyThreeName);
        final TextView companyThreeTicker = root.findViewById(R.id.companyThreeTicker);
        final ImageView companyThreeLogo = root.findViewById(R.id.companyThreeLogo);
        final TextView companyThreeShare = root.findViewById(R.id.companyThreeShare);
        portfolioViewModel.companyThree.observe(getViewLifecycleOwner(), Observable -> {});
        portfolioViewModel.companyThree.portfolioData.observe(getViewLifecycleOwner(), pm -> {
            getCompany(portfolioViewModel.companyThree);
            companyThreeName.setText(pm.getName());
            companyThreeTicker.setText(pm.getTicker());
            companyThreeShare.setText(pm.getShareOutstanding());
            Picasso.get().load(pm.getLogo()).into(companyThreeLogo);
        });

        final TextView companyFourName = root.findViewById(R.id.companyFourName);
        final TextView companyFourTicker = root.findViewById(R.id.companyFourTicker);
        final ImageView companyFourLogo = root.findViewById(R.id.companyFourLogo);
        final TextView companyFourShare = root.findViewById(R.id.companyFourShare);
        portfolioViewModel.companyFour.observe(getViewLifecycleOwner(), Observable -> {});
        portfolioViewModel.companyFour.portfolioData.observe(getViewLifecycleOwner(), pm -> {
            getCompany(portfolioViewModel.companyFour);
            companyFourName.setText(pm.getName());
            companyFourTicker.setText(pm.getTicker());
            companyFourShare.setText(pm.getShareOutstanding());
            Picasso.get().load(pm.getLogo()).into(companyFourLogo);
        });

        return root;
    }

    private void getCompany(final PortfolioLiveData portfolioLiveData) {
        String token = "bug81ef48v6qf6lcct50";
        String ticker = portfolioLiveData.portfolioData.getValue().getTicker();
        Call call = papi.getCompany(
                ticker, token
        );

        call.enqueue(new Callback<PortfolioModel>() {
            @Override
            public void onResponse(Call<PortfolioModel> call, Response<PortfolioModel> response) {

                if (response.isSuccessful()) {
                    PortfolioModel portfolioResponse = response.body();
                    Map<String, Object> data = new HashMap<>();
                    data.put("name", portfolioResponse.getName());
                    data.put("logo", portfolioResponse.getLogo());
                    data.put("share", portfolioResponse.getShareOutstanding());
                    portfolioLiveData.getDocumentReference().set(data, SetOptions.merge());
                } else {
                    return;
                }
            }

            @Override
            public void onFailure(Call<PortfolioModel> call, Throwable t) {
                Log.d("checkPortfolio", "Error!");
                t.printStackTrace();
            }
        });
    }
}