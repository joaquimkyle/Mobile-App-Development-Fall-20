package com.example.a4_secondphaseofapp.ui.portfolio;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PortfolioAPI {
    @GET("profile2")
    Call<PortfolioResponse> getCompany(
            @Query("symbol") String symbol,
            @Query("token") String token
    );
}
