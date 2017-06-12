package com.deakishin.cipherworld.gui.cipherscreen;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deakishin.cipherworld.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for a keyboard.
 */
class KeyboardAdapter extends RecyclerView.Adapter<KeyboardAdapter.ViewHolder> {

    /* Letters for the keyboard. */
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /* Position for the close button. */
    static final int CLOSE_POS = 20;

    /* Position for the erase button. */
    static final int ERASE_POS = 27;

    // String that contains occupied letters.
    private String mOccupiedLetters;

    // String that contains letters that must not be shown.
    private String mHiddenLetters;

    // Current letter on the keyboard.
    private Character mCurrentLetter;

    // Text colors for letters.
    private int[] mLetterColors;

    // Listener to letter selection.
    private OnLetterSelectedListener mListener;

    /**
     * Listener interface to be notified when a letter or a special option is selected.
     */
    interface OnLetterSelectedListener {
        /**
         * Invoked when a letter is selected.
         *
         * @param letter Selected letter.
         */
        void onLetterSelected(char letter);

        /**
         * Invoked when the close button is selected.
         */
        void onCloseSelected();

        /**
         * Invoked when the erase button is selected.
         */
        void onEraseSelected();
    }

    /**
     * Constructs adapter.
     *
     * @param onLetterSelectedListener Listener to letters being selected.
     * @param letterColor              Color for a normal letter.
     * @param occupiedLetterColor      Color for a letter that is occupied.
     * @param currentLetterColor       Color for the current letter.
     */
    KeyboardAdapter(OnLetterSelectedListener onLetterSelectedListener, int letterColor, int occupiedLetterColor,
                    int currentLetterColor) {
        mListener = onLetterSelectedListener;
        mLetterColors = new int[]{letterColor, occupiedLetterColor, currentLetterColor};
    }

    /**
     * Sets occupied and hidden letters. Occupied letters get marked as occupied,
     * hidden letters get not shown.
     * Also sets current letter.
     *
     * @param occupiedLetters String that contains occupied letters.
     * @param hiddenLetters   String that contains hidden letters.
     * @param currentLetter   Current letter.
     */
    void setOccupiedAndHiddenLetters(String occupiedLetters, String hiddenLetters,
                                     Character currentLetter) {
        mOccupiedLetters = occupiedLetters;
        mHiddenLetters = hiddenLetters;
        mCurrentLetter = currentLetter;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cipher_keyboard_item_view)
        TextView mLetterButton;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cipher_keyboard_item, parent, false);
        return new KeyboardAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == CLOSE_POS) {
            holder.mLetterButton.setText(R.string.keyboard_escape);
            holder.mLetterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onCloseSelected();
                    }
                }
            });
        } else if (position == ERASE_POS) {
            holder.mLetterButton.setText(R.string.keyboard_delete);
            holder.mLetterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onEraseSelected();
                    }
                }
            });
        } else {
            final Character letter = LETTERS.charAt(position -
                    (position >= CLOSE_POS ? 1 : 0) -
                    (position >= ERASE_POS ? 1 : 0));
            holder.mLetterButton.setText(letter.toString());
            holder.mLetterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onLetterSelected(letter);
                    }
                }
            });

            // Hide letter if it is in hidden letters.
            if (stringContainsLetter(mHiddenLetters, letter)) {
                holder.mLetterButton.setVisibility(View.INVISIBLE);
            } else {
                holder.mLetterButton.setVisibility(View.VISIBLE);
                // Mark the letter as occupied if it is occupied.
                if (mLetterColors != null) {
                    int textColor = letter.equals(mCurrentLetter) ? mLetterColors[2] :
                            stringContainsLetter(mOccupiedLetters, letter) ?
                                    mLetterColors[1] : mLetterColors[0];
                    holder.mLetterButton.setTextColor(textColor);
                }
            }
        }
    }

    // Returns true if the string contains specified letter.
    private boolean stringContainsLetter(String string, Character letter) {
        if (string == null || letter == null) {
            return false;
        }
        return string.contains(letter.toString());
    }

    @Override
    public int getItemCount() {
        return LETTERS.length() + 2;
    }
}
