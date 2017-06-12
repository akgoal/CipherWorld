package com.deakishin.cipherworld.gui.coins.coinsoptions;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deakishin.cipherworld.R;
import com.deakishin.cipherworld.app.CipherWorldApplication;
import com.deakishin.cipherworld.gui.AdVideoDisplayingActivity;
import com.deakishin.cipherworld.presenters.coins.MvpCoinsOptionsPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Dialog fragment for displaying coins options.
 */
public class CoinsOptionsDialogFragment extends DialogFragment implements MvpCoinsOptionsView {

    // Widgets.
    @BindView(R.id.coins_option_clicking_ad_reward_textView)
    TextView mClickAdRewardTextView;
    @BindView(R.id.coins_option_watching_ad_reward_textView)
    TextView mWatchAdRewardTextView;
    @BindView(R.id.coins_option_watching_ad_button)
    TextView mWatchAdButton;

    @BindView(R.id.coins_option_cancel_button)
    TextView mCancelButton;

    @BindView(R.id.coins_options_watching_ad_error_view)
    TextView mWatchAdErrorView;

    // Object for unbinding views bound with ButterKnife.
    private Unbinder mButterknifeUnbinder;

    // MVP Presenter.
    @Inject
    MvpCoinsOptionsPresenter mPresenter;

    public CoinsOptionsDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.coins_options_dialog_fragment, null);

        // Bind views.
        mButterknifeUnbinder = ButterKnife.bind(this, view);

        // Inject dependencies.
        ((CipherWorldApplication) getActivity().getApplication()).getAppComponent().inject(this);

        mPresenter.bindView(this);

        Dialog dialog = new AlertDialog.Builder(getActivity()).setView(view).create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
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
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);

        // Adding custom style for dialog animation.
        if (getDialog() != null && getDialog().getWindow() != null
                && getDialog().getWindow().getAttributes() != null) {
            getDialog().getWindow()
                    .getAttributes().windowAnimations = R.style.CoinsDialog;
        }
    }

    @OnClick(R.id.coins_option_watching_ad_button)
    void onWatchAdClicked() {
        mPresenter.onWatchAdClicked();
    }

    @OnClick(R.id.coins_option_cancel_button)
    void onCancelClicked() {
        mPresenter.onCancelClicked();
    }

    @Override
    public void setCoinsForAdClicking(int coins) {
        String text = Integer.toString(coins);
        mClickAdRewardTextView.setText(text);
    }

    @Override
    public void setCoinsForAdWatching(int coins) {
        String text = Integer.toString(coins);
        mWatchAdRewardTextView.setText(text);
    }

    @Override
    public void showAdVideo() {
        // Check if the host activity can diaply an ad video
        // and if so, tell it to do so.
        if (getActivity() instanceof AdVideoDisplayingActivity) {
            boolean success = ((AdVideoDisplayingActivity) getActivity()).showAdVideo();
            mPresenter.onAdVideoDisplayed(success);
        }
    }

    @Override
    public void setAdVideoErrorShown(boolean toShow) {
        mWatchAdErrorView.setVisibility(toShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void exit() {
        dismiss();
    }
}
