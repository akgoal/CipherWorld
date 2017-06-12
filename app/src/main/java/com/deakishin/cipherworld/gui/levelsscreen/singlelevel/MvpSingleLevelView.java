package com.deakishin.cipherworld.gui.levelsscreen.singlelevel;

import com.deakishin.cipherworld.model.cipherstorage.CipherShortInfo;

import java.util.List;

/**
 * Interface for a view that displays ciphers of a single level.
 */
public interface MvpSingleLevelView {

    /**
     * Sets ciphers to display.
     *
     * @param ciphers List of info objects about ciphers on the level.
     */
    void setCiphersInfo(List<CipherShortInfo> ciphers);

    /**
     * Goes to a specific cipher's view.
     *
     * @param cipherId Cipher's id.
     */
    void goToCipher(int cipherId);

    /**
     * Sets/removes lock on the level.
     *
     * @param locked True if the level is locked, false otherwise.
     * @param ciphersToSolve Number of ciphers to solve in order to unlock the level.
     */
    void setLocked(boolean locked, int ciphersToSolve);
}
