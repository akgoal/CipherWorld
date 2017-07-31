package com.deakishin.cipherworld.model.coinsmanager;

/**
 * Interface for managing coins.
 * Also for managing spending and earning coins.
 */
public interface CoinsManager {

    /**
     * Defines something that coins can be spent on.
     */
    enum Product {
        /**
         * Opening a symbol in a cipher.
         */
        OPEN_SYMBOL,
        /**
         * Checking letters in the current solution.
         */
        CHECK_LETTERS
    }

    /**
     * Defines a way to earn coins.
     */
    enum EarnWay {
        /**
         * Watching an ad.
         */
        AD_WATCHING,
        /**
         * Clicking on an ad.
         */
        AD_CLICKING
    }

    /**
     * @return Current number of coins.
     */
    int getCoins();

    /**
     * @return How much a product costs in coins.
     */
    int getPrice(Product product);

    /**
     * @param product Product to get price for.
     * @param count   Number of product items.
     * @return How much products cost in coins.
     */
    int getPrice(Product product, int count);

    /**
     * Spends coins on a product.
     * If current number of coins is less than the product's price,
     * then all the coins are spent.
     *
     * @param product Something to spend coins on.
     */
    void spendCoins(Product product);

    /**
     * Spends coins on products.
     * If current number of coins is less than the product's price,
     * then all the coins are spent.
     *
     * @param product Something to spend coins on.
     * @param count   Number of product items.
     */
    void spendCoins(Product product, int count);

    /**
     * Checks if there are enough coins for spending on a product.
     *
     * @param product Product to check.
     * @return True if there are enough coins for the product,
     * false otherwise.
     */
    boolean enoughCoins(Product product);

    /**
     * Checks if there are enough coins for spending on products.
     *
     * @param product Product to check coins for.
     * @param count   Number of product items.
     * @return True if there are enough coins for the product,
     * false otherwise.
     */
    boolean enoughCoins(Product product, int count);

    /**
     * @param earnWay How coins are earned
     * @return Number of coins that can be earned,
     * depending on the wat the coins are earned.
     */
    int getReward(EarnWay earnWay);

    /**
     * Adds coins to the current number of coins.
     *
     * @param earnWay How the coins were earned.
     */
    void addCoins(EarnWay earnWay);

    /**
     * Adds coins for solving a cipher.
     *
     * @param level            Level on which the cipher was solved.
     * @param levelIsSolved    True if all ciphers on the level are solved.
     *                         In this case, bonus coins will be added.
     *                         Number of this bonus coins can be obtained with {@link #getRewardForSolvingLevel(int)} method.
     * @param delimitersOpened True if delimiters are opened for the solved cipher. In this case,
     *                         number of reward coins is decreased.
     */
    void addCoinsForSolvingCipher(int level, boolean levelIsSolved, boolean delimitersOpened);

    /**
     * Gets reward for solving a cipher.
     *
     * @param level            Number of the level that the cipher belongs to.
     * @param delimitersOpened True if delimiters are opened for the cipher.
     * @return Number of reward coins.
     */
    int getRewardForSolvingCipher(int level, boolean delimitersOpened);

    /**
     * Adds coins for solving a level completely.
     *
     * @param level Number of the level.
     */
    void addCoinsForSolvingLevel(int level);

    /**
     * Gets reward for solving every cipher on a level.
     *
     * @param level Number of the level.
     * @return Number of reward coins.
     */
    int getRewardForSolvingLevel(int level);

    /**
     * Listener interface to receive notifications when coins number is changed.
     */
    interface OnCoinsChangedListener {
        /**
         * Invoked when coins number is changed.
         *
         * @param coins New coins number.
         */
        void onCoinsChanged(int coins);
    }

    /**
     * Adds a listener to coins changes.
     *
     * @param onCoinsChangedListener Listener to add.
     */
    void addOnCoinsChangedListener(OnCoinsChangedListener onCoinsChangedListener);

    /**
     * Removes a listener to coins changes.
     *
     * @param onCoinsChangedListener Listener to remove.
     */
    void removeOnCoinsChangedListener(OnCoinsChangedListener onCoinsChangedListener);
}
