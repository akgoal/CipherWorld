package com.deakishin.cipherworld.gui.cipherscreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.deakishin.cipherworld.gui.CoinsAndContentActivity;

/**
 * Activity for displaying a cipher.
 */
public class CipherActivity extends CoinsAndContentActivity {

    /**
     * Extra for passing cipher's id as an extra to intent.
     */
    public static final String EXTRA_CIPHER_ID = "com.deakishin.cipherworld.gui.cipherscreen.CipherActivity.cipherId";

    // Hosted cipher fragment.
    private CipherFragment mFragment;

    // Id of the displayed cipher.
    private int mCipherId;

    @Override
    protected Fragment getContentFragment() {
        mFragment = CipherFragment.getInstance(mCipherId);
        return mFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCipherId = 0;
        if (getIntent() != null) {
            mCipherId = getIntent().getIntExtra(EXTRA_CIPHER_ID, 0);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (mFragment != null) {
            mFragment.onBackPressed();
        }
    }
}
