package com.deakishin.cipherworld.dagger;

import com.deakishin.cipherworld.gui.cipherscreen.CipherFragment;
import com.deakishin.cipherworld.gui.coins.coinsdisplay.CoinsDisplayFragment;
import com.deakishin.cipherworld.gui.coins.coinsoptions.CoinsOptionsDialogFragment;
import com.deakishin.cipherworld.gui.levelsscreen.levels.LevelsActivity;
import com.deakishin.cipherworld.gui.levelsscreen.levels.LevelsFragment;
import com.deakishin.cipherworld.gui.levelsscreen.singlelevel.SingleLevelFragment;
import com.deakishin.cipherworld.gui.loadingscreen.LoadingFragment;
import com.deakishin.cipherworld.model.initmanager.InitManagerImpl;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, PresenterModule.class,
        CiphersModule.class, StorageModule.class, CoinsModule.class, InitModule.class})
public interface AppComponent {

    void inject(LevelsFragment target);

    void inject(SingleLevelFragment target);

    void inject(CipherFragment target);

    void inject(CoinsDisplayFragment target);

    void inject(CoinsOptionsDialogFragment target);

    void inject(LoadingFragment target);

    void inject(LevelsActivity target);


    void inject(InitManagerImpl target);
}
