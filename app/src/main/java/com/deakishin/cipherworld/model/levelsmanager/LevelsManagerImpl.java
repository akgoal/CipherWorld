package com.deakishin.cipherworld.model.levelsmanager;

import com.deakishin.cipherworld.model.cipherstorage.CipherShortInfo;
import com.deakishin.cipherworld.model.cipherstorage.CipherStorage;
import com.deakishin.cipherworld.model.settings.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages levels and progress on them.
 */
public class LevelsManagerImpl implements LevelsManager {

    // Object for getting data from.
    private CipherStorage mStorage;

    // Object for working with settings.
    private Settings mSettings;

    // Number of ciphers to solve to open each level,
    // or - 1 if level is open.
    private int[] mCipherToSolveCount;

    // Number of the current level.
    private int mCurrentLevel;

    // Listeners to info changes.
    private List<LevelsInfoChangesListener> mListeners = new ArrayList<>();

    public LevelsManagerImpl(CipherStorage storage, Settings settings) {
        mStorage = storage;
        mSettings =settings;

        mStorage.addChangesListener(new CipherStorage.ChangesListener() {
            @Override
            public void onChange() {
                update();
                notifyListeners();
            }
        });
        update();

        mCurrentLevel = settings.loadLastLevel();
        if (mCurrentLevel < 1 || mCurrentLevel >= getLevelCount()) {
            setLastLevel(1);
        }
    }

    @Override
    public int getCiphersToSolveCount(int level) {
        // return -1;
        return mCipherToSolveCount[level - 1];
    }

    @Override
    public int getLevelCount() {
        if (mCipherToSolveCount == null) {
            return 0;
        }
        return mCipherToSolveCount.length;
    }

    @Override
    public int getLastLevel() {
        return mCurrentLevel;
    }

    @Override
    public void setLastLevel(int level) {
        mCurrentLevel = level;
        mSettings.saveLastLevel(mCurrentLevel);
    }

    @Override
    public List<CipherShortInfo> getCiphersForLevel(int levelNumber) {
        return mStorage.getCiphersForLevel(levelNumber);
    }

    @Override
    public void addChangesListener(LevelsInfoChangesListener listener) {
        mListeners.add(listener);
    }

    @Override
    public void removeChangesListener(LevelsInfoChangesListener listener) {
        mListeners.remove(listener);
    }

    // Notifies listeners about changes.
    private void notifyListeners() {
        for (LevelsInfoChangesListener listener : mListeners) {
            listener.onInfoChanged();
        }
    }

    @Override
    public void update() {
        mCipherToSolveCount = new int[mStorage.getLevelCount()];
        mCipherToSolveCount[0] = -1;
        int total = 0; // Total number of ciphers.
        int solved = 0; // Number of solved ciphers.
        for (int level = 1; level <= mCipherToSolveCount.length; level++) {
            int toSolve = (int) (total * REQUIRED_SOLVED_RATIO - solved); // Number of ciphers to solve to open the level.

            mCipherToSolveCount[level - 1] = toSolve <= 0 ? -1 : toSolve;

            int[] info = mStorage.getCiphersSolvedCountForLevel(level);
            total += info[0];
            solved += info[1];
        }
    }

    @Override
    public boolean isLevelSolved(int level) {
        int[] info = mStorage.getCiphersSolvedCountForLevel(level);
        return info[0] == info[1];
    }
}
