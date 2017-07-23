package com.deakishin.cipherworld.model.coinsmanager;

import com.deakishin.cipherworld.model.settings.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager for managing coins, earning and spending them.
 */
public class CoinsManagerImpl implements CoinsManager {

    // Object for storing data to, and loading data to, the memory.
    private Settings mSettings;

    // Number of coins.
    private int mCoins;

    /**
     * Prices.
     */
    private static class PRICES {
        static final int OPEN_SYMBOL = 80;
        static final int CHECK_LETTER = 20;
    }

    /**
     * Rewards.
     */
    private static class REWARDS {
        static final int CIPHER_SOLVING = 25;
        static final double CIPHER_SOLVING_FACTOR = 0.2;

        static final int LEVEL_SOLVING = 150;
        static final double LEVEL_SOLVING_FACTOR = 0.2;

        static final int AD_WATCHING = 15;
        static final int AD_CLICKING = 5;
    }

    // Listeners to coins changes.
    private List<OnCoinsChangedListener> mListeners = new ArrayList<>();

    public CoinsManagerImpl(Settings settings) {
        mSettings = settings;

        mCoins = mSettings.loadCoins();
    }

    // Saves number of coins.
    private void saveCoins() {
        mSettings.saveCoins(mCoins);
    }

    @Override
    public int getCoins() {
        return mCoins;
    }

    @Override
    public int getPrice(Product product) {
        return getPrice(product, 1);
    }

    @Override
    public int getPrice(Product product, int count) {
        int price;
        switch (product) {
            case OPEN_SYMBOL:
                price = PRICES.OPEN_SYMBOL;
                break;
            case CHECK_LETTERS:
                price = PRICES.CHECK_LETTER;
                break;
            default:
                price = 0;
        }
        return count * price;
    }

    @Override
    public void spendCoins(Product product) {
        spendCoins(product, 1);
    }

    @Override
    public void spendCoins(Product product, int count) {
        int price = getPrice(product, count);
        mCoins -= price;
        if (mCoins < 0) {
            mCoins = 0;
        }

        updateListeners();
        saveCoins();
    }

    @Override
    public boolean enoughCoins(Product product) {
        return enoughCoins(product, 1);
    }

    @Override
    public boolean enoughCoins(Product product, int count) {
        return mCoins >= getPrice(product, count);
    }

    @Override
    public int getReward(EarnWay earnWay) {
        switch (earnWay) {
            case AD_WATCHING:
                return REWARDS.AD_WATCHING;
            case AD_CLICKING:
                return REWARDS.AD_CLICKING;
            default:
                return 0;
        }
    }

    @Override
    public void addCoins(EarnWay earnWay) {
        mCoins += getReward(earnWay);

        updateListeners();
        saveCoins();
    }

    @Override
    public void addCoinsForSolvingCipher(int level, boolean levelIsSolved) {
        int reward = getRewardForSolvingCipher(level);

        mCoins += reward;

        if (levelIsSolved) {
            mCoins += getRewardForSolvingLevel(level);
        }

        updateListeners();
        saveCoins();
    }

    @Override
    public int getRewardForSolvingCipher(int level) {
        return REWARDS.CIPHER_SOLVING
                + (int) (REWARDS.CIPHER_SOLVING * REWARDS.CIPHER_SOLVING_FACTOR) * (level - 1);
    }

    @Override
    public void addCoinsForSolvingLevel(int level) {
        int reward = getRewardForSolvingLevel(level);

        mCoins += reward;

        updateListeners();
        saveCoins();
    }

    @Override
    public int getRewardForSolvingLevel(int level) {
        return REWARDS.LEVEL_SOLVING
                + (int) (REWARDS.LEVEL_SOLVING * REWARDS.LEVEL_SOLVING_FACTOR) * (level - 1);
    }

    // Updates listeners.
    private void updateListeners() {
        for (OnCoinsChangedListener listener : mListeners) {
            listener.onCoinsChanged(mCoins);
        }
    }

    @Override
    public void addOnCoinsChangedListener(OnCoinsChangedListener onCoinsChangedListener) {
        mListeners.add(onCoinsChangedListener);
    }

    @Override
    public void removeOnCoinsChangedListener(OnCoinsChangedListener onCoinsChangedListener) {
        mListeners.remove(onCoinsChangedListener);
    }
}
