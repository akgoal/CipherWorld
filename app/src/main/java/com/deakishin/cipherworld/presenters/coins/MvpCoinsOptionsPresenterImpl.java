package com.deakishin.cipherworld.presenters.coins;

import com.deakishin.cipherworld.gui.coins.coinsoptions.MvpCoinsOptionsView;
import com.deakishin.cipherworld.model.coinsmanager.CoinsManager;

/**
 * MVP Presenter for working with coins options.
 */
public class MvpCoinsOptionsPresenterImpl implements MvpCoinsOptionsPresenter {

    // MVP View.
    private MvpCoinsOptionsView mView;

    // Object for working with coins.
    private CoinsManager mCoinsManager;

    public MvpCoinsOptionsPresenterImpl(CoinsManager coinsManager) {
        mCoinsManager = coinsManager;
    }

    @Override
    public void bindView(MvpCoinsOptionsView view) {
        mView = view;

        updateView();
        if (mView != null) mView.setAdVideoErrorShown(false);
    }

    // Updates View.
    private void updateView() {
        if (mView != null) {
            mView.setCoinsForAdClicking(mCoinsManager.getReward(CoinsManager.EarnWay.AD_CLICKING));
            mView.setCoinsForAdWatching(mCoinsManager.getReward(CoinsManager.EarnWay.AD_WATCHING));
        }
    }

    @Override
    public void unbindView() {
        mView = null;
    }

    @Override
    public void onWatchAdClicked() {
        if (mView != null) mView.showAdVideo();
    }

    @Override
    public void onAdVideoDisplayed(boolean success) {
        if (mView != null) mView.setAdVideoErrorShown(!success);
    }

    @Override
    public void onCancelClicked() {
        mView.exit();
    }
}
