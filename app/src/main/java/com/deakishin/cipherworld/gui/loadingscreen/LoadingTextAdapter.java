package com.deakishin.cipherworld.gui.loadingscreen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deakishin.cipherworld.R;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter that provides items for loading animation.
 */
class LoadingTextAdapter extends RecyclerView.Adapter<LoadingTextAdapter.ViewHolder> {

    // Params.
    private String mLoadingText;
    private int mRowPadding;

    // Number of items.
    private int mLength;

    // Animations params.
    private int mLetterAnimDur;
    private int mLetterAnimOffset;

    /**
     * Constructs adapter.
     *
     * @param loadingText      Loading text to display.
     * @param rowPadding       Padding of the row, i.e. number of empty items on the both sides.
     * @param letterAnimDur    Duration of a letter turning over animation (in milliseconds).
     * @param letterAnimOffset Milliseconds that have to pass between animating next letter.
     */
    LoadingTextAdapter(String loadingText, int rowPadding,
                       int letterAnimDur, int letterAnimOffset) {
        mLoadingText = loadingText;
        mRowPadding = rowPadding;

        mLetterAnimDur = letterAnimDur;
        mLetterAnimOffset = letterAnimOffset;

        int len = mLoadingText == null ? 0 : mLoadingText.length();
        mLength = len + mRowPadding + mRowPadding;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.loading_letter_textView)
        TextView mItemTextView;

        @BindColor(R.color.loadingLoadingItemTextOne)
        int mNormalTextColor;
        @BindColor(R.color.loadingLoadingItemTextTwo)
        int mTurnedOverTextColor;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        /**
         * Sets background and text colors fot the item.
         *
         * @param turnedOver True if the item is turned over, false otherwise.
         */
        void setColors(boolean turnedOver) {
            mItemTextView.setBackgroundResource(
                    !turnedOver ? R.drawable.cipher_symbol_bg : R.drawable.cipher_symbol_letter_bg_opened);
            mItemTextView.setTextColor(!turnedOver ? mNormalTextColor : mTurnedOverTextColor);
        }
    }

    @Override
    public LoadingTextAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.loading_loading_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final LoadingTextAdapter.ViewHolder holder, int position) {
        Character letter = null;
        int textLen = mLoadingText == null ? 0 : mLoadingText.length();
        int posInText = -1;
        if (position >= mRowPadding && position < textLen + mRowPadding) {
            posInText = position - mRowPadding;
            if (posInText < textLen) {
                letter = mLoadingText.charAt(posInText);
            }
        }

        if (letter == null || posInText < 0) {
            holder.mItemTextView.setVisibility(View.INVISIBLE);
        } else {
            holder.mItemTextView.setVisibility(View.VISIBLE);
            holder.mItemTextView.setText(letter.toString());

            holder.setColors(false);

            // Start turning-over animation.
            final ValueAnimator letterAnimator = ValueAnimator.ofFloat(-1, 1);

            letterAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                private boolean turnedOver = false; // Letter is turned over.

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();

                    if (value > 1 || value < -1) {
                        return;
                    }

                    if (value > 0 && !turnedOver) {
                        holder.setColors(true);
                        turnedOver = true;
                    } else {
                        if (value < 0 && turnedOver) {
                            holder.setColors(false);
                            turnedOver = false;
                        }
                    }

                    holder.mItemTextView.setScaleX(Math.abs(value));
                }
            });
            letterAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                }
            });

            letterAnimator.setDuration(mLetterAnimDur);
            letterAnimator.setRepeatMode(ValueAnimator.REVERSE);
            letterAnimator.setRepeatCount(ValueAnimator.INFINITE);
            int delay = mLetterAnimOffset * posInText;
            letterAnimator.setStartDelay(delay);
            letterAnimator.start();
        }
    }

    @Override
    public int getItemCount() {
        return mLength;
    }
}
