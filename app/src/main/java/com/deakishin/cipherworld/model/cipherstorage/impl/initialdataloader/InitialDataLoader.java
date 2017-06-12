package com.deakishin.cipherworld.model.cipherstorage.impl.initialdataloader;

/**
 * Interface for loading initial data.
 */
public interface InitialDataLoader {
    /**
     * Loads initial data.
     *
     * @param handler Object for handling loaded data items.
     */
    void loadData(LoadedItemHandler handler);

    /**
     * Interface for handling loaded data items.
     */
    interface LoadedItemHandler {
        /**
         * Invoked when a cipher's initial data is loaded.
         *
         * @param cipherId      Cipher's id.
         * @param level         Number of the level that the cipher belongs to.
         * @param number        Number of the cipher on the level.
         * @param description   Cipher's description.
         * @param solution      Cipher's solution.
         * @param openedLetters String that contains cipher's opened letters.
         */
        void onCipherLoaded(int cipherId, int level, int number, String description,
                            String solution, String openedLetters);
    }
}
