package com.deakishin.cipherworld.model.levelsmanager;

import com.deakishin.cipherworld.model.cipherstorage.CipherShortInfo;

import java.util.List;

/**
 * Interface for a manager that manages levels and progress on them.
 */
public interface LevelsManager {

    /**
     * Ratio between the number of solved cipher
     * and the total number of ciphers
     * required to open the next level.
     */
    float REQUIRED_SOLVED_RATIO = (float) 0.8;

    /**
     * @param level Level number.
     * @return Number of ciphers that have to be solved to open the level,
     * or -1 if the level is already open.
     */
    int getCiphersToSolveCount(int level);

    /**
     * @return Number of levels.
     */
    int getLevelCount();

    /**
     * @return Last displayed level.
     */
    int getLastLevel();

    /**
     * Sets last displayed level.
     *
     * @param level Level number.
     */
    void setLastLevel(int level);


    /**
     * Updates the manager with fresh data.
     * Needs to be invoked every time there is a change to the data.
     */
    void update();

    /**
     * @param level Level number.
     * @return True if all cipher on the level are solved, false otherwise.
     */
    boolean isLevelSolved(int level);

    /**
     * Gets ciphers (short info about them) for a level.
     *
     * @param levelNumber Number of the level to return ciphers for.
     * @return List of objects, each containing short info about a cipher.
     * Ciphers are placed in the list according to their number on the level.
     */
    List<CipherShortInfo> getCiphersForLevel(int levelNumber);

    /**
     * Adds listener to levels info changes.
     *
     * @param listener Listener to add.
     */
    void addChangesListener(LevelsInfoChangesListener listener);

    /**
     * Removes listener to level info changes.
     *
     * @param listener Listener to remove.
     */
    void removeChangesListener(LevelsInfoChangesListener listener);

    /**
     * Listener interface to be notified when there is a change
     * to the data in the storage.
     */
    interface LevelsInfoChangesListener {
        /**
         * Invoked when levels info is changed.
         */
        void onInfoChanged();
    }
}
