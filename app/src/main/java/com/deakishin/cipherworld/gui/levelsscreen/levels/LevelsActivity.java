package com.deakishin.cipherworld.gui.levelsscreen.levels;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.deakishin.cipherworld.R;
import com.deakishin.cipherworld.app.CipherWorldApplication;
import com.deakishin.cipherworld.gui.AdVideoDisplayingActivity;
import com.deakishin.cipherworld.gui.CipherSwitchingActivity;
import com.deakishin.cipherworld.gui.ColorChangingActivity;
import com.deakishin.cipherworld.gui.OnBackPressedListener;
import com.deakishin.cipherworld.gui.cipherscreen.CipherFragment;
import com.deakishin.cipherworld.gui.coins.coinsdisplay.CoinsDisplayFragment;
import com.deakishin.cipherworld.presenters.ads.MvpAdVideoPresenter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity for the Levels screen.
 * <p>
 * Implements interface for changing background and status bar colors and going to a cipher's screen.
 * <p>
 * Also implements interface for displaying ad videos.
 */
public class LevelsActivity extends AppCompatActivity implements CipherSwitchingActivity,
        ColorChangingActivity, AdVideoDisplayingActivity {

    // Tag for Cipher fragment.
    private static final String CIPHER_FRAGMENT_TAG = "cipherFragment";

    // Widgets.
    @BindView(R.id.levels_screen_background_panel)
    View mBgPanel;

    // Object for unbinding views bound with ButterKnife.
    private Unbinder mButterknifeUnbinder;

    // Fragment that displays levels.
    private LevelsFragment mLevelsFragment;

    // Ad video.
    private RewardedVideoAd mAdVideo;
    @BindString(R.string.ad_video_unit_id)
    String mAdVideoUnitId;

    // MVP Presenter for ad videos.
    @Inject
    MvpAdVideoPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_and_coins);

        // Bind views.
        mButterknifeUnbinder = ButterKnife.bind(this);

        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.contentFragmentContainer) == null) {
            mLevelsFragment = new LevelsFragment();
            fm.beginTransaction().add(R.id.contentFragmentContainer, mLevelsFragment).commit();
        }

        Fragment coinsFragment = fm.findFragmentById(R.id.coinsFragmentContainer);
        if (coinsFragment == null) {
            coinsFragment = new CoinsDisplayFragment();
            fm.beginTransaction().add(R.id.coinsFragmentContainer, coinsFragment).commit();
        }

        // Inject dependencies.
        ((CipherWorldApplication) getApplication()).getAppComponent().inject(this);

        // Set up ad video.
        mAdVideo = MobileAds.getRewardedVideoAdInstance(this);
        mAdVideo.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
            }

            @Override
            public void onRewardedVideoAdOpened() {
            }

            @Override
            public void onRewardedVideoStarted() {
            }

            @Override
            public void onRewardedVideoAdClosed() {
                // Preload the next video ad.
                loadRewardedVideoAd();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                if (mPresenter != null) mPresenter.onVideoAdWatched();
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
            }
        });
        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd() {
        if (!mAdVideo.isLoaded()) {
            mAdVideo.loadAd(mAdVideoUnitId, new AdRequest.Builder().build());
        }
    }

    @Override
    public void onResume() {
        mAdVideo.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mAdVideo.pause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mButterknifeUnbinder.unbind();

        mAdVideo.destroy(this);

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

    @Override
    public void goToCipher(int cipherId) {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(CIPHER_FRAGMENT_TAG) != null) return;

        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.screen_alpha_in, 0, 0, R.anim.screen_alpha_out);
        ft.addToBackStack(null);
        ft.add(R.id.contentFragmentContainer, CipherFragment.getInstance(cipherId), CIPHER_FRAGMENT_TAG).commit();
    }

    @Override
    public void goToLevels() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contentFragmentContainer, new LevelsFragment()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contentFragmentContainer);
        if (fragment != null && fragment instanceof OnBackPressedListener) {
            ((OnBackPressedListener) fragment).onBackPressed();
        } else {
            finish();
        }
    }

    @Override
    public boolean showAdVideo() {
        if (mAdVideo != null && mAdVideo.isLoaded()) {
            mAdVideo.show();
            return true;
        }
        return false;
    }
}
