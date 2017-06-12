package com.deakishin.cipherworld.gui.coins.coinsdisplay;

/**
 * Interface for a view that displays coins info.
 */
public interface MvpCoinsDisplayView {

    /** Sets coins to display.
     * @param coins Number of coins. */
    void setCoins(int coins);

    /** Shows coins options. */
    void showCoinsOptions();
}
