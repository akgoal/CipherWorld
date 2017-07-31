package com.deakishin.cipherworld.presenters;

import android.os.Bundle;

import com.deakishin.cipherworld.gui.cipherscreen.MvpCipherView;
import com.deakishin.cipherworld.model.ciphermanager.CipherManager;
import com.deakishin.cipherworld.model.cipherstorage.CipherInfo;
import com.deakishin.cipherworld.model.coinsmanager.CoinsManager;
import com.deakishin.cipherworld.model.levelsmanager.LevelsManager;

import java.util.ArrayList;
import java.util.Map;

import icepick.Icepick;
import icepick.State;

/**
 * MVP Presenter for working with a cipher.
 */
public class MvpCipherPresenterImpl implements MvpCipherPresenter {

    // MVP View.
    private MvpCipherView mView;

    // Manager to work with the cipher.
    private CipherManager mCipherManager;

    // Manager to work with coins.
    private CoinsManager mCoinsManager;

    // Manager for checking if all ciphers on the level are solved.
    private LevelsManager mLevelsManager;

    // Current selected symbol.
    @State
    int mSymbolId = -1;

    // Indicates that the Open symbol hint option was selected.
    @State
    boolean mOpenSymbolHintSelected = false;

    // Indicates that the Check letters hint option was selected.
    @State
    boolean mCheckLettersHintSelected = false;

    // Indicates that the Open delimiters hint option was selected.
    @State
    boolean mOpenDelimitersHintSelected = false;

    // Cipher is solved.
    private boolean mSolved;

    public MvpCipherPresenterImpl(CipherManager cipherManager,
                                  CoinsManager coinsManager, LevelsManager levelsManager) {
        mCipherManager = cipherManager;
        mCoinsManager = coinsManager;
        mLevelsManager = levelsManager;
    }

    @Override
    public void bindView(MvpCipherView view, int cipherId) {
        mView = view;
        if (mView == null) {
            return;
        }

        mView.setAnimateChanges(false);
        if (!mCipherManager.setUp(cipherId)) {
            mView.exit();
        } else {
            mSolved = mCipherManager.checkIfSolved();
            updateView();
            if (!mSolved) {
                if (mCheckLettersHintSelected) {
                    closeKeyboardAndSymbolHints();

                    setOverallHintsShown(true);
                    showHintCheckLettersPanel();
                } else {
                    if (mOpenDelimitersHintSelected) {
                        closeKeyboardAndSymbolHints();

                        setOverallHintsShown(true);
                        showHintOpenDelimitersPanel();
                    } else {
                        closeHintCheckLettersPanel();
                        closeHintOpenDelimitersPanel();
                        if (mSymbolId > -1) {
                            mView.setSelectedSymbol(mSymbolId);
                            showViewKeyboardAndSymbolHintsControls();
                            setOverallHintsShown(false);
                            if (mOpenSymbolHintSelected) {
                                showOpenSymbolHintPanel();
                            } else {
                                closeHintSymbolPanel();
                            }
                        } else {
                            closeKeyboardAndSymbolHints();
                            setOverallHintsShown(true);
                        }
                    }
                }
            } else {
                closeHintCheckLettersPanel();
                closeHintOpenDelimitersPanel();
                closeKeyboardAndSymbolHints();
                setOverallHintsShown(false);
            }
        }
        mView.setAnimateChanges(true);
    }

    // Shows View's keyboard and symbol-specific hint buttons.

    private void showViewKeyboardAndSymbolHintsControls() {
        if (mView == null) return;

        mView.showKeyboard(constructOccupiedLetters(mSymbolId),
                mCipherManager.getCipherInfo().getOpenedLetters(),
                mCipherManager.getLetterMapping().get(mSymbolId));
        mView.setHintSymbolShown(true);
    }

    // Closes keyboard, erases all info connected to it.
    // Also hides symbol-specific hint buttons and hint panels.
    private void closeKeyboardAndSymbolHints() {
        mSymbolId = -1;

        if (mView != null) mView.setSelectedSymbol(-1);

        closeHintSymbolPanel();

        if (mView != null) {
            mView.hideKeyboard();
            mView.setHintSymbolShown(false);
        }
    }

    // Closes the Hint symbol panel.
    private void closeHintSymbolPanel() {
        mOpenSymbolHintSelected = false;
        mCheckLettersHintSelected = false;
        if (mView != null) mView.closeHintSymbolPanel();
    }

    // Closes the Check letter hint panel.
    private void closeHintCheckLettersPanel() {
        mCheckLettersHintSelected = false;
        if (mView != null) mView.closeHintCheckLettersPanel();
    }

    // Closes the Open delimiters hint panel.
    private void closeHintOpenDelimitersPanel() {
        mOpenDelimitersHintSelected = false;
        if (mView != null) mView.closeHintOpenDelimitersPanel();
    }

    // Shows/hides overall, not symbol-specific hints.
    private void setOverallHintsShown(boolean toShow) {
        if (mView != null) {
            mView.setHintCheckLettersShown(toShow && mCipherManager.getLettersCount() > 0);
            mView.setHintOpenDelimitersShown(toShow && !mCipherManager.getCipherInfo().isDelimiterOpened());
        }
    }

    // Updates view.
    private void updateView() {
        if (mView == null) return;

        CipherInfo cipher = mCipherManager.getCipherInfo();
        mView.setCipherData(cipher.getShortInfo().getLevel(), cipher.getShortInfo().getNumber());
        mView.setCipherQuestion(cipher.getQuestion());
        mView.setSolved(mSolved);
        mView.setSymbols(mCipherManager.getSymbols());

        updateDelimiters();

        updateViewSymbols();
    }

    // Updates View's delimiters.
    private void updateDelimiters(){
        if (mSolved || mCipherManager.getCipherInfo().isDelimiterOpened()) {
            mView.setDelimiterPositions(mCipherManager.getDelimiters());
        }
    }

    // Updates View's symbols info.
    private void updateViewSymbols() {
        if (mView == null) return;

        mView.setLetters(mSolved ?
                mCipherManager.getSolutionMapping() :
                mCipherManager.getLetterMapping());

        mView.setOpenedLetters(mSolved ?
                new ArrayList<>(mCipherManager.getSolutionMapping().keySet()) :
                mCipherManager.getOpenedSymbols());
    }

    @Override
    public void unbindView() {
        mView = null;
    }

    @Override
    public void onPaused() {
        mCipherManager.saveCurrentSolution();
    }

    @Override
    public void onSymbolClicked(int symbolId) {
        if (mSolved) {
            return;
        }

        if (mCipherManager.getOpenedSymbols().contains(symbolId)) {
            return;
        }

        if (mSymbolId == symbolId) {
            closeKeyboardAndSymbolHints();
            setOverallHintsShown(true);
        } else {
            if (mCheckLettersHintSelected) {
                closeHintCheckLettersPanel();
            }

            if (mOpenDelimitersHintSelected) {
                closeHintOpenDelimitersPanel();
            }

            if (mView != null) {
                mView.setSelectedSymbol(symbolId);
            }
            // Show keyboard.
            mSymbolId = symbolId;
            showViewKeyboardAndSymbolHintsControls();

            setOverallHintsShown(false);
        }
    }

    /* Constructs String that contains letters mapped to other symbols. */
    private String constructOccupiedLetters(int symbolId) {
        Map<Integer, Character> mapping = mCipherManager.getLetterMapping();
        StringBuilder sb = new StringBuilder();
        for (Integer id : mapping.keySet()) {
            if (id != symbolId) {
                sb.append(mapping.get(id));
            }
        }
        return sb.toString();
    }

    @Override
    public void onKeyboardLetterSelected(Character letter) {
        mCipherManager.setLetter(mSymbolId, letter);

        updateViewSymbols();
        closeKeyboardAndSymbolHints();

        setOverallHintsShown(true);

        checkIfJustSolved();
    }

    // Checks if the cipher was just solved.
    private void checkIfJustSolved() {
        mSolved = mCipherManager.checkIfSolved();
        if (mSolved) {
            if (mView != null) {
                mView.setSolved(true);
                mView.setDelimiterPositions(mCipherManager.getDelimiters());
            }

            updateViewSymbols();

            int level = mCipherManager.getCipherInfo().getShortInfo().getLevel();

            boolean levelIsSolved = mLevelsManager.isLevelSolved(level);

            mCoinsManager.addCoinsForSolvingCipher(level, levelIsSolved, mCipherManager.getCipherInfo().isDelimiterOpened());

            closeHintCheckLettersPanel();
            closeHintOpenDelimitersPanel();
            closeKeyboardAndSymbolHints();
            setOverallHintsShown(false);
        }
    }

    @Override
    public void onKeyboardCloseClicked() {
        closeKeyboardAndSymbolHints();
        setOverallHintsShown(true);
    }

    @Override
    public void onKeyboardEraseClicked() {
        onKeyboardLetterSelected(null);
    }

    @Override
    public void onBackClicked() {
        if (mCheckLettersHintSelected) {
            closeHintCheckLettersPanel();
            return;
        }
        if (mOpenDelimitersHintSelected) {
            closeHintCheckLettersPanel();
            return;
        }
        if (mOpenSymbolHintSelected) {
            closeHintSymbolPanel();
            return;
        }
        if (mSymbolId > -1) {
            closeKeyboardAndSymbolHints();
            return;
        }

        if (mView != null) mView.exit();
    }

    @Override
    public void onLevelsClicked() {
        if (mView != null) mView.exit();
    }

    // Show View's Open Button hint panel.
    private void showOpenSymbolHintPanel() {
        mOpenSymbolHintSelected = false;

        if (mSymbolId < 0) {
            return;
        }

        CoinsManager.Product openSymbol = CoinsManager.Product.OPEN_SYMBOL;

        int price = mCoinsManager.getPrice(openSymbol);

        if (mView != null) {
            if (mCipherManager.maxOpenedSymbols()) {
                mView.showHintSymbolMaxSymbolsOpened(mCipherManager.getOpenedSymbols().size());
            } else {
                if (!mCoinsManager.enoughCoins(openSymbol)) {
                    mView.showHintSymbolNotEnoughCoins(mCoinsManager.getCoins(), price);
                } else {
                    mView.showHintSymbolConfirmation(price);
                }
            }
        }
        mOpenSymbolHintSelected = true;
    }

    // Show View's Check letters hint panel.
    private void showHintCheckLettersPanel() {
        mCheckLettersHintSelected = false;

        if (mSymbolId >= 0) {
            closeKeyboardAndSymbolHints();
        }

        CoinsManager.Product checkLetters = CoinsManager.Product.CHECK_LETTERS;

        int lettersCount = mCipherManager.getLettersCount();
        int price = mCoinsManager.getPrice(checkLetters, lettersCount);
        if (mView != null) {
            if (lettersCount == 0) {
                mView.showHintCheckLettersNoLetters();
            } else {
                if (!mCoinsManager.enoughCoins(checkLetters, lettersCount)) {
                    mView.showHintCheckLettersNotEnoughCoins(mCoinsManager.getCoins(), price);
                } else {
                    mView.showHintCheckLettersConfirmation(price);
                }
            }
        }

        mCheckLettersHintSelected = true;
    }

    // Show View's Open delimiters hint panel.
    private void showHintOpenDelimitersPanel() {
        mOpenDelimitersHintSelected = false;

        if (mSymbolId >= 0) {
            closeKeyboardAndSymbolHints();
        }

        if (mView != null && !mCipherManager.getCipherInfo().isDelimiterOpened()) {
            mView.showHintOpenDelimitersConfirmation();
            mOpenDelimitersHintSelected = true;
        }
    }

    @Override
    public void onHintControlSymbolClicked() {
        if (mOpenSymbolHintSelected) {
            closeHintSymbolPanel();
        } else {
            showOpenSymbolHintPanel();
        }
    }

    @Override
    public void onHintControlCheckLettersClicked() {
        if (mCheckLettersHintSelected) {
            closeHintCheckLettersPanel();
        } else {
            if (mOpenDelimitersHintSelected) {
                closeHintOpenDelimitersPanel();
            }
            showHintCheckLettersPanel();
        }
    }

    @Override
    public void onHintControlOpenDelimitersClicked() {
        if (mOpenDelimitersHintSelected) {
            closeHintOpenDelimitersPanel();
        } else {
            if (mCheckLettersHintSelected) {
                closeHintCheckLettersPanel();
            }
            showHintOpenDelimitersPanel();
        }
    }

    @Override
    public void onHintCanceled() {
        if (mOpenSymbolHintSelected) {
            closeHintSymbolPanel();
        } else if (mCheckLettersHintSelected) {
            closeHintCheckLettersPanel();
        } else if (mOpenDelimitersHintSelected) {
            closeHintOpenDelimitersPanel();
        }
    }

    @Override
    public void onHintConfirmed() {
        if (mCheckLettersHintSelected) {
            int lettersCount = mCipherManager.checkLetters();
            mCoinsManager.spendCoins(CoinsManager.Product.CHECK_LETTERS, lettersCount);
            closeHintCheckLettersPanel();
            setOverallHintsShown(true);
        } else if (mOpenDelimitersHintSelected) {
            mCipherManager.openDelimiters();
            updateDelimiters();
            closeHintOpenDelimitersPanel();
            setOverallHintsShown(true);
        } else if (mOpenSymbolHintSelected) {
            if (mCipherManager.openSymbol(mSymbolId)) {
                mCoinsManager.spendCoins(CoinsManager.Product.OPEN_SYMBOL);
            }
            closeKeyboardAndSymbolHints();
        }

        updateViewSymbols();

        checkIfJustSolved();
    }

    @Override
    public void storeState(Bundle bundle) {
        Icepick.saveInstanceState(this, bundle);
    }

    @Override
    public void restoreState(Bundle bundle) {
        Icepick.restoreInstanceState(this, bundle);
    }
}
