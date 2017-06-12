package com.deakishin.cipherworld.gui;

/**
 * Interface for an activity that can change its background and status bar colors.
 */
public interface ColorChangingActivity {

    /**
     * Changes background and status bar colors.
     *
     * @param backgroundColor Color for the background.
     * @param statusBarColor  Color for the status bar.
     */
    void changeColors(int backgroundColor, int statusBarColor);
}
