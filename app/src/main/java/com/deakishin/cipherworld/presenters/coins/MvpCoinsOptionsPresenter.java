package com.deakishin.cipherworld.presenters.coins;

import com.deakishin.cipherworld.gui.coins.coinsdisplay.MvpCoinsDisplayView;
import com.deakishin.cipherworld.gui.coins.coinsoptions.MvpCoinsOptionsView;

/**
 * MVP Presenter interface for working with coins options.
 */
public interface MvpCoinsOptionsPresenter {

    /**
     * Binds MVP View to the Presenter.
     *
     * @param view View to bind.
     */
    void bindView(MvpCoinsOptionsView view);

    /**
     * Unbinds view.
     */
    void unbindView();

    /**
     * Invoked when watching ad is selected.
     */
    void onWatchAdClicked();

    /**
     * Indicates if displaying the ad video was successful or not.
     *
     * @param success True if the ad was shown successfully, false otherwise.
     */
    void onAdVideoDisplayed(boolean success);

    /**
     * Invoked when the Cancel option is selected.
     */
    void onCancelClicked();
}
