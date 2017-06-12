package com.deakishin.cipherworld.gui.levelsscreen.levels;

/**
 * Interface for a view that displays levels.
 */
public interface MvpLevelsView {

    /**
     * Enables/disables button for going to the previous level.
     *
     * @param enabled True if the button must be enabled, false if it has to be disabled.
     */
    void setPrevLevelButtonEnabled(boolean enabled);

    /**
     * Enables/disables button for going to the next level.
     *
     * @param enabled True if the button must be enabled, false if it has to be disabled.
     */
    void setNextLevelButtonEnabled(boolean enabled);

    /**
     * Sets level to display.
     *
     * @param levelNumber Level number to set (starting from 1).
     */
    void setLevel(int levelNumber);

    /**
     * Animates going to the next level.
     */
    void goToNextLevel();

    /**
     * Animates going to the previous level.
     */
    void goToPrevLevel();
}
