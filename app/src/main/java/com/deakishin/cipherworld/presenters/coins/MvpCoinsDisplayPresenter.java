package com.deakishin.cipherworld.presenters.coins;

import com.deakishin.cipherworld.gui.coins.coinsdisplay.MvpCoinsDisplayView;

/**
 * MVP Presenter interface for working with coins info.
 */
public interface MvpCoinsDisplayPresenter {

    /**
     * Binds MVP View to the Presenter.
     *
     * @param view View to bind.
     */
    void bindView(MvpCoinsDisplayView view);

    /**
     * Unbinds view.
     */
    void unbindView();

    /**
     * Invoked when coins info is clicked.
     */
    void onCoinsClicked();
}
