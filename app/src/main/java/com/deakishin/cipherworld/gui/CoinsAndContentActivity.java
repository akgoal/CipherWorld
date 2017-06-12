package com.deakishin.cipherworld.gui;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.deakishin.cipherworld.R;
import com.deakishin.cipherworld.gui.coins.coinsdisplay.CoinsDisplayFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Abstract activity for displaying a fragment with coins and a custom fragment with other content.
 * <p>
 * Implements interface for color changing.
 */
public abstract class CoinsAndContentActivity extends AppCompatActivity implements ColorChangingActivity {

    // Widgets.
    @BindView(R.id.levels_screen_background_panel)
    View mBgPanel;

    // Object for unbinding views bound with ButterKnife.
    private Unbinder mButterknifeUnbinder;

    /**
     * @return Fragment with content.
     */
    protected abstract Fragment getContentFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_and_coins);

        // Bind views.
        mButterknifeUnbinder = ButterKnife.bind(this);

        FragmentManager fm = getSupportFragmentManager();
        Fragment contentFragment = fm.findFragmentById(R.id.contentFragmentContainer);
        if (contentFragment == null) {
            contentFragment = getContentFragment();
            fm.beginTransaction().add(R.id.contentFragmentContainer, contentFragment).commit();
        }

        Fragment coinsFragment = fm.findFragmentById(R.id.coinsFragmentContainer);
        if (coinsFragment == null) {
            coinsFragment = new CoinsDisplayFragment();
            fm.beginTransaction().add(R.id.coinsFragmentContainer, coinsFragment).commit();
        }
    }

    @Override
    protected void onDestroy() {
        mButterknifeUnbinder.unbind();

        super.onDestroy();
    }

    // Changes status bar color (if possible).
    private void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    @Override
    public void changeColors(int backgroundColor, int statusBarColor) {
        mBgPanel.setBackgroundColor(backgroundColor);
        changeStatusBarColor(statusBarColor);
    }
}
