package com.deakishin.cipherworld.model.settings;

import android.content.SharedPreferences;

/**
 * Implements saving and loading data with the help of {@link android.content.SharedPreferences}.
 */
public class SettingsImpl implements Settings {

    // Keys for storing data.
    private static final String PREF_COINS = "coins";
    private static final String PREF_LAST_LEVEL = "lastLevel";

    // Default values.
    private static final int DEF_COINS = 0;
    private static final int DEF_LAST_LEVEL = 1;

    // Object for storing and retrieving data.
    private SharedPreferences mPrefs;

    public SettingsImpl(SharedPreferences prefs) {
        mPrefs = prefs;
    }

    @Override
    public void saveCoins(int coins) {
        mPrefs.edit().putInt(PREF_COINS, coins).apply();
    }

    @Override
    public int loadCoins() {
        return mPrefs.getInt(PREF_COINS, DEF_COINS);
    }

    @Override
    public void saveLastLevel(int level) {
        mPrefs.edit().putInt(PREF_LAST_LEVEL, level).apply();
    }

    @Override
    public int loadLastLevel() {
        return mPrefs.getInt(PREF_LAST_LEVEL, DEF_LAST_LEVEL);
    }
}
