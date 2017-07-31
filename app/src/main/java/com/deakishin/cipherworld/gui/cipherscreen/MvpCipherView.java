package com.deakishin.cipherworld.gui.cipherscreen;

import com.deakishin.cipherworld.model.ciphergenerator.CipherSymbol;

import java.util.List;
import java.util.Map;

/**
 * Interface for a view that displays a cipher.
 */
public interface MvpCipherView {

    /**
     * Tells the view whether changes has to be animated or not.
     *
     * @param toAnimate True if changes must be animated, false otherwise.
     */
    void setAnimateChanges(boolean toAnimate);

    /**
     * Sets cipher's basic info.
     *
     * @param level  Number of the level that the cipher belongs to.
     * @param number Number of the cipher in the level.
     */
    void setCipherData(int level, int number);

    /**
     * Sets cipher's question.
     *
     * @param question Question to set.
     */
    void setCipherQuestion(String question);

    /**
     * Marks the cipher as solved/unsolved.
     *
     * @param solved True if the cipher is solved, false otherwise.
     */
    void setSolved(boolean solved);

    /**
     * Sets symbols to display.
     *
     * @param symbols List of symbols to display.
     */
    void setSymbols(List<CipherSymbol> symbols);

    /**
     * Sets positions in the list of symbols after which delimiters must be shown.
     *
     * @param delimiterPositions List of positions. Null if delimiters must not be shown.
     */
    void setDelimiterPositions(List<Integer> delimiterPositions);

    /**
     * Sets letters that must replace symbols.
     *
     * @param letters Mapping between symbols' ids and letters replacing the symbols.
     */
    void setLetters(Map<Integer, Character> letters);

    /**
     * Sets opened symbols. These symbols must be marked as opened.
     *
     * @param openedSymbols List of ids of opened symbols.
     */
    void setOpenedLetters(List<Integer> openedSymbols);

    /**
     * Sets selected symbol.
     *
     * @param symbolId Id of the symbol to select.
     */
    void setSelectedSymbol(int symbolId);

    /**
     * Shows keyboard.
     *
     * @param occupiedLetters    String that contains occupied letters.
     * @param unavailableLetters String that contains letters that can not be selected.
     * @param currentLetter      Current letter for the symbol.
     */
    void showKeyboard(String occupiedLetters, String unavailableLetters, Character currentLetter);

    /**
     * Hides keyboard.
     */
    void hideKeyboard();


    /**
     * Shows/hides the Hint symbol button.
     *
     * @param toShow True if the button must be shown,
     *               false if it must be hidden.
     */
    void setHintSymbolShown(boolean toShow);

    /**
     * Shows confirmation to use the Open symbol hint.
     *
     * @param priceInCoins Number of coins that will be spend if the hint is used.
     */
    void showHintSymbolConfirmation(int priceInCoins);

    /**
     * Shows message about not enough coins to use the Open symbol hint.
     *
     * @param coins     Current number of coins.
     * @param hintPrice Price for the hint.
     */
    void showHintSymbolNotEnoughCoins(int coins, int hintPrice);

    /**
     * Shows message saying that maximum number of symbol has been already opened.
     *
     * @param openedSymbols Number of opened symbols.
     */
    void showHintSymbolMaxSymbolsOpened(int openedSymbols);

    /**
     * Closes panel with the Open symbol hint options.
     */
    void closeHintSymbolPanel();


    /**
     * Shows/hides the Check letters hint button.
     *
     * @param toShow True if the button must be shown,
     *               false if it must be hidden.
     */
    void setHintCheckLettersShown(boolean toShow);

    /**
     * Shows confirmation to use the Check letters hint.
     *
     * @param priceInCoins Number of coins that will be spend if the hint is used.
     */
    void showHintCheckLettersConfirmation(int priceInCoins);

    /**
     * Shows message about not enough coins to use the Check letters hint.
     *
     * @param coins     Current number of coins.
     * @param hintPrice Price for the hint.
     */
    void showHintCheckLettersNotEnoughCoins(int coins, int hintPrice);

    /**
     * Shows message saying that there is no letters to check.
     */
    void showHintCheckLettersNoLetters();

    /**
     * Closes panel with the Check letters hint options.
     */
    void closeHintCheckLettersPanel();


    /**
     * Shows/hides the Open delimiters hint button.
     *
     * @param toShow True if the button must be shown,
     *               false if it must be hidden.
     */
    void setHintOpenDelimitersShown(boolean toShow);

    /**
     * Shows confirmation to use the Open delimiters hint.
     */
    void showHintOpenDelimitersConfirmation();

    /**
     * Closes panel with the Open delimiters hint options.
     */
    void closeHintOpenDelimitersPanel();

    /**
     * Exits the view.
     */
    void exit();
}
