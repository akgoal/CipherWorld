package com.deakishin.cipherworld.gui.loadingscreen;

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
 * Adapter that provides items for title.
 */
class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.ViewHolder> {

    // Params.
    private String mTitleOne;
    private String mTitleTwo;
    private int mTitleTwoOffset;
    private int mRowPadding;

    // Length of each "row".
    // (Each "row" contains one part of the title).
    private int mRowLength;

    /**
     * Constructs adapter.
     *
     * @param titleOne       First part of the title. This part is placed on the first "row".
     * @param titleTwo       Second part of the title. This part is placed on the second "row".
     * @param titleTwoOffset Offset of the second part, i.e. number of items skipped on the second "row*.
     * @param rowPadding     Padding of the rows, i.e. number of empty items on the both sides
     *                       of the "rows".
     */
    TitleAdapter(String titleOne, String titleTwo, int titleTwoOffset, int rowPadding) {
        mTitleOne = titleOne;
        mTitleTwo = titleTwo;
        mTitleTwoOffset = titleTwoOffset;
        mRowPadding = rowPadding;

        int firstLen = mTitleOne == null ? 0 : mTitleOne.length();
        int secondLen = mTitleTwoOffset + (mTitleTwo == null ? 0 : mTitleTwo.length());
        mRowLength = Math.max(firstLen, secondLen) + mRowPadding + mRowPadding;
    }

    /**
     * @return Number of items in a "row".
     */
    int getRowLength() {
        return mRowLength;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.loading_letter_textView)
        TextView mItemTextView;

        @BindColor(R.color.loadingTitleItemTextOne)
        int mTitleOneTextColor;
        @BindColor(R.color.loadingTitleItemTextTwo)
        int mTitleTwoTextColor;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        /**
         * Sets background and text colors fot the item.
         *
         * @param firstRow True if the item belongs to the first row, false otherwise.
         */
        void setColors(boolean firstRow) {
            mItemTextView.setBackgroundResource(
                    firstRow ? R.drawable.cipher_symbol_bg : R.drawable.cipher_symbol_letter_bg);
            mItemTextView.setTextColor(firstRow ? mTitleOneTextColor : mTitleTwoTextColor);
        }
    }

    @Override
    public TitleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.loading_title_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TitleAdapter.ViewHolder holder, int position) {
        boolean firstRow = position < mRowLength;
        Character letter = null;
        String titlePart = firstRow ? mTitleOne : mTitleTwo;
        int pos = firstRow ? position : position - mRowLength;
        int offset = firstRow ? 0 : mTitleTwoOffset;

        int titleLen = titlePart == null ? 0 : titlePart.length();
        if (pos < mRowPadding + offset || pos >= titleLen + mRowPadding + offset) {
            letter = null;
        } else {
            int posInTitle = pos - mRowPadding - offset;
            if (posInTitle < titleLen) {
                letter = titlePart.charAt(posInTitle);
            }
        }

        if (letter == null) {
            holder.mItemTextView.setVisibility(View.INVISIBLE);
        } else {
            holder.mItemTextView.setVisibility(View.VISIBLE);
            holder.mItemTextView.setText(letter.toString());
            holder.setColors(firstRow);
        }
    }

    @Override
    public int getItemCount() {
        return mRowLength * 2;
    }
}
