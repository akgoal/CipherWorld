package com.deakishin.cipherworld.dagger;

import android.content.Context;

import com.deakishin.cipherworld.model.initmanager.InitManager;
import com.deakishin.cipherworld.model.initmanager.InitManagerImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class InitModule {

    @Provides
    @Singleton
    public InitManager provideInitManager(Context context) {
        return new InitManagerImpl(context);
    }
}
