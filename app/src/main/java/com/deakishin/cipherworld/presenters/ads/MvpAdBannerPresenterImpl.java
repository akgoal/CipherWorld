package com.deakishin.cipherworld.presenters.ads;

import com.deakishin.cipherworld.model.coinsmanager.CoinsManager;

/**
 * MVP Presenter for working with banner ads.
 */
public class MvpAdBannerPresenterImpl implements MvpAdBannerPresenter {

    // Manager for increasing coins when a banner ad is tapped on.
    private CoinsManager mCoinsManager;

    public MvpAdBannerPresenterImpl(CoinsManager coinsManager) {
        mCoinsManager = coinsManager;
    }

    @Override
    public void onBannerAdOpened() {
       // mCoinsManager.addCoins(CoinsManager.EarnWay.AD_CLICKING);
    }
}
