package com.deakishin.cipherworld.gui.loadingscreen;

import android.support.v4.app.Fragment;

import com.deakishin.cipherworld.gui.SingleFragmentActivity;

/** Activity for displaying loading screen. */
public class LoadingActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new LoadingFragment();
    }
}
