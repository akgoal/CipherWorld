package com.deakishin.cipherworld.model.settings;

/**
 * Interface for saving data to and loading it from the memory.
 */
public interface Settings {

    /**
     * Saves coins.
     *
     * @param coins Number of coins.
     */
    void saveCoins(int coins);

    /**
     * Loads coins.
     *
     * @return Number of coins. Returns 0 if there is no saved coins.
     */
    int loadCoins();

    /**
     * Saves last level.
     *
     * @param level number.
     */
    void saveLastLevel(int level);

    /**
     * @return Number of the last level. Returns 1 if there is no saved last level.
     */
    int loadLastLevel();
}
