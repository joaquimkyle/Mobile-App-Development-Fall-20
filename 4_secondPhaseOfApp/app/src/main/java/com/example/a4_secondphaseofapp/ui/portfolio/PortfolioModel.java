package com.example.a4_secondphaseofapp.ui.portfolio;

public class PortfolioModel {
    private String name;
    private String ticker;
    private String logo;
    private String shareOutstanding;

    public PortfolioModel() {
        name = "0";
        ticker = "0";
        logo = "0";
        shareOutstanding = "0";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getShareOutstanding() {
        return shareOutstanding;
    }

    public void setShareOutstanding(String shareOutstanding) {
        this.shareOutstanding = shareOutstanding;
    }
}
