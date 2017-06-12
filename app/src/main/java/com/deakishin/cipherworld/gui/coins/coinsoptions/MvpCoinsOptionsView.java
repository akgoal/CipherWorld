package com.deakishin.cipherworld.gui.coins.coinsoptions;

/**
 * Interface for a view that displays coins options.
 */
public interface MvpCoinsOptionsView {

    /**
     * Sets reward coins for clicking on an ad.
     *
     * @param coins Number of coins.
     */
    void setCoinsForAdClicking(int coins);

    /**
     * Sets reward coins for watching an ad.
     *
     * @param coins Number of coins.
     */
    void setCoinsForAdWatching(int coins);

    /**
     * Invokes displaying an ad video.
     */
    void showAdVideo();

    /**
     * Shows/hides error about being unable to display the ad video.
     *
     * @param toShow True if the error must be shown, false otherwise.
     */
    void setAdVideoErrorShown(boolean toShow);

    /**
     * Exits the view.
     */
    void exit();
}
