package com.deakishin.cipherworld.presenters.loading;


import com.deakishin.cipherworld.gui.cipherscreen.MvpCipherView;
import com.deakishin.cipherworld.gui.loadingscreen.MvpLoadingView;

/**
 * MVP Presenter interface for executing loading.
 */
public interface MvpLoadingPresenter {
    /**
     * Binds MVP View to the Presenter.
     *
     * @param view     View to bind.
     */
    void bindView(MvpLoadingView view);

    /**
     * Unbinds view.
     */
    void unbindView();
}
