package com.deakishin.cipherworld.gui.cipherscreen;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.deakishin.cipherworld.R;
import com.deakishin.cipherworld.model.ciphergenerator.CipherSymbol;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for the list/grid of ciphers on the level.
 */
class SymbolsAdapter extends RecyclerView.Adapter<SymbolsAdapter.ViewHolder> {

    private final String TAG = getClass().getSimpleName();

    // Symbols to display.
    private List<CipherSymbol> mSymbols = new ArrayList<>();
    // Mapping between symbols' ids and letters replacing the symbols.
    private Map<Integer, Character> mLetters;
    // Id of selected symbol.
    private int mSelectedSymbolId = -1;

    // Opened symbols.
    private List<Integer> mOpenedSymbols = new ArrayList<>();

    // Positions of delimiters.
    private List<Integer> mDelimiterPositions = new ArrayList<>();

    // Animations.
    private Animation mHighlightAnimation;

    // Indicates whether the symbols must be animated or not.
    private boolean mAnimateSymbols = false;

    // Duration of symbols turning over animation.
    private int mAnimationDuration;

    // Time when symbols were last changed.
    private long mLettersChangedTimeStamp;

    // Indicates the current data is the first one that was set.
    // Needed to prevent animation of the initial data setting.
    private boolean mDataIsInitial = true;

    // Symbols that have to be "turned over", i.e. if they are open
    // they must close and vice versa.
    private List<Integer> mSymbolsToTurnOver = new ArrayList<>();

    // Listener to symbols being clicked on.
    private OnSymbolClickListener mListener;

    private Context mContext;

    /**
     * Listener interface to be notified when a symbol is clicked on.
     */
    interface OnSymbolClickListener {
        /**
         * Invoked when a symbol is clicked on.
         *
         * @param symbolId Symbol's id.
         */
        void onSymbolClicked(int symbolId);
    }

    /**
     * Constructs adapter.
     *
     * @param context                   App context.
     * @param listener                  Listener to symbols being clicked on.
     * @param turnOverAnimationDuration Duration of symbols turning-over animation
     *                                  (in milliseconds).
     */
    SymbolsAdapter(Context context, OnSymbolClickListener listener, int turnOverAnimationDuration) {
        mListener = listener;
        mContext = context.getApplicationContext();
        mHighlightAnimation = AnimationUtils.loadAnimation(mContext, R.anim.cipher_symbol_highlight);
        mAnimationDuration = turnOverAnimationDuration;
    }

    /**
     * Sets symbols to display.
     */
    void setSymbols(List<CipherSymbol> symbols) {
        if (symbols == null) {
            mSymbols.clear();
        } else {
            mSymbols = symbols;
        }
        notifyDataSetChanged();
    }

    /**
     * Sets opened symbols.
     */
    void setOpenedSymbols(List<Integer> openedSymbols) {
        if (openedSymbols == null) {
            mOpenedSymbols.clear();
        } else {
            mOpenedSymbols = openedSymbols;
        }
        notifyDataSetChanged();
    }

    /**
     * Sets mapping between symbols' ids and letters that have to replace the symbols.
     */
    void setLetters(Map<Integer, Character> letterMap) {
        // If current set data is null than
        // the data that is being set is initial.
        mDataIsInitial = mLetters == null;
        if (mLetters == null) {
            mLetters = new HashMap<>();
        }

        // Construct symbols to open/close.
        mSymbolsToTurnOver.clear();

        // Iterate through symbols,
        // see if letters mapped to them changed
        // and decide whether the symbols must be opened or closed.
        for (CipherSymbol symbol : mSymbols) {
            int id = symbol.getId();
            Character lastLetter = mLetters.get(id);
            Character newLetter = letterMap == null ? null : letterMap.get(id);
            if (lastLetter == null) {
                if (newLetter != null) {
                    mSymbolsToTurnOver.add(id);
                }
            } else {
                if (newLetter == null) {
                    mSymbolsToTurnOver.add(id);
                }
            }
        }

        if (letterMap == null) {
            mLetters.clear();
        } else {
            mLetters.clear();
            mLetters.putAll(letterMap);
        }

        mLettersChangedTimeStamp = new Date().getTime();
        notifyDataSetChanged();
    }

    /**
     * Sets id of the symbol to be selected.
     */
    void setSelectedSymbolId(int selectedSymbolId) {
        mSelectedSymbolId = selectedSymbolId;
        notifyDataSetChanged();
    }

    /**
     * Sets list of positions after which delimiters must be shown.
     */
    void setDelimiterPositions(List<Integer> delimiterPositions) {
        if (delimiterPositions == null) {
            mDelimiterPositions.clear();
        } else {
            mDelimiterPositions.addAll(delimiterPositions);
        }
        notifyDataSetChanged();
    }

    /**
     * Controls whether symbols must be animated or not.
     *
     * @param symbolsAnimated True if symbols must be animated, false otherwise.
     */
    void setSymbolsAnimated(boolean symbolsAnimated) {
        mAnimateSymbols = symbolsAnimated;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cipher_symbol_item_panel)
        View mSymbolItemPanel;
        @BindView(R.id.cipher_symbol_letter_textView)
        TextView mLetterTextView;
        @BindView(R.id.cipher_symbol_item_highlight)
        View mHighlightView;

        @BindColor(R.color.cipherSymbolLetterText)
        int mLetterTextColor;
        @BindColor(R.color.cipherSymbolLetterText1)
        int mLetterTextColor1;
        @BindColor(R.color.cipherSymbolLetterText2)
        int mLetterTextColor2;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cipher_symbol_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CipherSymbol symbol = mSymbols.get(position);

        int letterTextColor = holder.mLetterTextColor;
        if (!mDelimiterPositions.isEmpty()) {
            // Change text color of every second word divided by delimiters.
            int delCount = 0; // Number of delimiters before the symbol.
            for (Integer delimiter : mDelimiterPositions) {
                if (position > delimiter) {
                    delCount++;
                }
            }
            if (delCount % 2 == 0) {
                letterTextColor = holder.mLetterTextColor1;
            } else {
                letterTextColor = holder.mLetterTextColor2;
            }
        }
        holder.mLetterTextView.setTextColor(letterTextColor);

        Character letter = mLetters == null ? null : mLetters.get(symbol.getId());

        Integer id = symbol.getId();

        boolean symbolIsToTurnOver = false;
        if (mSymbolsToTurnOver.contains(id)) {
            mSymbolsToTurnOver.remove(id);
            symbolIsToTurnOver = true;
        }
        long timePast = new Date().getTime() - mLettersChangedTimeStamp; // Time past since letters were changed.
        boolean notOld = (timePast < mAnimationDuration); // Indicator that the view is not too old to be animated.
        updateSymbolView(holder, letter != null, letter, symbol.getImage(), mOpenedSymbols.contains(id),
                mAnimateSymbols && symbolIsToTurnOver && !mDataIsInitial && notOld);

        Log.d(TAG, "Displaying symbol. Id: " + id
                + ". Letter: " + (letter == null ? "null" : letter)
                + ". mAnimateChanges: " + mAnimateSymbols
                + ". mSymbolsToTurnOver: " + symbolIsToTurnOver
                + ". Data is initial: " + mDataIsInitial
                + ". Not old: " + notOld
                + ". Is an opened letter: " + mOpenedSymbols.contains(id));

        if (symbol.getId() == mSelectedSymbolId) {
            holder.mHighlightView.setVisibility(View.VISIBLE);
            holder.mHighlightView.setAnimation(mHighlightAnimation);
        } else {
            holder.mHighlightView.setVisibility(View.INVISIBLE);
            holder.mHighlightView.setAnimation(null);
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onSymbolClicked(symbol.getId());
                }
            }
        };
        holder.mSymbolItemPanel.setOnClickListener(onClickListener);
    }

    // Updates symbol view in the holder.
    // toOpen - if the symbol has to be opened.
    // letter - letter to draw.
    // symbolImage - symbol image to draw.
    // letterIsOpened - if the letter is opened.
    // animate - if the symbol must be animated.
    private void updateSymbolView(final ViewHolder holder, final boolean toOpen, final Character letter,
                                  final Bitmap symbolImage, final boolean letterIsOpened, boolean animate) {
        boolean closed = animate ? toOpen : !toOpen;

        updateSymbolView(holder, closed, letter, symbolImage, letterIsOpened);

        if (animate) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(-1, 1);

            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                private boolean turnedOver = false; // Symbol is turned over.

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();

                    if (value > 0 && !turnedOver) {
                        updateSymbolView(holder, !toOpen, letter, symbolImage, letterIsOpened);
                        turnedOver = true;
                    }
                    holder.mSymbolItemPanel.setScaleX(Math.abs(value));
                }
            });

            valueAnimator.setDuration(mAnimationDuration);
            valueAnimator.start();
        }
    }

    // Updates symbol view in the holder.
    // closed - if the symbol is closed. letter - letter to draw.
    // symbolImage - symbol image to draw.
    // letterIsOpened - if the letter is opened.
    private void updateSymbolView(ViewHolder holder, boolean closed, Character letter,
                                  Bitmap symbolImage, boolean letterIsOpened) {
        if (closed) {
            holder.mSymbolItemPanel.setBackgroundResource(R.drawable.cipher_symbol_bg);
            holder.mLetterTextView.setText(null);
            holder.mLetterTextView.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), symbolImage));
        } else {
            holder.mSymbolItemPanel.setBackgroundResource(
                    letterIsOpened ? R.drawable.cipher_symbol_letter_bg_opened : R.drawable.cipher_symbol_letter_bg);
            holder.mLetterTextView.setText(letter == null ? null : letter.toString());
            holder.mLetterTextView.setBackgroundResource(0);
        }
    }

    @Override
    public int getItemCount() {
        return mSymbols == null ? 0 : mSymbols.size();
    }
}
