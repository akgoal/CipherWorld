package com.deakishin.cipherworld.gui.cipherscreen;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.deakishin.cipherworld.R;
import com.deakishin.cipherworld.app.CipherWorldApplication;
import com.deakishin.cipherworld.gui.CipherSwitchingActivity;
import com.deakishin.cipherworld.gui.ColorChangingActivity;
import com.deakishin.cipherworld.gui.LevelColorHelper;
import com.deakishin.cipherworld.gui.OnBackPressedListener;
import com.deakishin.cipherworld.model.ciphergenerator.CipherSymbol;
import com.deakishin.cipherworld.presenters.MvpCipherPresenter;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.deakishin.cipherworld.gui.cipherscreen.KeyboardAdapter.CLOSE_POS;
import static com.deakishin.cipherworld.gui.cipherscreen.KeyboardAdapter.ERASE_POS;

/**
 * Fragment for displaying a cipher.
 */
public class CipherFragment extends Fragment implements MvpCipherView, OnBackPressedListener {

    /* Keys for arguments. */
    private static final String EXTRA_CIPHER_ID = "cipherId";

    // Id of a displayed cipher.
    private int mCipherId;

    // Object for unbinding views bound with ButterKnife.
    private Unbinder mButterknifeUnbinder;

    // Widgets.
    @BindView(R.id.cipher_screen_background_view)
    View mBackgroundView;
    @BindView(R.id.cipher_screen_levels_imageButton)
    ImageButton mLevelsButton;
    @BindView(R.id.cipher_screen_cipher_title_textView)
    TextView mTitleTextView;
    @BindView(R.id.cipher_screen_cipher_question_textView)
    TextView mQuestionTextView;
    @BindView(R.id.cipher_symbols_recyclerView)
    RecyclerView mSymbolsRecyclerView;

    @BindView(R.id.cipher_screen_keyboard_panel)
    View mKeyboardPanel;
    @BindView(R.id.cipher_keyboard_recyclerView)
    RecyclerView mKeyboardRecyclerView;

    @BindView(R.id.cipher_screen_solved_panel)
    View mSolvedPanel;

    @BindView(R.id.cipher_screen_hint_control_panel)
    View mHintControlsPanel;
    @BindView(R.id.cipher_screen_hint_letter_imageButton)
    ImageButton mHintSymbolButton;
    @BindView(R.id.cipher_screen_hint_check_letters_imageButton)
    ImageButton mHintCheckLettersButton;
    @BindView(R.id.cipher_screen_hint_open_delimiters_imageButton)
    ImageButton mHintOpenDelimitersButton;

    @BindView(R.id.cipher_screen_hint_panel)
    View mHintPanel;

    // Number of letters in a row on the keyboard.
    private static final int KEYBOARD_ROW_LEN = 10;

    // Number of symbols in a row in the grid.
    @BindInt(R.integer.cipher_symbols_grid_row_length)
    int mRowLength;

    // Cipher's title string.
    @BindString(R.string.cipher_title)
    String mTitle;

    @BindColor(R.color.keyboardLetterText)
    int mKbLetterColor;
    @BindColor(R.color.keyboardOccupiedLetterText)
    int mKbOccupiedLetterColor;
    @BindColor(R.color.keyboardCurrentLetterText)
    int mKbCurrentLetterColor;

    // Indicates whether changes has to be animated or not.
    private boolean mAnimateChanges = false;

    // Adapter for the symbols grid.
    private SymbolsAdapter mSymbolsAdapter;

    // Adapter for the keyboard.
    private KeyboardAdapter mKeyboardAdapter;

    // Helper for getting background and status bar colors.
    private LevelColorHelper mLevelColorHelper;

    // MVP Presenter.
    @Inject
    MvpCipherPresenter mPresenter;

    // View Holder for the Hint panel.
    private HintPanelViewHolder mHintPanelViewHolder;

    /**
     * View Holder for the Hint panel/
     */
    class HintPanelViewHolder {
        @BindView(R.id.cipher_hint_panel_price_display_panel)
        View mPricePanel;
        @BindView(R.id.cipher_hint_panel_price_display_textView)
        TextView mPriceTextView;
        @BindView(R.id.cipher_hint_panel_textView)
        TextView mDescriptionTextView;
        @BindView(R.id.cipher_hint_panel_cancel_textView)
        TextView mCancelButton;
        @BindView(R.id.cipher_hint_panel_confirm_textView)
        TextView mConfirmButton;

        @OnClick(R.id.cipher_hint_panel_confirm_textView)
        void onConfirmClicked() {
            mPresenter.onHintConfirmed();
        }

        @OnClick(R.id.cipher_hint_panel_cancel_textView)
        void onCancelClicked() {
            mPresenter.onHintCanceled();
        }

        HintPanelViewHolder() {
        }
    }

    // Object for unbinding Hint panel views bound with ButterKnife.
    private Unbinder mButterknifeHinePanelUnbinder;

    // Animation objects and parameters.
    private ObjectAnimator mKeyboardAnimator;
    @BindInt(R.integer.cipher_keyboard_slide_anim_dur)
    int mKeyboardAnimDur;
    private boolean mKeyboardShown = false;
    @BindInt(R.integer.cipher_keyboard_hint_symbol_anim_offset)
    int mHintSymbolAnimOffset;
    private static final float HINT_BUTTON_MIN_ALPHA = 0.1f;
    private ObjectAnimator mHintSymbolButtonAnimator;
    private boolean mHintSymbolShown = false;
    private ObjectAnimator mHintCheckLettersButtonAnimator;
    private boolean mHintCheckLettersShown = false;
    private ObjectAnimator mHintOpenDelimitersButtonAnimator;
    private boolean mHintOpenDelimitersShown = false;
    private ObjectAnimator mHintPanelAnimator;
    private boolean mHintPanelShown = false;

    @BindInt(R.integer.cipher_symbol_turn_over_anim_dur)
    int mSymbolTurnOverAnimDur;

    public CipherFragment() {
    }

    /**
     * Constructs and returns instance of the fragment for a concrete cipher.
     *
     * @param cipherId Cipher's id.
     * @return Configured fragment.
     */
    public static CipherFragment getInstance(int cipherId) {
        CipherFragment fragment = new CipherFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_CIPHER_ID, cipherId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey(EXTRA_CIPHER_ID)) {
            mCipherId = args.getInt(EXTRA_CIPHER_ID);
        }

        mLevelColorHelper = LevelColorHelper.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = layoutInflater.inflate(R.layout.cipher_fragment, parent, false);

        mButterknifeUnbinder = ButterKnife.bind(this, v);

        // Configure Symbols RecyclerView.
        mSymbolsRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), mRowLength);
        mSymbolsRecyclerView.setLayoutManager(gridLayoutManager);
        mSymbolsAdapter = new SymbolsAdapter(getActivity(), new SymbolsAdapter.OnSymbolClickListener() {
            @Override
            public void onSymbolClicked(int symbolId) {
                mPresenter.onSymbolClicked(symbolId);
            }
        }, mSymbolTurnOverAnimDur);
        mSymbolsRecyclerView.setAdapter(mSymbolsAdapter);
        mSymbolsRecyclerView.setWillNotDraw(false);

        // Configure Keyboard RecyclerView.
        mKeyboardRecyclerView.setHasFixedSize(true);
        GridLayoutManager keyboardGridLayoutManager = new GridLayoutManager(getActivity(), KEYBOARD_ROW_LEN);
        keyboardGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == CLOSE_POS || position == ERASE_POS) {
                    return 2;
                }
                return 1;
            }
        });
        mKeyboardRecyclerView.setLayoutManager(keyboardGridLayoutManager);
        mKeyboardAdapter = new KeyboardAdapter(new KeyboardAdapter.OnLetterSelectedListener() {
            @Override
            public void onLetterSelected(char letter) {
                mPresenter.onKeyboardLetterSelected(letter);
            }

            @Override
            public void onCloseSelected() {
                mPresenter.onKeyboardCloseClicked();
            }

            @Override
            public void onEraseSelected() {
                mPresenter.onKeyboardEraseClicked();
            }
        }, mKbLetterColor, mKbOccupiedLetterColor, mKbCurrentLetterColor);
        mKeyboardRecyclerView.setAdapter(mKeyboardAdapter);

        // Bind Hint panel views.
        mHintPanelViewHolder = new HintPanelViewHolder();
        mButterknifeHinePanelUnbinder = ButterKnife.bind(mHintPanelViewHolder, mHintPanel);

        ((CipherWorldApplication) getActivity().getApplication()).getAppComponent().inject(this);

        mPresenter.restoreState(savedInstanceState);
        mPresenter.bindView(this, mCipherId);

        return v;
    }

    @Override
    public void onDestroyView() {
        mPresenter.unbindView();
        mButterknifeUnbinder.unbind();
        mButterknifeHinePanelUnbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mPresenter.storeState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        mPresenter.onPaused();

        super.onPause();
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackClicked();
    }

    @OnClick(R.id.cipher_screen_levels_imageButton)
    public void onLevelsClicked() {
        mPresenter.onLevelsClicked();
    }

    @OnClick(R.id.cipher_screen_hint_letter_imageButton)
    public void onHintLetterClicked() {
        mPresenter.onHintControlSymbolClicked();
    }

    @OnClick(R.id.cipher_screen_hint_check_letters_imageButton)
    public void onHintCheckLettersClicked() {
        mPresenter.onHintControlCheckLettersClicked();
    }

    @OnClick(R.id.cipher_screen_hint_open_delimiters_imageButton)
    public void onHintOpenDelimitersClicked() {
        mPresenter.onHintControlOpenDelimitersClicked();
    }

    @Override
    public void setAnimateChanges(boolean toAnimate) {
        mAnimateChanges = toAnimate;
        mSymbolsAdapter.setSymbolsAnimated(mAnimateChanges);
    }

    // MVP View methods.
    @Override
    public void setCipherData(int level, int number) {
        mTitleTextView.setText(String.format(mTitle, level, number));

        changeActivityColors(mLevelColorHelper.getBackgroundColor(level - 1),
                mLevelColorHelper.getStatusBarColor(level - 1));

        mBackgroundView.setBackgroundColor(mLevelColorHelper.getBackgroundColor(level - 1));
    }

    // Changes activity's colors if possible.
    private void changeActivityColors(int bgColor, int statusBarColor) {
        if (getActivity() instanceof ColorChangingActivity) {
            ((ColorChangingActivity) getActivity()).changeColors(bgColor, statusBarColor);
        }
    }

    @Override
    public void setCipherQuestion(String question) {
        mQuestionTextView.setText(question);
    }

    @Override
    public void setSolved(boolean solved) {
        if (mAnimateChanges) {
            if (solved) {
                mSolvedPanel.setVisibility(View.VISIBLE);
                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.solved_stamp);
                mSolvedPanel.startAnimation(anim);
            }
        } else {
            mSolvedPanel.setVisibility(solved ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void setSymbols(List<CipherSymbol> symbols) {
        mSymbolsAdapter.setSymbols(symbols);
    }

    @Override
    public void setDelimiterPositions(List<Integer> delimiterPositions) {
        mSymbolsAdapter.setDelimiterPositions(delimiterPositions);
    }

    @Override
    public void setLetters(Map<Integer, Character> letters) {
        mSymbolsAdapter.setLetters(letters);
    }

    @Override
    public void setOpenedLetters(List<Integer> openedSymbols) {
        mSymbolsAdapter.setOpenedSymbols(openedSymbols);
    }

    @Override
    public void setSelectedSymbol(int symbolId) {
        mSymbolsAdapter.setSelectedSymbolId(symbolId);
    }

    @Override
    public void showKeyboard(String occupiedLetters, String unavailableLetters, Character currentLetter) {
        mKeyboardAdapter.setOccupiedAndHiddenLetters(occupiedLetters, unavailableLetters, currentLetter);

        setKeyboardShown(true);
    }

    @Override
    public void hideKeyboard() {
        setKeyboardShown(false);
    }

    // Shows/hides the keyboard depending on the shown param.
    private void setKeyboardShown(boolean toShow) {
        if (mAnimateChanges) {
            if (toShow == mKeyboardShown) {
                return;
            }
            mKeyboardPanel.setVisibility(View.VISIBLE);
            if (mKeyboardAnimator == null || !mKeyboardAnimator.isRunning()) {
                mKeyboardAnimator = constructKeyboardAnimator(toShow);
                mKeyboardAnimator.start();
            } else {
                mKeyboardAnimator.reverse();
            }
        } else {
            mKeyboardPanel.setVisibility(toShow ? View.VISIBLE : View.INVISIBLE);
        }
        mKeyboardShown = toShow;
    }

    @Override
    public void setHintSymbolShown(boolean toShow) {
        if (mAnimateChanges) {
            if (toShow == mHintSymbolShown) {
                return;
            }
            // mHintSymbolButton.setVisibility(View.VISIBLE);
            if (mHintSymbolButtonAnimator == null || !mHintSymbolButtonAnimator.isRunning()) {
                mHintSymbolButtonAnimator = constructHintButtonAnimator(toShow, mHintSymbolButton);
                mHintSymbolButtonAnimator.start();
            } else {
                mHintSymbolButtonAnimator.reverse();
            }
        } else {
            // mHintSymbolButton.setVisibility(toShow ? View.VISIBLE : View.INVISIBLE);
            mHintSymbolButton.setAlpha(toShow ? 1f : HINT_BUTTON_MIN_ALPHA);
        }
        mHintSymbolShown = toShow;
        mHintSymbolButton.setEnabled(toShow);
    }

    @Override
    public void showHintSymbolConfirmation(int priceInCoins) {
        setHintPanel(R.string.hint_open_symbol_desc, R.string.hint_open_symbol_confirm, priceInCoins);

        showHintPanel(true);
    }

    @Override
    public void showHintSymbolNotEnoughCoins(int coins, int hintPrice) {
        setHintPanel(R.string.hint_open_symbol_not_enough_coins, -1, hintPrice);

        showHintPanel(true);
    }

    @Override
    public void showHintSymbolMaxSymbolsOpened(int openedSymbols) {
        setHintPanel(R.string.hint_open_symbol_max_symbols_opened, -1, -1);

        showHintPanel(true);
    }

    @Override
    public void closeHintSymbolPanel() {
        showHintPanel(false);
    }

    /**
     * Sets info for the panel.
     *
     * @param descrResId   String resource id for description.
     * @param confirmResId String resource id for the Confirm button.
     *                     <0 of the Confirm button must not be shown.
     * @param price        Price to display. <0 if the price must not be shown.
     */
    private void setHintPanel(int descrResId, int confirmResId, int price) {
        mHintPanelViewHolder.mDescriptionTextView.setText(descrResId);

        if (confirmResId < 0) {
            mHintPanelViewHolder.mConfirmButton.setVisibility(View.INVISIBLE);
        } else {
            mHintPanelViewHolder.mConfirmButton.setVisibility(View.VISIBLE);
            mHintPanelViewHolder.mConfirmButton.setText(confirmResId);
        }

        if (price < 0) {
            mHintPanelViewHolder.mPricePanel.setVisibility(View.INVISIBLE);
        } else {
            String priceStr = Integer.toString(price);
            mHintPanelViewHolder.mPriceTextView.setText(priceStr);
            mHintPanelViewHolder.mPricePanel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setHintCheckLettersShown(boolean toShow) {
        if (mAnimateChanges) {
            if (toShow == mHintCheckLettersShown) {
                return;
            }
            if (mHintCheckLettersButtonAnimator == null || !mHintCheckLettersButtonAnimator.isRunning()) {
                mHintCheckLettersButtonAnimator = constructHintButtonAnimator(toShow, mHintCheckLettersButton);
                mHintCheckLettersButtonAnimator.start();
            } else {
                mHintCheckLettersButtonAnimator.reverse();
            }
        } else {
            mHintCheckLettersButton.setAlpha(toShow ? 1f : HINT_BUTTON_MIN_ALPHA);
        }
        mHintCheckLettersShown = toShow;
        mHintCheckLettersButton.setEnabled(toShow);
    }

    @Override
    public void showHintCheckLettersConfirmation(int priceInCoins) {
        setHintPanel(R.string.hint_check_letters_desc, R.string.hint_check_letters_confirm, priceInCoins);

        showHintPanel(true);
    }

    @Override
    public void showHintCheckLettersNotEnoughCoins(int coins, int hintPrice) {
        setHintPanel(R.string.hint_check_letters_not_enough_coins, -1, hintPrice);

        showHintPanel(true);
    }

    @Override
    public void showHintCheckLettersNoLetters() {
        setHintPanel(R.string.hint_check_letters_no_letters, -1, -1);

        showHintPanel(true);
    }

    @Override
    public void closeHintCheckLettersPanel() {
        showHintPanel(false);
    }

    @Override
    public void setHintOpenDelimitersShown(boolean toShow) {
        if (mAnimateChanges) {
            if (toShow == mHintOpenDelimitersShown) {
                return;
            }
            if (mHintOpenDelimitersButtonAnimator == null || !mHintOpenDelimitersButtonAnimator.isRunning()) {
                mHintOpenDelimitersButtonAnimator = constructHintButtonAnimator(toShow, mHintOpenDelimitersButton);
                mHintOpenDelimitersButtonAnimator.start();
            } else {
                mHintOpenDelimitersButtonAnimator.reverse();
            }
        } else {
            mHintOpenDelimitersButton.setAlpha(toShow ? 1f : HINT_BUTTON_MIN_ALPHA);
        }
        mHintOpenDelimitersShown = toShow;
        mHintOpenDelimitersButton.setEnabled(toShow);
    }

    @Override
    public void showHintOpenDelimitersConfirmation() {
        setHintPanel(R.string.hint_open_delimiters_desc, R.string.hint_open_delimiters_confirm, -1);

        showHintPanel(true);
    }

    @Override
    public void closeHintOpenDelimitersPanel() {
        showHintPanel(false);
    }

    // Shows the Hint panel. toShow - if the panel must be shown or hidden.
    private void showHintPanel(boolean toShow) {
        if (mAnimateChanges) {
            if (toShow == mHintPanelShown) {
                return;
            }

            mHintPanel.setVisibility(View.VISIBLE);
            if (mHintPanelAnimator == null || !mHintPanelAnimator.isRunning()) {
                mHintPanelAnimator = constructHintPanelAnimator(toShow);
                mHintPanelAnimator.start();
            } else {
                mHintPanelAnimator.reverse();
            }
        } else {
            mHintPanel.setVisibility(toShow ? View.VISIBLE : View.INVISIBLE);
        }
        mHintPanelShown = toShow;
    }

    @Override
    public void exit() {
        // Check if the host activity implements interface for switching back
        // to levels screen. If so, delegate operation to the activity.
        // Otherwise exit activity from here.
        if (getActivity() instanceof CipherSwitchingActivity) {
            ((CipherSwitchingActivity) getActivity()).goToLevels();
        } else {
            getActivity().finish();
        }
    }


    // Following methods construct objects for animations.

    // Constructs animator for the keyboard.
    // toOpen - if the animator must open the keyboard or close it.
    private ObjectAnimator constructKeyboardAnimator(boolean toOpen) {
        float start = toOpen ? -mKeyboardPanel.getWidth() : 0;
        float finish = toOpen ? 0 : -mKeyboardPanel.getWidth();
        ObjectAnimator animator = ObjectAnimator.ofFloat(mKeyboardPanel, "translationX",
                start, finish);
        animator.setDuration(mKeyboardAnimDur);
        return animator;
    }

    // Constructs animator for the hint panel.
    // toOpen - if the animator must open the panel or close it.
    private ObjectAnimator constructHintPanelAnimator(boolean toOpen) {
        float start = toOpen ? -mHintPanel.getWidth() : 0;
        float finish = toOpen ? 0 : -mHintPanel.getWidth();
        ObjectAnimator animator = ObjectAnimator.ofFloat(mHintPanel, "translationX",
                start, finish);
        animator.setDuration(mKeyboardAnimDur);
        return animator;
    }

    // Constructs animator for the hint button.
    // toShow - if the animator must show it or hide it.
    // hintButton - Button to animate.
    private ObjectAnimator constructHintButtonAnimator(boolean toShow, ImageButton hintButton) {
        float start = toShow ? HINT_BUTTON_MIN_ALPHA : 1;
        float finish = 1 + HINT_BUTTON_MIN_ALPHA - start;
        ObjectAnimator animator = ObjectAnimator.ofFloat(hintButton, "alpha",
                start, finish);
        animator.setStartDelay(mHintSymbolAnimOffset);
        animator.setDuration(mKeyboardAnimDur - mHintSymbolAnimOffset);
        return animator;
    }
}
