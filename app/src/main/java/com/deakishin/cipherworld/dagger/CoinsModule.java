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
import com.deakishin.cipherworld.model.coinsmanager.CoinsManager;
import com.deakishin.cipherworld.model.coinsmanager.CoinsManagerImpl;
import com.deakishin.cipherworld.model.levelsmanager.LevelsManager;
import com.deakishin.cipherworld.model.levelsmanager.LevelsManagerImpl;
import com.deakishin.cipherworld.model.settings.Settings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CoinsModule {

    @Provides
    @Singleton
    public CoinsManager provideCoinsManager(Settings settings) {
        return new CoinsManagerImpl(settings);
    }
}
