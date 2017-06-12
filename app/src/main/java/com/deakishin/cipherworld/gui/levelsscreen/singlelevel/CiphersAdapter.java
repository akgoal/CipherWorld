package com.deakishin.cipherworld.gui.levelsscreen.singlelevel;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.deakishin.cipherworld.R;
import com.deakishin.cipherworld.model.cipherstorage.CipherShortInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for the list/grid of ciphers on the level.
 */
class CiphersAdapter extends RecyclerView.Adapter<CiphersAdapter.ViewHolder> {

    // Data to display.
    private List<CipherShortInfo> mDataset;

    // Listener to ciphers being clicked on.
    private OnCipherClickListener mListener;

    /**
     * Listener interface to be notified when a cipher is clicked on.
     */
    interface OnCipherClickListener {
        /**
         * Invoked when a cipher is clicked on.
         *
         * @param cipherId Cipher's id.
         */
        void onCipherClicked(int cipherId);
    }

    CiphersAdapter(OnCipherClickListener listener) {
        mListener = listener;
    }

    /**
     * Sets ciphers' data to display.
     */
    void setDataset(List<CipherShortInfo> ciphers) {
        mDataset = ciphers;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.single_level_cipher_item_button)
        Button mCipherButton;
        @BindView(R.id.single_level_cipher_item_solved_view)
        View mSolvedView;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_level_cipher_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CipherShortInfo cipher = mDataset.get(position);
        String number = Integer.toString(cipher.getNumber());
        holder.mCipherButton.setText(number);
        holder.mCipherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCipherClicked(cipher.getId());
                }
            }
        });

        holder.mSolvedView.setVisibility(cipher.isSolved() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }
}
