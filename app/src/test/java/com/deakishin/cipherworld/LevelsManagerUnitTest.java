package com.deakishin.cipherworld;

import com.deakishin.cipherworld.model.cipherstorage.CipherStorage;
import com.deakishin.cipherworld.model.levelsmanager.LevelsManager;
import com.deakishin.cipherworld.model.levelsmanager.LevelsManagerImpl;
import com.deakishin.cipherworld.model.settings.Settings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Tests levels manager.
 */
@RunWith(MockitoJUnitRunner.class)
public class LevelsManagerUnitTest {

    LevelsManager mManager;

    @Mock
    CipherStorage mCipherStorage;
    @Mock
    Settings mSettings;

    private int mLevelCount = 4;
    private int mCipherCount = 20;
    private int[] mSolved = new int[]{18, 14, 5, 0};

    @Before
    public void setUp() {
        when(mCipherStorage.getLevelCount()).thenReturn(mLevelCount);
        for (int i = 1; i <= mLevelCount; i++) {
            when(mCipherStorage.getCiphersSolvedCountForLevel(eq(i))).thenReturn(new int[]{mCipherCount, mSolved[i - 1]});
        }

        mManager = new LevelsManagerImpl(mCipherStorage, mSettings);
    }

    @Test
    public void managerWork() {
        assertEquals(-1, mManager.getCiphersToSolveCount(1));
        assertEquals(-1, mManager.getCiphersToSolveCount(2));
        assertEquals(-1, mManager.getCiphersToSolveCount(3));
        assertEquals((int) (3 * mCipherCount * LevelsManager.REQUIRED_SOLVED_RATIO) - mSolved[0] - mSolved[1] - mSolved[2],
                mManager.getCiphersToSolveCount(4));
    }
}
