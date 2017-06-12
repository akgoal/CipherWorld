package com.deakishin.cipherworld.presenters;

import com.deakishin.cipherworld.gui.levelsscreen.levels.MvpLevelsView;
import com.deakishin.cipherworld.model.levelsmanager.LevelsManager;

/**
 * MVP Presenter for accessing the levels data and updating View.
 */
public class MvpLevelsPresenterImpl implements MvpLevelsPresenter {

    // MVP View.
    private MvpLevelsView mView;

    // Managing for accessing levels info.
    private LevelsManager mLevelsManager;

    // Current level.
    int mCurrentLevel;
    // Total number of levels.
    private int mLevelCount;

    public MvpLevelsPresenterImpl(LevelsManager levelsManager) {
        mLevelsManager = levelsManager;

        if (mLevelsManager != null) {
            mLevelCount = mLevelsManager.getLevelCount();
            mCurrentLevel = mLevelsManager.getLastLevel();
        }
    }

    @Override
    public void bindView(MvpLevelsView view) {
        mView = view;
        if (isViewSet()) {
            updateView();
        }
    }

    @Override
    public void unbindView() {
        mView = null;
    }

    // Updates view.
    private void updateView() {
        mView.setLevel(mCurrentLevel);
        mView.setNextLevelButtonEnabled(mCurrentLevel < mLevelCount);
        mView.setPrevLevelButtonEnabled(mCurrentLevel > 1);
    }

    @Override
    public int getLevelCount() {
        return mLevelCount;
    }

    // Sets current level and saves it to the manager.
    private void setCurrentLevel(int level){
        mCurrentLevel = level;
        mLevelsManager.setLastLevel(mCurrentLevel);
    }

    /* Increases/decreases current level depending on the parameter. */
    private void changeCurrentLevel(boolean toIncrease) {
        if (toIncrease) {
            if (mCurrentLevel < mLevelCount) {
                setCurrentLevel(mCurrentLevel + 1);
            }
        } else {
            if (mCurrentLevel > 1) {
                setCurrentLevel(mCurrentLevel - 1);
            }
        }
    }

    @Override
    public void onPrevLevelClicked() {
        changeCurrentLevel(false);
        if (isViewSet()) {
            mView.goToPrevLevel();
            updateView();
        }
    }

    @Override
    public void onNextLevelClicked() {
        changeCurrentLevel(true);
        if (isViewSet()) {
            mView.goToNextLevel();
            updateView();
        }
    }

    @Override
    public void onLevelSelected(int levelNumber) {
        if (levelNumber > 0 && levelNumber <= mLevelCount) {
            setCurrentLevel(levelNumber);
        }
        if (isViewSet()) {
            updateView();
        }
    }

    // Indicates if the view is set or not.
    private boolean isViewSet() {
        return mView != null;
    }
}
