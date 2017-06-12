package com.deakishin.cipherworld.presenters;

import com.deakishin.cipherworld.gui.levelsscreen.singlelevel.MvpSingleLevelView;
import com.deakishin.cipherworld.model.levelsmanager.LevelsManager;

/**
 * MVP Presenter for providing info about levels.
 */
public class MvpSingleLevelPresenterImpl implements MvpSingleLevelPresenter,
        LevelsManager.LevelsInfoChangesListener {

    private MvpSingleLevelView mView;

    // Level number.
    private int mLevel;

    // Object for getting info about the level.
    private LevelsManager mLevelsManager;

    public MvpSingleLevelPresenterImpl(LevelsManager levelsManager) {
        mLevelsManager = levelsManager;
    }

    @Override
    public void bindView(MvpSingleLevelView view, int level) {
        mView = view;
        mLevel = level;

        mLevelsManager.addChangesListener(this);

        updateView();
    }

    @Override
    public void unbindView() {
        mView = null;
        mLevelsManager.removeChangesListener(this);
    }

    @Override
    public void onCipherClicked(int cipherId) {
        mView.goToCipher(cipherId);
    }

    // Updates view.
    private void updateView() {
        if (mView != null) {
            int ciphersToSolve = mLevelsManager.getCiphersToSolveCount(mLevel);
            if (ciphersToSolve > 0) {
                mView.setLocked(true, ciphersToSolve);
            } else {
                mView.setLocked(false, -1);
                mView.setCiphersInfo(mLevelsManager.getCiphersForLevel(mLevel));
            }
        }
    }

    @Override
    public void onInfoChanged() {
        updateView();
    }
}
