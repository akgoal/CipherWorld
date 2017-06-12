package com.deakishin.cipherworld.dagger;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;

import com.deakishin.cipherworld.model.assets.AssetsHelper;
import com.deakishin.cipherworld.model.assets.AssetsHelperImpl;
import com.deakishin.cipherworld.model.cipherstorage.CipherStorage;
import com.deakishin.cipherworld.model.cipherstorage.impl.CipherStorageDbImpl;
import com.deakishin.cipherworld.model.settings.Settings;
import com.deakishin.cipherworld.model.settings.SettingsImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {

    @Provides
    public AssetManager provideAssetManager(Context context) {
        return context.getAssets();
    }

    @Provides
    @Singleton
    public AssetsHelper provideAssetHelper(AssetManager assetManager) {
        return new AssetsHelperImpl(assetManager);
    }

    @Provides
    public SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    public Settings provideSettings(SharedPreferences prefs) {
        return new SettingsImpl(prefs);
    }
}
