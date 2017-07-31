package com.deakishin.cipherworld.presenters;

import android.os.Bundle;

import com.deakishin.cipherworld.gui.cipherscreen.MvpCipherView;

/**
 * MVP Presenter interface for working with a cipher.
 */
public interface MvpCipherPresenter {

    /**
     * Binds MVP View to the Presenter.
     *
     * @param view     View to bind.
     * @param cipherId Id of the cipher to work with.
     */
    void bindView(MvpCipherView view, int cipherId);

    /**
     * Unbinds view.
     */
    void unbindView();

    /**
     * Invoked when the view is paused.
     */
    void onPaused();

    /**
     * Invoked when a symbol is clicked on.
     *
     * @param symbolId Symbol's id.
     */
    void onSymbolClicked(int symbolId);

    /**
     * Invoked when a letter is selected on the keyboard.
     *
     * @param letter Selected letter.
     */
    void onKeyboardLetterSelected(Character letter);

    /**
     * Invoked when the keyboard is set to be closed.
     */
    void onKeyboardCloseClicked();

    /**
     * Invoked when an option to erase letter is selected on the keyboard.
     */
    void onKeyboardEraseClicked();

    /**
     * Invoked when the Back button is clicked.
     */
    void onBackClicked();

    /**
     * Invoked when the Levels button is clicked.
     */
    void onLevelsClicked();

    /**
     * Invoked when the Open symbol hint is selected.
     */
    void onHintControlSymbolClicked();

    /**
     * Invoked when the Check letters hint is selected.
     */
    void onHintControlCheckLettersClicked();

    /**
     * Invoked when the Open delimiters hint is selected.
     */
    void onHintControlOpenDelimitersClicked();

    /**
     * Invoked when the hint is canceled.
     */
    void onHintCanceled();

    /**
     * Invoked when the hint is confirmed.
     */
    void onHintConfirmed();

    /**
     * Saves presenter's state in the given Bundle object.
     *
     * @param bundle Bundle to save state to.
     */
    void storeState(Bundle bundle);

    /**
     * Loads presenter's state from the given Bundle object.
     *
     * @param bundle Bundle to load state from.
     */
    void restoreState(Bundle bundle);
}
