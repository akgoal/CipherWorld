package com.deakishin.cipherworld;

import com.deakishin.cipherworld.model.ciphergenerator.CipherGenerator;
import com.deakishin.cipherworld.model.ciphergenerator.CipherGeneratorImpl;
import com.deakishin.cipherworld.model.ciphergenerator.CipherSymbol;
import com.deakishin.cipherworld.model.ciphergenerator.SymbolsLoader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Tests cipher generator.
 */
@RunWith(MockitoJUnitRunner.class)
public class CipherGeneratorUnitTest {

    CipherGenerator mGenerator;

    @Mock
    SymbolsLoader mLoader;

    @Before
    public void setUp() {
        // List of loaded symbols.
        List<CipherSymbol> symbols = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            symbols.add(new CipherSymbol(i, null));
        }
        when(mLoader.loadSymbols()).thenReturn(symbols);

        mGenerator = new CipherGeneratorImpl(mLoader);
    }

    @Test
    public void cipherGenerating() {
        String text = "";
        assertEquals(0, mGenerator.generateCipher(text).size());

        text = "34шоф _+Ъ";
        assertEquals(0, mGenerator.generateCipher(text).size());

        text = "Cipher World";
        assertEquals(11, mGenerator.generateCipher(text).size());

        text = "aaaaaa";
        List<CipherSymbol> cipher = mGenerator.generateCipher(text);
        int id = cipher.get(0).getId();
        for (int i = 1; i < text.length(); i++) {
            assertEquals(id, cipher.get(i).getId());
        }

        text = "ABCDE ABCDE";
        cipher = mGenerator.generateCipher(text);
        for (int i = 0; i < 5; i++) {
            assertEquals(cipher.get(i).getId(), cipher.get(i + 5).getId());
        }
    }

    @Test
    public void textValidation() {
        assertEquals("CipherWorld", mGenerator.getValidText("Cipher World"));
        assertEquals("", mGenerator.getValidText("034-+ ятщу87рп=./*"));
        assertEquals("aSampleSentence", mGenerator.getValidText("a Sample Sentence"));
    }

    @Test
    public void gettingDelimiters() {
        String s;
        List<Integer> delimiters;

        s = "CipherWorld";
        delimiters = mGenerator.getDelimitersInValidText(s);
        assertEquals(0, delimiters.size());

        s = "Cipher World";
        delimiters = mGenerator.getDelimitersInValidText(s);
        assertEquals(1, delimiters.size());
        assertEquals(5, delimiters.get(0).intValue());

        s = " Cipher World";
        delimiters = mGenerator.getDelimitersInValidText(s);
        assertEquals(2, delimiters.size());
        assertEquals(-1, delimiters.get(0).intValue());
        assertEquals(5, delimiters.get(1).intValue());

        s = "Cip her W or  l   d";
        delimiters = mGenerator.getDelimitersInValidText(s);
        assertEquals(5, delimiters.size());
        assertEquals(2, delimiters.get(0).intValue());
        assertEquals(5, delimiters.get(1).intValue());
        assertEquals(6, delimiters.get(2).intValue());
        assertEquals(8, delimiters.get(3).intValue());
        assertEquals(9, delimiters.get(4).intValue());
    }
}
