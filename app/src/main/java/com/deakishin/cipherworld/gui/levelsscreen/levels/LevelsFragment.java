package com.deakishin.cipherworld.gui.levelsscreen.levels;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.deakishin.cipherworld.R;
import com.deakishin.cipherworld.app.CipherWorldApplication;
import com.deakishin.cipherworld.gui.ColorChangingActivity;
import com.deakishin.cipherworld.gui.LevelColorHelper;
import com.deakishin.cipherworld.gui.levelsscreen.singlelevel.SingleLevelFragment;
import com.deakishin.cipherworld.presenters.MvpLevelsPresenter;
import com.deakishin.cipherworld.presenters.ads.MvpAdBannerPresenter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Fragment for displaying levels. Implements View interface for MVP pattern.
 * <p>
 * If the activity that hosts this fragment, wants to dynamically update its
 * background and status bar colors depending on the current level,
 * it must implement {@link ColorChangingActivity}.
 */
public class LevelsFragment extends Fragment implements MvpLevelsView {

    // Determines if paging between levels is enabled.
    private static final boolean PAGING_ENABLED = true;
    // Factor by which the ViewPager standard scrolling duration is changed.
    private static final double SCROLL_DURATION_FACTOR = 4;

    // Widgets.
    @BindView(R.id.levels_screen_level_title_textView)
    TextView mLevelTitleTextView;
    @BindView(R.id.levels_screen_prev_level_imageButton)
    ImageButton mPrevLevelImageButton;
    @BindView(R.id.levels_screen_next_level_imageButton)
    ImageButton mNextLevelImageButton;
    @BindView(R.id.level_ciphers_viewPager)
    CustomViewPager mViewPager;

    @BindView(R.id.ad_banner_view)
    AdView mAdBannerView;

    @BindString(R.string.level_num)
    String mLevelTitle;

    // Adapter for view pager.
    private FragmentPagerAdapter mPagerAdapter;

    // Helper for getting background and status bar colors for different levels.
    private LevelColorHelper mLevelColorHelper;

    // MVP Presenter for levels info.
    @Inject
    MvpLevelsPresenter mPresenter;

    // MVP Presenter for ads.
    @Inject
    MvpAdBannerPresenter mAdBannerPresenter;

    // Object for animating color changes.
    private ArgbEvaluator mArgbEvaluator = new ArgbEvaluator();

    // Object for unbinding views bound with ButterKnife.
    private Unbinder mButterknifeUnbinder;

    public LevelsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLevelColorHelper = LevelColorHelper.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.levels_fragment, parent, false);

        // Bind views.
        mButterknifeUnbinder = ButterKnife.bind(this, view);

        // Inject dependencies.
        ((CipherWorldApplication) getActivity().getApplication()).getAppComponent().inject(this);

        mPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return SingleLevelFragment.getInstance(position + 1);
            }

            @Override
            public int getCount() {
                return mPresenter.getLevelCount();
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setPagingEnabled(PAGING_ENABLED);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Animate background color changes.
                int bgColor, statusBarColor;
                if (position < (mViewPager.getAdapter().getCount() - 1)) {
                    bgColor =
                            (Integer) mArgbEvaluator.evaluate(positionOffset,
                                    mLevelColorHelper.getBackgroundColor(position),
                                    mLevelColorHelper.getBackgroundColor(position + 1));
                    statusBarColor =
                            (Integer) mArgbEvaluator.evaluate(positionOffset,
                                    mLevelColorHelper.getStatusBarColor(position),
                                    mLevelColorHelper.getStatusBarColor(position + 1));
                } else {
                    bgColor = mLevelColorHelper.getBackgroundColor(position);
                    statusBarColor = mLevelColorHelper.getStatusBarColor(position);
                }

                changeColors(bgColor, statusBarColor);
            }

            @Override
            public void onPageSelected(int position) {
                mPresenter.onLevelSelected(position + 1);
                //mBackgroundView.setBackgroundColor(mLevelColorHelper.getBackgroundColor(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setScrollDurationFactor(SCROLL_DURATION_FACTOR);

        // Bind presenter
        mPresenter.bindView(this);


        // Load ad banner.
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdBannerView.setVisibility(View.GONE);
        mAdBannerView.loadAd(adRequest);
        mAdBannerView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mAdBannerView != null) {
                    mAdBannerView.setVisibility(View.VISIBLE);
                    mAdBannerView.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.ad_banner_slide_in_up));
                }
            }

            @Override
            public void onAdOpened() {
                if (mAdBannerPresenter != null) mAdBannerPresenter.onBannerAdOpened();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        // Unbind presenter to prevent leaks.
        mPresenter.unbindView();

        // Unbind views.
        mButterknifeUnbinder.unbind();

        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // Changes activity's colors, if it implements right interface.
    private void changeColors(int bgColor, int statusBarColor) {
        if (getActivity() instanceof ColorChangingActivity) {
            ((ColorChangingActivity) getActivity()).changeColors(bgColor, statusBarColor);
        }
    }

    @OnClick(R.id.levels_screen_prev_level_imageButton)
    public void onPrevLevelClicked() {
        mPresenter.onPrevLevelClicked();
    }

    @OnClick(R.id.levels_screen_next_level_imageButton)
    public void onNextLevelClicked() {
        mPresenter.onNextLevelClicked();
    }

    // MVP View methods.
    @Override
    public void setPrevLevelButtonEnabled(boolean enabled) {
        mPrevLevelImageButton.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setNextLevelButtonEnabled(boolean enabled) {
        mNextLevelImageButton.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setLevel(int levelNumber) {
        mLevelTitleTextView.setText(String.format(mLevelTitle, levelNumber));

        //mBackgroundView.setBackgroundColor(mLevelColorHelper.getBackgroundColor(levelNumber - 1));

        mViewPager.setCurrentItem(levelNumber - 1);
    }

    @Override
    public void goToNextLevel() {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);

    }

    @Override
    public void goToPrevLevel() {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
    }
}