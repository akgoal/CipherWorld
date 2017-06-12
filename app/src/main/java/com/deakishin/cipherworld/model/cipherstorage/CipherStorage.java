package com.deakishin.cipherworld.model.cipherstorage;

import java.util.List;

/**
 * Interface for storing and retrieving ciphers and information about them.
 */
public interface CipherStorage {
    /**
     * @param levelNumber Level to return ciphers for.
     * @return List of short info objects for ciphers on the given level.
     */
    List<CipherShortInfo> getCiphersForLevel(int levelNumber);

    /**
     * @param levelNumber Level to return info for.
     * @return Array with two numbers. First one is the total number
     * of ciphers on the level. Second one is the number of solved ciphers
     * on the level.
     */
    int[] getCiphersSolvedCountForLevel(int levelNumber);

    /**
     * @param cipherId Cipher's id.
     * @return Cipher with the given id, or null if the cipher is not found.
     */
    CipherInfo getCipher(int cipherId);

    /**
     * Marks the cipher with the given id as solved.
     *
     * @param cipherId Cipher's id.
     */
    void setCipherSolved(int cipherId);

    /**
     * Sets opened letters for a cipher.
     * These letters will be considered opened.
     *
     * @param cipherId      Cipher's id.
     * @param openedLetters String containing opened letters.
     */
    void setCipherOpenedLetters(int cipherId, String openedLetters);

    /**
     * Sets current solution for a cipher.
     * Current solution is a string that represents current result of decrypting.
     *
     * @param cipherId        Cipher's id.
     * @param currentSolution String that contains current solution.
     */
    void setCipherCurrentSolution(int cipherId, String currentSolution);

    /**
     * @return Total number of levels.
     */
    int getLevelCount();

    /**
     * Adds listener to data changes.
     *
     * @param listener Listener to add.
     */
    void addChangesListener(ChangesListener listener);

    /**
     * Removes listener to data changes.
     *
     * @param listener Listener to remove.
     */
    void removeChangesListener(ChangesListener listener);

    /**
     * Listener interface to be notified when there is a change
     * to the data in the storage.
     */
    interface ChangesListener {
        /**
         * Invoked when data in the storage is changed.
         */
        void onChange();
    }
}
