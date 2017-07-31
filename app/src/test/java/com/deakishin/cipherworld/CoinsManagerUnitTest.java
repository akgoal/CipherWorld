package com.deakishin.cipherworld;

import com.deakishin.cipherworld.model.cipherstorage.CipherStorage;
import com.deakishin.cipherworld.model.coinsmanager.CoinsManager;
import com.deakishin.cipherworld.model.coinsmanager.CoinsManagerImpl;
import com.deakishin.cipherworld.model.levelsmanager.LevelsManager;
import com.deakishin.cipherworld.model.levelsmanager.LevelsManagerImpl;
import com.deakishin.cipherworld.model.settings.Settings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests coins manager.
 */
@RunWith(MockitoJUnitRunner.class)
public class CoinsManagerUnitTest {

    private CoinsManager mManager;

    @Mock
    CoinsManager.OnCoinsChangedListener mListener;

    @Mock
    Settings mSettings;

    private int mInitCoins = 100;

    @Before
    public void setUp() {
        when(mSettings.loadCoins()).thenReturn(mInitCoins);

        mManager = new CoinsManagerImpl(mSettings);
        mManager.addOnCoinsChangedListener(mListener);
    }

    @Test
    public void managerWork() {
        int coins = mInitCoins;
        assertEquals(coins, mManager.getCoins());

        CoinsManager.Product product = CoinsManager.Product.OPEN_SYMBOL;
        int price = mManager.getPrice(product);
        assertEquals(coins >= price, mManager.enoughCoins(product));

        mManager.spendCoins(product);
        coins -= price;
        assertEquals(coins, mManager.getCoins());
        verify(mListener).onCoinsChanged(coins);

        int adClickRew = mManager.getReward(CoinsManager.EarnWay.AD_CLICKING);
        int adWatchRew = mManager.getReward(CoinsManager.EarnWay.AD_WATCHING);
        mManager.addCoins(CoinsManager.EarnWay.AD_CLICKING);
        mManager.addCoins(CoinsManager.EarnWay.AD_WATCHING);
        coins = coins + adClickRew + adWatchRew;
        assertEquals(coins, mManager.getCoins());
        verify(mListener).onCoinsChanged(coins);

        boolean openedLetters = false;
        for (int level = 1; level < 5; level++, openedLetters = !openedLetters) {
            mManager.addCoinsForSolvingCipher(level, true, openedLetters);
            coins += mManager.getRewardForSolvingCipher(level, openedLetters);
            coins += mManager.getRewardForSolvingLevel(level);
            verify(mListener).onCoinsChanged(coins);
        }
        assertEquals(coins, mManager.getCoins());

        mManager.removeOnCoinsChangedListener(mListener);
        mManager.addCoins(CoinsManager.EarnWay.AD_WATCHING);
        coins += adWatchRew;
        assertEquals(coins, mManager.getCoins());
        verify(mListener, never()).onCoinsChanged(coins);
    }
}
