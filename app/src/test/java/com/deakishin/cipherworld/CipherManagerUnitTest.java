package com.deakishin.cipherworld;

import com.deakishin.cipherworld.model.ciphergenerator.CipherGenerator;
import com.deakishin.cipherworld.model.ciphergenerator.CipherSymbol;
import com.deakishin.cipherworld.model.ciphermanager.CipherManager;
import com.deakishin.cipherworld.model.ciphermanager.CipherManagerImpl;
import com.deakishin.cipherworld.model.cipherstorage.CipherInfo;
import com.deakishin.cipherworld.model.cipherstorage.CipherShortInfo;
import com.deakishin.cipherworld.model.cipherstorage.CipherStorage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.AdditionalMatchers.and;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Tests cipher manager.
 */
@RunWith(MockitoJUnitRunner.class)
public class CipherManagerUnitTest {

    CipherManager mManager;

    @Mock
    CipherGenerator mGenerator;
    @Mock
    CipherStorage mCipherStorage;

    int mCipherId = 1;
    String mTestCipherText = "CipherWorldCipherWorld";
    char mReplacedLetter = 'i';
    char mReplacingLetter = 'z';

    @Before
    public void setUp() {

        when(mGenerator.getValidText(eq(mTestCipherText))).thenReturn(mTestCipherText);
        List<CipherSymbol> symbols = new ArrayList<>();
        for (int idx = 0; idx < mTestCipherText.length(); idx++) {
            symbols.add(new CipherSymbol(getId(idx), null));
        }
        when(mGenerator.generateCipher(eq(mTestCipherText))).thenReturn(symbols);

        CipherInfo cipherInfo = new CipherInfo();
        CipherShortInfo cipherShortInfo = new CipherShortInfo();
        cipherShortInfo.setId(mCipherId);
        cipherShortInfo.setSolved(false);
        cipherInfo.setShortInfo(cipherShortInfo);
        cipherInfo.setSolution(mTestCipherText);
        String currentSolution = mTestCipherText.replace(mReplacedLetter, mReplacingLetter);
        for (char chr : mTestCipherText.toCharArray()) {
            currentSolution = currentSolution.replace(chr, '_');
        }
        cipherInfo.setCurrentSolution(currentSolution);
        when(mCipherStorage.getCipher(eq(mCipherId))).thenReturn(cipherInfo);
        when(mCipherStorage.getCipher(not(eq(mCipherId)))).thenReturn(null);

        mManager = new CipherManagerImpl(mGenerator, mCipherStorage);
        mManager.setUp(mCipherId);
    }

    // Returns stub symbol's id for a position in the solution text.
    private int getId(int pos) {
        return (int) mTestCipherText.charAt(pos);
    }

    @Test
    public void managerSetUp() {
        assertEquals(false, mManager.setUp(mCipherId + 1));
        assertEquals(true, mManager.setUp(mCipherId));
        assertEquals(mCipherId, mManager.getCipherInfo().getShortInfo().getId());
        assertEquals(mTestCipherText.length(), mManager.getSymbols().size());
        assertEquals(false, mManager.checkIfSolved());

        for (int i = 0; i < mTestCipherText.length() / 2; i++) {
            assertEquals(Character.toString(mTestCipherText.charAt(i)),
                    mManager.getSolutionMapping().get(getId(i)).toString());
        }
    }

    @Test
    public void cipherDecoding() {
        for (int i = 0; i < mTestCipherText.length() / 2; i++) {
            mManager.setLetter(getId(i), mTestCipherText.charAt(i));
        }
        assertEquals(true, mManager.checkIfSolved());

        mManager.getCipherInfo().getShortInfo().setSolved(false);
        int first = (int) mTestCipherText.charAt(0);
        mManager.setLetter(first, null);
        assertEquals(false, mManager.checkIfSolved());

        mManager.setLetter(first, 'c');
        assertEquals(true, mManager.checkIfSolved());

        mManager.getCipherInfo().getShortInfo().setSolved(false);
        mManager.setLetter(first, 'z');
        assertEquals(false, mManager.checkIfSolved());

        mManager.setLetter(first, 'C');
        assertEquals(true, mManager.checkIfSolved());
    }

    @Test
    public void openingSymbols() {
        int idx = 0;
        int id = getId(idx);
        mManager.openSymbol(id);
        assertEquals(mTestCipherText.charAt(idx), (char) mManager.getLetterMapping().get(id));

        mManager.setLetter(id, 'Z');
        assertEquals(mTestCipherText.charAt(idx), (char) mManager.getLetterMapping().get(id));

        mManager.setLetter(getId(idx + 1), mTestCipherText.charAt(idx));
        assertEquals(mTestCipherText.charAt(idx), (char) mManager.getLetterMapping().get(id));

        for (int i = 0; i < mTestCipherText.length() / 2; i++) {
            int symId = getId(i);
            mManager.openSymbol(symId);
            assertEquals(mTestCipherText.charAt(i), (char) mManager.getLetterMapping().get(symId));
        }
    }

    @Test
    public void openingRandomSymbol() {

        int symId = mManager.openRandomSymbol();
        Character letter = mManager.getLetterMapping().get(symId);
        assertEquals(true, mManager.getCipherInfo().getOpenedLetters().contains(letter.toString().toUpperCase()));

        mManager.setLetter(symId, 'Z');
        assertEquals(letter, mManager.getLetterMapping().get(symId));

        mManager.setLetter(symId + 1, letter);
        assertEquals(letter, mManager.getLetterMapping().get(symId));

        for (int i = 1; i < 10; i++) {
            symId = mManager.openRandomSymbol();
            letter = mManager.getLetterMapping().get(symId);
            String openedLetters = mManager.getCipherInfo().getOpenedLetters();
            assertEquals(i + 1, openedLetters.length());
            assertEquals(true, openedLetters.contains(letter.toString().toUpperCase()));
        }
    }

    @Test
    public void savingSolutions() {
        int pos = mTestCipherText.indexOf(mReplacedLetter);
        for (int p = 0; p < mTestCipherText.length() / 2; p++) {
            int id = getId(p);
            if (p == pos) {
                assertEquals(mReplacingLetter, (char) mManager.getLetterMapping().get(id));
            } else {
                assertNull(mManager.getLetterMapping().get(id));
            }
        }
    }

    @Test
    public void checkingLetters() {
        int idx = 0;
        int id = getId(idx);
        char letter = mTestCipherText.charAt(idx);
        mManager.setLetter(id, letter);
        assertEquals(true, mManager.getLettersCount() > 0);
        assertEquals(true, mManager.checkLetters() > 0);
        assertEquals(true, mManager.getOpenedSymbols().contains(id));

        mManager.setLetter(getId(1), null);
        assertEquals(true, mManager.getLettersCount() == 0);
        mManager.setLetter(getId(idx + 1), 'Z');
        assertEquals(true, mManager.getLettersCount() > 0);
        mManager.checkLetters();
        assertEquals(0, mManager.getLettersCount());
    }
}
