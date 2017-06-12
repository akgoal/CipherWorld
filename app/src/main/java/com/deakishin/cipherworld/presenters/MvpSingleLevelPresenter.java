package com.deakishin.cipherworld.presenters;

import com.deakishin.cipherworld.gui.levelsscreen.singlelevel.MvpSingleLevelView;

/**
 * MVP Presenter interface for working with a single level.
 */
public interface MvpSingleLevelPresenter {

    /**
     * Binds MVP View to the Presenter.
     *
     * @param view  View to bind.
     * @param level Number of the level to work with.
     */
    void bindView(MvpSingleLevelView view, int level);

    /**
     * Unbinds view.
     */
    void unbindView();

    /**
     * Invoked when a cipher is clicked on.
     *
     * @param cipherId Cipher's id.
     */
    void onCipherClicked(int cipherId);
}
