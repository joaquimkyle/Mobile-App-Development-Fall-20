package com.example.a4_secondphaseofapp.ui.portfolio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PortfolioViewModel extends ViewModel {

    static class Company {
        private MutableLiveData<String> mName;
        private MutableLiveData<String> mTicker;
        private MutableLiveData<String> mLogo;
        private MutableLiveData<String> mShareOutstanding;

        public LiveData<String> getName() {
            return mName;
        }

        public void setName(String name) {
            mName.setValue(name);
        }

        public LiveData<String> getTicker() {
            return mTicker;
        }

        public void setTicker(String ticker) {
            mTicker.setValue(ticker);
        }

        public LiveData<String> getLogo() {
            return mLogo;
        }

        public void setLogo(String logo) {
            mLogo.setValue(logo);
        }

        public LiveData<String> getShareOut() {
            return mShareOutstanding;
        }

        public void setShareOutstanding(String share) {
            mShareOutstanding.setValue(share);
        }

    }

    public Company companyOne;

    public PortfolioViewModel() {
        companyOne = new Company();
        companyOne.mName = new MutableLiveData<String>("");
        companyOne.mTicker = new MutableLiveData<String>("AAPL");
        companyOne.mLogo = new MutableLiveData<String>("");
        companyOne.mShareOutstanding = new MutableLiveData<String>("");
    }

}