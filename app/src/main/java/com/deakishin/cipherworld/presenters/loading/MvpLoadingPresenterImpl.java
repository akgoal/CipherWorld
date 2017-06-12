package com.deakishin.cipherworld.presenters.loading;

import com.deakishin.cipherworld.gui.loadingscreen.MvpLoadingView;
import com.deakishin.cipherworld.model.initmanager.InitManager;

/**
 * MVP Presenter for executing loading.
 */
public class MvpLoadingPresenterImpl implements MvpLoadingPresenter, InitManager.OnInitListener {

    // MVP View;
    private MvpLoadingView mView;

    // Object for initializing components in the app.
    private InitManager mInitManager;

    public MvpLoadingPresenterImpl(InitManager initManager) {
        mInitManager = initManager;
    }

    @Override
    public void bindView(MvpLoadingView view) {
        mView = view;

        if (mInitManager.isInitialized()) {
            onLoaded();
        } else {
            mInitManager.addOnInitListener(this);
            mInitManager.init();
        }
    }

    // Invoked when everything is loaded.
    private void onLoaded() {
        if (mView != null) {
            mView.goToLevels();
        }
    }

    @Override
    public void unbindView() {
        mView = null;
        mInitManager.removeOnInitListener(this);
    }

    @Override
    public void onInit() {
        onLoaded();
    }
}
