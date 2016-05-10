package com.mygdx.buildthetower;

/**
 * Created by musial321 on 4/3/2016.
 */
public class DummyAdsController implements AdsController {
    @Override
    public void showBannerAd() {

    }

    @Override
    public void hideBannerAd() {

    }

    @Override
    public boolean isWifiConnected() {
        return false;
    }

    @Override
    public void showInterstitialAd(Runnable then) {

    }
}
