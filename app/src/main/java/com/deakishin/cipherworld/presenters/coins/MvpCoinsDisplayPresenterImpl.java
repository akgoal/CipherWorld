package com.deakishin.cipherworld.presenters.coins;

import android.util.Log;

import com.deakishin.cipherworld.gui.coins.coinsdisplay.MvpCoinsDisplayView;
import com.deakishin.cipherworld.model.coinsmanager.CoinsManager;

/**
 * MVP Presenter for working with coins info.
 */
public class MvpCoinsDisplayPresenterImpl implements MvpCoinsDisplayPresenter,
        CoinsManager.OnCoinsChangedListener {

    // Object for working woth coins info.
    private CoinsManager mCoinsManager;

    // MVP View.
    private MvpCoinsDisplayView mView;

    // Number of coins.
    private int mCoins;

    public MvpCoinsDisplayPresenterImpl(CoinsManager coinsManager) {
        mCoinsManager = coinsManager;
    }

    @Override
    public void bindView(MvpCoinsDisplayView view) {
        mView = view;
        mCoins = mCoinsManager.getCoins();
        updateView();

        mCoinsManager.addOnCoinsChangedListener(this);
    }

    // Updates View.
    private void updateView() {
        if (mView != null) {
            mView.setCoins(mCoinsManager.getCoins());
        }
    }

    @Override
    public void unbindView() {
        mView = null;
        mCoinsManager.removeOnCoinsChangedListener(this);
    }

    @Override
    public void onCoinsClicked() {
        mView.showCoinsOptions();
    }

    @Override
    public void onCoinsChanged(int coins) {
        mCoins = coins;
        updateView();
    }
}
