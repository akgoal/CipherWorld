package com.deakishin.cipherworld.gui;

/**
 * Interface for an activity that can organize switching between cipher screen and levels screen.
 */
public interface CipherSwitchingActivity {
    /**
     * Switches to a cipher screen.
     *
     * @param cipherId Cipher's id.
     */
    void goToCipher(int cipherId);

    /**
     * Switches to levels screen.
     */
    void goToLevels();
}
