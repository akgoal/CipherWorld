package com.deakishin.cipherworld.model.cipherstorage;

/**
 * Information about a cipher. Contains its short info in a {@link CipherShortInfo} object, plus:
 * its question and solution, a string that contains opened letters for the cipher,
 * a string that represents current solution (i.e. current result of decrypting),
 * and a flag that indicates whether delimiters have been opened for the cipher.
 */
public class CipherInfo {
    private CipherShortInfo mShortInfo;
    private String mQuestion;
    private String mSolution;

    private String mOpenedLetters;

    private String mCurrentSolution;

    private boolean mDelimiterOpened = false;

    public CipherInfo() {
    }

    // Getters-setters.

    public CipherShortInfo getShortInfo() {
        return mShortInfo;
    }

    public void setShortInfo(CipherShortInfo shortInfo) {
        mShortInfo = shortInfo;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }

    public String getSolution() {
        return mSolution;
    }

    public void setSolution(String solution) {
        mSolution = solution;
    }

    public String getOpenedLetters() {
        return mOpenedLetters;
    }

    public void setOpenedLetters(String openedLetters) {
        mOpenedLetters = openedLetters;
    }

    public String getCurrentSolution() {
        return mCurrentSolution;
    }

    public void setCurrentSolution(String currentSolution) {
        mCurrentSolution = currentSolution;
    }

    public boolean isDelimiterOpened() {
        return mDelimiterOpened;
    }

    public void setDelimiterOpened(boolean delimiterOpened) {
        mDelimiterOpened = delimiterOpened;
    }
}
