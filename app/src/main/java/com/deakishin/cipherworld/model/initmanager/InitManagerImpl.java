package com.deakishin.cipherworld.model.initmanager;

import android.content.Context;
import android.os.AsyncTask;

import com.deakishin.cipherworld.R;
import com.deakishin.cipherworld.app.CipherWorldApplication;
import com.deakishin.cipherworld.model.ciphergenerator.CipherGenerator;
import com.deakishin.cipherworld.model.cipherstorage.CipherStorage;
import com.deakishin.cipherworld.model.coinsmanager.CoinsManager;
import com.deakishin.cipherworld.model.levelsmanager.LevelsManager;
import com.deakishin.cipherworld.model.settings.Settings;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;

/**
 * Manager for initializing all components in the app.
 */
public class InitManagerImpl implements InitManager {

    // Listeners to components being initialized.
    private List<OnInitListener> mListeners = new ArrayList<>();

    // Indicates that components are initialized.
    private boolean mInitialized = false;

    // Object for initializing components in the background.
    private InitTask mInitTask;

    // Application context;
    private Context mContext;

    // Components to initialize.
    // These components are initialized by getting injected with Dagger.
    @Inject
    CipherGenerator mGenerator;
    @Inject
    CipherStorage mStorage;
    @Inject
    LevelsManager mLevelsManager;
    @Inject
    CoinsManager mCoinsManager;
    @Inject
    Settings mSettings;

    public InitManagerImpl(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void init() {
        if (mInitialized) {
            return;
        }

        if (mInitTask == null || mInitTask.isCancelled()) {
            mInitTask = new InitTask();
            mInitTask.execute();
        }
    }

    @Override
    public boolean isInitialized() {
        return mInitialized;
    }

    // Notifies listeners that all components are initialized.
    private void notifyListeners() {
        for (OnInitListener listener : mListeners) {
            listener.onInit();
        }
        mListeners.clear();
    }

    @Override
    public void addOnInitListener(OnInitListener listener) {
        mListeners.add(listener);
    }

    @Override
    public void removeOnInitListener(OnInitListener listener) {
        mListeners.remove(listener);
    }

    /**
     * Class for initializing components in the background.
     */
    private class InitTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Inject components.
            ((CipherWorldApplication) mContext).getAppComponent().inject(InitManagerImpl.this);
            MobileAds.initialize(mContext, mContext.getString(R.string.ad_app_id));

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (!isCancelled()) {
                mInitialized = true;
                notifyListeners();
            }
        }
    }
}
