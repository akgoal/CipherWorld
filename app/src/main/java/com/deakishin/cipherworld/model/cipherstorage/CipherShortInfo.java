package com.deakishin.cipherworld.model.cipherstorage;

/**
 * Short information about a cipher: its id, number, level number, and whether it's solved or not.
 */
public class CipherShortInfo {
    private int mId;
    private int mNumber;
    private int mLevel;
    private boolean mSolved;

    public CipherShortInfo() {
    }

    // Getters-setters.

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int number) {
        mNumber = number;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int level) {
        mLevel = level;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    @Override
    public String toString() {
        return "CipherShortInfo{" +
                "mId=" + mId +
                ", mNumber=" + mNumber +
                ", mLevel=" + mLevel +
                ", mSolved=" + mSolved +
                '}';
    }
}
