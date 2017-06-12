package com.deakishin.cipherworld.presenters.ads;

import com.deakishin.cipherworld.model.coinsmanager.CoinsManager;

/**
 * MVP Presenter for working with video ads.
 */
public class MvpAdVideoPresenterImpl implements MvpAdVideoPresenter {

    // Manager for rewarding with coins.
    private CoinsManager mCoinsManager;

    public MvpAdVideoPresenterImpl(CoinsManager coinsManager) {
        mCoinsManager = coinsManager;
    }

    @Override
    public void onVideoAdWatched() {
        mCoinsManager.addCoins(CoinsManager.EarnWay.AD_WATCHING);
    }
}
