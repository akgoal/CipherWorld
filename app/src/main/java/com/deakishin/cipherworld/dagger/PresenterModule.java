package com.deakishin.cipherworld.dagger;

import com.deakishin.cipherworld.model.ciphermanager.CipherManager;
import com.deakishin.cipherworld.model.cipherstorage.CipherStorage;
import com.deakishin.cipherworld.model.coinsmanager.CoinsManager;
import com.deakishin.cipherworld.model.initmanager.InitManager;
import com.deakishin.cipherworld.model.levelsmanager.LevelsManager;
import com.deakishin.cipherworld.presenters.MvpCipherPresenter;
import com.deakishin.cipherworld.presenters.MvpCipherPresenterImpl;
import com.deakishin.cipherworld.presenters.MvpLevelsPresenter;
import com.deakishin.cipherworld.presenters.MvpLevelsPresenterImpl;
import com.deakishin.cipherworld.presenters.MvpSingleLevelPresenter;
import com.deakishin.cipherworld.presenters.MvpSingleLevelPresenterImpl;
import com.deakishin.cipherworld.presenters.ads.MvpAdBannerPresenter;
import com.deakishin.cipherworld.presenters.ads.MvpAdBannerPresenterImpl;
import com.deakishin.cipherworld.presenters.ads.MvpAdVideoPresenter;
import com.deakishin.cipherworld.presenters.ads.MvpAdVideoPresenterImpl;
import com.deakishin.cipherworld.presenters.coins.MvpCoinsDisplayPresenter;
import com.deakishin.cipherworld.presenters.coins.MvpCoinsDisplayPresenterImpl;
import com.deakishin.cipherworld.presenters.coins.MvpCoinsOptionsPresenter;
import com.deakishin.cipherworld.presenters.coins.MvpCoinsOptionsPresenterImpl;
import com.deakishin.cipherworld.presenters.loading.MvpLoadingPresenter;
import com.deakishin.cipherworld.presenters.loading.MvpLoadingPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {
    @Provides
    public MvpLevelsPresenter provideMvpLevelsPresenter(LevelsManager levelsManager) {
        return new MvpLevelsPresenterImpl(levelsManager);
    }

    @Provides
    public MvpSingleLevelPresenter provideMvpSingleLevelPresenter(LevelsManager levelsManager) {
        return new MvpSingleLevelPresenterImpl(levelsManager);
    }

    @Provides
    public MvpCipherPresenter provideMvpCipherPresenter(CipherManager cipherManager, CoinsManager coinsManager,
                                                        LevelsManager levelsManager) {
        return new MvpCipherPresenterImpl(cipherManager, coinsManager, levelsManager);
    }

    @Provides
    public MvpCoinsDisplayPresenter provideMvpCoinsDisplayPresenter(CoinsManager coinsManager) {
        return new MvpCoinsDisplayPresenterImpl(coinsManager);
    }

    @Provides
    public MvpCoinsOptionsPresenter provideMvpCoinsOptionsPresenter(CoinsManager coinsManager) {
        return new MvpCoinsOptionsPresenterImpl(coinsManager);
    }

    @Provides
    public MvpLoadingPresenter provideMvpLoadingPresenter(InitManager initManager) {
        return new MvpLoadingPresenterImpl(initManager);
    }


    @Provides
    public MvpAdBannerPresenter provideMvpAdBannerPresenter(CoinsManager coinsManager) {
        return new MvpAdBannerPresenterImpl(coinsManager);
    }

    @Provides
    public MvpAdVideoPresenter provideMvpAdVideoPresenter(CoinsManager coinsManager) {
        return new MvpAdVideoPresenterImpl(coinsManager);
    }
}
