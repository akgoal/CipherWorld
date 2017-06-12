package com.deakishin.cipherworld.gui.loadingscreen;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.deakishin.cipherworld.R;
import com.deakishin.cipherworld.app.CipherWorldApplication;
import com.deakishin.cipherworld.gui.levelsscreen.levels.LevelsActivity;
import com.deakishin.cipherworld.presenters.loading.MvpLoadingPresenter;

import java.util.Date;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment for displaying loading.
 * <p>
 * Implements MVP View interface.
 */
public class LoadingFragment extends Fragment implements MvpLoadingView {

    // MVP Presenter.
    @Inject
    MvpLoadingPresenter mPresenter;

    // Object for unbinding views bound with ButterKnife.
    private Unbinder mButterknifeUnbinder;

    // Widgets.
    @BindView(R.id.loading_screen_title_recyclerView)
    RecyclerView mTitleRecyclerView;
    @BindView(R.id.loading_screen_loading_recyclerView)
    RecyclerView mLoadingRecyclerView;

    @BindInt(R.integer.loading_title_grid_row_padding)
    int mTitleRowPadding;
    @BindInt(R.integer.loading_title_grid_row_right_offset)
    int mTitleRightRowOffset;
    @BindInt(R.integer.loading_loading_grid_row_padding)
    int mLoadingRowOffset;
    @BindString(R.string.loading_title_left)
    String mTitleLeft;
    @BindString(R.string.loading_title_right)
    String mTitleRight;
    @BindString(R.string.loading_loading)
    String mLoadingText;
    @BindInt(R.integer.loading_letter_turn_over_anim_dur)
    int mLetterAnimDur;
    @BindInt(R.integer.loading_letter_turn_over_anim_offset)
    int mLetterAnimOffset;

    @BindColor(R.color.loadingStatusBar)
    int mStatusBarColor;

    // Flsgs indicating whether delay and loading are complete or not.
    private boolean mDelayDone = false, mLoadingDone = false;

    // Handler for executing delay.
    private Handler mDelayHandler;

    @BindInt(R.integer.loading_screen_delay)
    int mLoadingDelay;

    // Key for storing delay in the Bundle.
    private static final String EXTRA_DELAY = "delay";
    // How many milliseconds passed since delay started.
    private long mDelayPast = 0;
    // Time in milliseconds when the delay started.
    private long mDelayStartedTime = 0;

    public LoadingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = layoutInflater.inflate(R.layout.loading_fragment, parent, false);

        mButterknifeUnbinder = ButterKnife.bind(this, v);

        // Configure Title RecyclerView.
        mTitleRecyclerView.setHasFixedSize(true);
        TitleAdapter titleAdapter = new TitleAdapter(mTitleLeft, mTitleRight, mTitleRightRowOffset, mTitleRowPadding);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), titleAdapter.getRowLength());
        mTitleRecyclerView.setLayoutManager(gridLayoutManager);
        mTitleRecyclerView.setAdapter(titleAdapter);

        // Configure Loading RecyclerView.
        mLoadingRecyclerView.setHasFixedSize(true);
        LoadingTextAdapter loadingAdapter = new LoadingTextAdapter(mLoadingText,
                mLoadingRowOffset, mLetterAnimDur, mLetterAnimOffset);
        GridLayoutManager loadingGridLayoutManager = new GridLayoutManager(getActivity(), loadingAdapter.getItemCount());
        mLoadingRecyclerView.setLayoutManager(loadingGridLayoutManager);
        mLoadingRecyclerView.setAdapter(loadingAdapter);

        ((CipherWorldApplication) getActivity().getApplication()).getAppComponent().inject(this);

        changeStatusBarColor(mStatusBarColor);

        mPresenter.bindView(this);

        if (savedInstanceState != null) {
            mDelayPast = savedInstanceState.getLong(EXTRA_DELAY, 0);
        }
        if (mDelayPast > mLoadingDelay) {
            mDelayPast = 0;
        }

        return v;
    }

    @Override
    public void onDestroyView() {
        stopDelay();
        mPresenter.unbindView();
        mButterknifeUnbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        startDelay();
    }

    @Override
    public void onPause() {
        stopDelay();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(EXTRA_DELAY, mDelayPast);

        super.onSaveInstanceState(outState);
    }

    // Changes status bar color (if possible).
    private void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    @Override
    public void goToLevels() {
        mLoadingDone = true;
        onLoaded();
    }

    // Invoked when loading is complete.
    private void onLoaded() {
        if (mDelayDone && mLoadingDone) {
            Intent i = new Intent(getActivity(), LevelsActivity.class);
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.screen_slide_in_left, R.anim.screen_hold);
        }
    }

    // Starts delay.
    private void startDelay() {
        mDelayHandler = new Handler();
        mDelayStartedTime = new Date().getTime();
        mDelayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDelayDone = true;
                onLoaded();
            }
        }, mLoadingDelay - mDelayPast);
    }

    // Stops delay.
    private void stopDelay() {
        mDelayPast = new Date().getTime() - mDelayStartedTime;
        mDelayHandler.removeCallbacksAndMessages(null);
    }
}
