package com.deakishin.cipherworld.presenters;

import com.deakishin.cipherworld.gui.levelsscreen.levels.MvpLevelsView;

/**
 * MVP Presenter interface for controlling access to the info about levels.
 */
public interface MvpLevelsPresenter {

    /**
     * Binds MVP View to the Presenter.
     *
     * @param view View to bind.
     */
    void bindView(MvpLevelsView view);

    /**
     * Unbinds view.
     */
    void unbindView();

    /**
     * @return Total number of levels.
     */
    int getLevelCount();

    /**
     * Invoked when previous level button is clicked.
     */
    void onPrevLevelClicked();

    /**
     * Invoked when next level button is clicked.
     */
    void onNextLevelClicked();

    /**
     * Invoked when a level is selected.
     *
     * @param levelNumber Number of the selected level.
     */
    void onLevelSelected(int levelNumber);
}
