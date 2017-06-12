package com.deakishin.cipherworld.gui.coins.coinsdisplay;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.deakishin.cipherworld.R;
import com.deakishin.cipherworld.app.CipherWorldApplication;
import com.deakishin.cipherworld.gui.coins.coinsoptions.CoinsOptionsDialogFragment;
import com.deakishin.cipherworld.presenters.coins.MvpCoinsDisplayPresenter;

import javax.inject.Inject;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Fragment for displaying coins info.
 */
public class CoinsDisplayFragment extends Fragment implements MvpCoinsDisplayView {

    // Identifiers for dialogs.
    private static final String DIALOG_COINS_OPTIONS = "dialogCoinsOptions";

    // Widgets.
    @BindView(R.id.coins_display_panel)
    View mPanel;
    @BindView(R.id.coins_display_textView)
    TextView mTextView;

    // Object for unbinding views bound with ButterKnife.
    private Unbinder mButterknifeUnbinder;

    // MVP Presenter.
    @Inject
    MvpCoinsDisplayPresenter mPresenter;

    // Duration of animations of changes.
    @BindInt(R.integer.coins_display_change_anim_dur)
    int mAnimationDuration;

    // Last number of coins. Needed for animated changes.
    private int mLastCoins = -1;

    // Object for animating changes.
    private ValueAnimator mChangesAnimator;

    public CoinsDisplayFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.coins_display_fragment, parent, false);

        // Bind views.
        mButterknifeUnbinder = ButterKnife.bind(this, view);

        // Inject dependencies.
        ((CipherWorldApplication) getActivity().getApplication()).getAppComponent().inject(this);

        mPresenter.bindView(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        stopAnimations();

        // Unbind presenter to prevent leaks.
        mPresenter.unbindView();

        // Unbind views.
        mButterknifeUnbinder.unbind();

        super.onDestroyView();
    }

    @OnClick(R.id.coins_display_panel)
    public void onPanelClicked() {
        mPresenter.onCoinsClicked();
    }

    // Sets number of coins as text.
    private void setCoinsText(int coins) {
        String text = Integer.toString(coins);
        mTextView.setText(text);
    }

    // Stops all animations.
    private void stopAnimations() {
        if (mChangesAnimator != null) {
            mChangesAnimator.cancel();
        }
    }

    @Override
    public void setCoins(int coins) {
        if (mLastCoins < 0) {
            setCoinsText(coins);
            mLastCoins = coins;
        } else {

            // Making changes animated.

            stopAnimations();

            mChangesAnimator = ValueAnimator.ofInt(mLastCoins, coins);
            mChangesAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();

                    setCoinsText(value);
                }
            });

            mChangesAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    setCoinsText(mLastCoins);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    setCoinsText(mLastCoins);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });

            mChangesAnimator.setInterpolator(new LinearInterpolator());
            mChangesAnimator.setDuration(mAnimationDuration);

            mChangesAnimator.start();

            mLastCoins = coins;
        }
    }

    @Override
    public void showCoinsOptions() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        if (fm.findFragmentByTag(DIALOG_COINS_OPTIONS) != null) {
            return;
        }

        CoinsOptionsDialogFragment dialog = new CoinsOptionsDialogFragment();
        dialog.show(fm, DIALOG_COINS_OPTIONS);
    }
}
