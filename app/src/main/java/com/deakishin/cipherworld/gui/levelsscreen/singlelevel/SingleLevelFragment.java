package com.deakishin.cipherworld.gui.levelsscreen.singlelevel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deakishin.cipherworld.R;
import com.deakishin.cipherworld.app.CipherWorldApplication;
import com.deakishin.cipherworld.gui.CipherSwitchingActivity;
import com.deakishin.cipherworld.gui.cipherscreen.CipherActivity;
import com.deakishin.cipherworld.model.cipherstorage.CipherShortInfo;
import com.deakishin.cipherworld.presenters.MvpSingleLevelPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment for displaying ciphers of a single level. Implements MVP View interface.
 * <p>
 * If the host activity want to organize going to a cipher's screen itself,
 * then it must implement {@link CipherSwitchingActivity}.
 */
public class SingleLevelFragment extends Fragment implements MvpSingleLevelView {

    /* Keys for arguments. */
    private static final String EXTRA_LEVEL = "level";

    // Widgets.
    @BindView(R.id.single_level_lock_panel)
    View mLockPanel;
    @BindView(R.id.single_level_lock_textView)
    TextView mLockTextView;
    @BindView(R.id.single_level_ciphers_panel)
    View mCiphersPanel;
    @BindView(R.id.single_level_ciphers_recyclerView)
    RecyclerView mCiphersRecyclerView;
    private CiphersAdapter mCiphersAdapter;
    private RecyclerView.LayoutManager mCiphersLayoutManager;

    // Object for unbinding views bound with ButterKnife.
    private Unbinder mButterknifeUnbinder;

    // Level number to display data for.
    private int mLevel;

    // MVP Presenter.
    @Inject
    MvpSingleLevelPresenter mPresenter;

    // Number of ciphers in each row in the grid.
    @BindInt(R.integer.level_ciphers_grid_row_length)
    int mRowLength;

    @BindString(R.string.level_lock_one_to_solve)
    String mLockTextOneToSolve;
    @BindString(R.string.level_lock)
    String mLockText;

    public SingleLevelFragment() {
    }

    /**
     * Constructs and returns instance of the fragment for a concrete level.
     *
     * @param level Level number (starting from 1).
     * @return Configured fragment.
     */
    public static SingleLevelFragment getInstance(int level) {
        SingleLevelFragment fragment = new SingleLevelFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_LEVEL, level);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey(EXTRA_LEVEL)) {
            mLevel = args.getInt(EXTRA_LEVEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = layoutInflater.inflate(R.layout.single_level_fragment, parent, false);

        mButterknifeUnbinder = ButterKnife.bind(this, v);

        // Configure RecyclerView
        mCiphersRecyclerView.setHasFixedSize(true);
        mCiphersLayoutManager = new GridLayoutManager(getActivity(), mRowLength);
        mCiphersRecyclerView.setLayoutManager(mCiphersLayoutManager);
        mCiphersAdapter = new CiphersAdapter(new CiphersAdapter.OnCipherClickListener() {
            @Override
            public void onCipherClicked(int cipherId) {
                mPresenter.onCipherClicked(cipherId);
            }
        });
        mCiphersRecyclerView.setAdapter(mCiphersAdapter);

        ((CipherWorldApplication) getActivity().getApplication()).getAppComponent().inject(this);

        mPresenter.bindView(this, mLevel);

        return v;
    }

    @Override
    public void onDestroyView() {
        mPresenter.unbindView();
        mButterknifeUnbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setCiphersInfo(List<CipherShortInfo> ciphers) {
        mCiphersAdapter.setDataset(ciphers);
    }

    @Override
    public void goToCipher(int cipherId) {

        // Check if the host activity implements interface for going
        // to a cipher's screen.
        if (getActivity() instanceof CipherSwitchingActivity) {
            ((CipherSwitchingActivity) getActivity()).goToCipher(cipherId);
        } else {
            // If the activity can't go to a cipher's screen,
            // start cipher's activity here.
            Intent intent = new Intent(getActivity(), CipherActivity.class);
            intent.putExtra(CipherActivity.EXTRA_CIPHER_ID, cipherId);
            startActivity(intent);
        }
    }

    @Override
    public void setLocked(boolean locked, int ciphersToSolve) {
        if (locked) {
            mCiphersPanel.setVisibility(View.GONE);
            mLockPanel.setVisibility(View.VISIBLE);
            String lockText = ciphersToSolve == 1 ?
                    mLockTextOneToSolve :
                    String.format(mLockText, ciphersToSolve);
            mLockTextView.setText(lockText);
        } else {
            mCiphersPanel.setVisibility(View.VISIBLE);
            mLockPanel.setVisibility(View.GONE);
        }
    }
}
