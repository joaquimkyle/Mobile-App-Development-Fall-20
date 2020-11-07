package com.example.a4_secondphaseofapp.ui.portfolio;

import androidx.lifecycle.ViewModel;

public class PortfolioViewModel extends ViewModel {

    public PortfolioRepository portfolioRepository = new PortfolioRepository();
    public PortfolioLiveData companyOne, companyTwo, companyThree, companyFour = null;

    public PortfolioViewModel() {
        companyOne = portfolioRepository.getFireStoreLiveData("companyOne");
        companyTwo = portfolioRepository.getFireStoreLiveData("companyTwo");
        companyThree = portfolioRepository.getFireStoreLiveData("companyThree");
        companyFour = portfolioRepository.getFireStoreLiveData("companyFour");
    }

}