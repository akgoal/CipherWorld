package com.deakishin.cipherworld.dagger;

import android.content.Context;

import com.deakishin.cipherworld.model.assets.AssetsHelper;
import com.deakishin.cipherworld.model.ciphergenerator.CipherGenerator;
import com.deakishin.cipherworld.model.ciphergenerator.CipherGeneratorImpl;
import com.deakishin.cipherworld.model.ciphergenerator.SymbolsLoader;
import com.deakishin.cipherworld.model.ciphergenerator.SymbolsLoaderImpl;
import com.deakishin.cipherworld.model.ciphermanager.CipherManager;
import com.deakishin.cipherworld.model.ciphermanager.CipherManagerImpl;
import com.deakishin.cipherworld.model.cipherstorage.CipherStorage;
import com.deakishin.cipherworld.model.cipherstorage.impl.CipherStorageDbImpl;
import com.deakishin.cipherworld.model.cipherstorage.impl.LocalDbHelper;
import com.deakishin.cipherworld.model.cipherstorage.impl.initialdataloader.InitialDataLoader;
import com.deakishin.cipherworld.model.cipherstorage.impl.initialdataloader.InitialDataLoaderImpl;
import com.deakishin.cipherworld.model.levelsmanager.LevelsManager;
import com.deakishin.cipherworld.model.levelsmanager.LevelsManagerImpl;
import com.deakishin.cipherworld.model.settings.Settings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CiphersModule {

    @Provides
    public InitialDataLoader provideInitialDataLoader(AssetsHelper assetsHelper) {
        return new InitialDataLoaderImpl(assetsHelper);
    }

    @Provides
    public LocalDbHelper provideLocalDbHelper(Context context, InitialDataLoader initialDataLoader) {
        return new LocalDbHelper(context, initialDataLoader);
    }

    @Provides
    @Singleton
    public CipherStorage provideCipherStorage(LocalDbHelper localDbHelper) {
        return new CipherStorageDbImpl(localDbHelper);
    }

    @Provides
    public SymbolsLoader provideSymbolLoader(AssetsHelper assets) {
        return new SymbolsLoaderImpl(assets);
    }

    @Provides
    @Singleton
    public CipherGenerator provideCipherGenerator(SymbolsLoader symbolsLoader) {
        return new CipherGeneratorImpl(symbolsLoader);
    }

    @Provides
    @Singleton
    public CipherManager provideCipherManager(CipherGenerator cipherGenerator, CipherStorage cipherStorage) {
        return new CipherManagerImpl(cipherGenerator, cipherStorage);
    }

    @Provides
    @Singleton
    public LevelsManager provideLevelsManager(CipherStorage cipherStorage, Settings settings) {
        return new LevelsManagerImpl(cipherStorage, settings);
    }
}
