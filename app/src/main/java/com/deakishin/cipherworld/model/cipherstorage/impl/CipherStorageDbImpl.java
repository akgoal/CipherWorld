package com.deakishin.cipherworld.model.cipherstorage.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.deakishin.cipherworld.model.cipherstorage.CipherInfo;
import com.deakishin.cipherworld.model.cipherstorage.CipherShortInfo;
import com.deakishin.cipherworld.model.cipherstorage.CipherStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation of the cipher storage interface that uses a local database for persistence.
 */
public class CipherStorageDbImpl implements CipherStorage {

    private final String TAG = getClass().getSimpleName();

    // Object for executing queries to the local database.
    private SQLiteDatabase mSQLiteDatabase;

    // Listeners to data changes.
    private List<ChangesListener> mListeners = new ArrayList<>();

    public CipherStorageDbImpl(LocalDbHelper localDbHelper) {
        mSQLiteDatabase = localDbHelper.getWritableDatabase();
    }

    @Override
    public List<CipherShortInfo> getCiphersForLevel(int levelNumber) {
        Log.i(TAG, "Executing query for ciphers of the level. Level: " + levelNumber);

        List<CipherShortInfo> cipherList = new ArrayList<>();

        String[] projection = {
                LocalDbContract.Ciphers._ID,
                LocalDbContract.Ciphers.COLUMN_NAME_NUMBER,
                LocalDbContract.Ciphers.COLUMN_NAME_SOLVED
        };
        String selection = LocalDbContract.Ciphers.COLUMN_NAME_LEVEL + " = ?";
        String[] selectionArgs = new String[]{Integer.toString(levelNumber)};
        String orderBy = LocalDbContract.Ciphers.COLUMN_NAME_NUMBER + " ASC";

        Cursor c = mSQLiteDatabase.query(LocalDbContract.Ciphers.TABLE_NAME, projection,
                selection, selectionArgs, null, null, orderBy);
        int idIdx = c.getColumnIndex(LocalDbContract.Ciphers._ID);
        int numIdx = c.getColumnIndex(LocalDbContract.Ciphers.COLUMN_NAME_NUMBER);
        int solvedIdx = c.getColumnIndex(LocalDbContract.Ciphers.COLUMN_NAME_SOLVED);

        while (c.moveToNext()) {
            CipherShortInfo cipher = new CipherShortInfo();
            cipher.setId(c.isNull(idIdx) ? -1 : c.getInt(idIdx));
            cipher.setNumber(c.isNull(numIdx) ? -1 : c.getInt(numIdx));
            cipher.setSolved(!c.isNull(solvedIdx) && c.getInt(solvedIdx) > 0);
            cipher.setLevel(levelNumber);
            cipherList.add(cipher);
        }
        c.close();

        return cipherList;
    }

    @Override
    public int[] getCiphersSolvedCountForLevel(int levelNumber) {
        String selection = LocalDbContract.Ciphers.COLUMN_NAME_LEVEL + " = ?";
        String[] selectionArgs = new String[]{Integer.toString(levelNumber)};
        int total = (int) DatabaseUtils.queryNumEntries(mSQLiteDatabase, LocalDbContract.Ciphers.TABLE_NAME,
                selection, selectionArgs);

        selection += " AND " + LocalDbContract.Ciphers.COLUMN_NAME_SOLVED + " = ?";
        selectionArgs = new String[]{Integer.toString(levelNumber), "1"};
        int solved = (int) DatabaseUtils.queryNumEntries(mSQLiteDatabase, LocalDbContract.Ciphers.TABLE_NAME,
                selection, selectionArgs);

        return new int[]{total, solved};
    }

    @Override
    public CipherInfo getCipher(int cipherId) {
        Log.i(TAG, "Executing query for a cipher for the given id. Id: " + cipherId);

        CipherInfo cipher = null;

        String[] projection = {
                LocalDbContract.Ciphers._ID,
                LocalDbContract.Ciphers.COLUMN_NAME_NUMBER,
                LocalDbContract.Ciphers.COLUMN_NAME_LEVEL,
                LocalDbContract.Ciphers.COLUMN_NAME_QUESTION,
                LocalDbContract.Ciphers.COLUMN_NAME_SOLUTION,
                LocalDbContract.Ciphers.COLUMN_NAME_SOLVED,
                LocalDbContract.Ciphers.COLUMN_NAME_OPENED_LETTERS,
                LocalDbContract.Ciphers.COLUMN_NAME_CURRENT_SOLUTION,
                LocalDbContract.Ciphers.COLUMN_NAME_DELIMITERS_OPENED
        };
        String selection = LocalDbContract.Ciphers._ID + " = ?";
        String[] selectionArgs = new String[]{Integer.toString(cipherId)};

        Cursor c = mSQLiteDatabase.query(LocalDbContract.Ciphers.TABLE_NAME, projection,
                selection, selectionArgs, null, null, null);
        int idIdx = c.getColumnIndex(LocalDbContract.Ciphers._ID);
        int numIdx = c.getColumnIndex(LocalDbContract.Ciphers.COLUMN_NAME_NUMBER);
        int levelIdx = c.getColumnIndex(LocalDbContract.Ciphers.COLUMN_NAME_LEVEL);
        int questionIdx = c.getColumnIndex(LocalDbContract.Ciphers.COLUMN_NAME_QUESTION);
        int solutionIdx = c.getColumnIndex(LocalDbContract.Ciphers.COLUMN_NAME_SOLUTION);
        int solvedIdx = c.getColumnIndex(LocalDbContract.Ciphers.COLUMN_NAME_SOLVED);
        int openedLettersIdx = c.getColumnIndex(LocalDbContract.Ciphers.COLUMN_NAME_OPENED_LETTERS);
        int currentSolutionIdx = c.getColumnIndex(LocalDbContract.Ciphers.COLUMN_NAME_CURRENT_SOLUTION);

        int delimitersOpenedIdx = c.getColumnIndex(LocalDbContract.Ciphers.COLUMN_NAME_DELIMITERS_OPENED);

        if (c.moveToFirst()) {
            cipher = new CipherInfo();
            CipherShortInfo shortInfo = new CipherShortInfo();
            shortInfo.setId(c.isNull(idIdx) ? -1 : c.getInt(idIdx));
            shortInfo.setNumber(c.isNull(numIdx) ? -1 : c.getInt(numIdx));
            shortInfo.setSolved(!c.isNull(solvedIdx) && c.getInt(solvedIdx) > 0);
            shortInfo.setLevel(c.isNull(levelIdx) ? -1 : c.getInt(levelIdx));
            cipher.setShortInfo(shortInfo);

            cipher.setQuestion(c.getString(questionIdx));
            cipher.setSolution(c.getString(solutionIdx));

            cipher.setOpenedLetters(c.getString(openedLettersIdx));

            cipher.setCurrentSolution(c.getString(currentSolutionIdx));

            cipher.setDelimiterOpened(!c.isNull(delimitersOpenedIdx) && c.getInt(delimitersOpenedIdx) > 0);
        }
        c.close();

        return cipher;
    }

    @Override
    public void setCipherSolved(int cipherId) {
        Log.i(TAG, "Executing query for setting a cipher as solved. Cipher's id: " + cipherId);

        String selection = LocalDbContract.Ciphers._ID + " = ?";
        String[] selectionArgs = new String[]{Integer.toString(cipherId)};

        ContentValues cv = new ContentValues();
        cv.put(LocalDbContract.Ciphers.COLUMN_NAME_SOLVED, 1);

        mSQLiteDatabase.update(LocalDbContract.Ciphers.TABLE_NAME, cv, selection, selectionArgs);

        notifyListeners();
    }

    @Override
    public void setCipherOpenedLetters(int cipherId, String openedLetters) {
        Log.i(TAG, "Executing query for updating openedLetters for a cipher. Cipher's id: " + cipherId
                + ". Opened letters: " + openedLetters);

        String selection = LocalDbContract.Ciphers._ID + " = ?";
        String[] selectionArgs = new String[]{Integer.toString(cipherId)};

        ContentValues cv = new ContentValues();
        cv.put(LocalDbContract.Ciphers.COLUMN_NAME_OPENED_LETTERS, openedLetters);

        mSQLiteDatabase.update(LocalDbContract.Ciphers.TABLE_NAME, cv, selection, selectionArgs);
    }

    @Override
    public void setCipherCurrentSolution(int cipherId, String currentSolution) {
        Log.i(TAG, "Executing query for updating currentSolution for a cipher. Cipher's id: " + cipherId
                + ". Current solution: " + currentSolution);

        String selection = LocalDbContract.Ciphers._ID + " = ?";
        String[] selectionArgs = new String[]{Integer.toString(cipherId)};

        ContentValues cv = new ContentValues();
        cv.put(LocalDbContract.Ciphers.COLUMN_NAME_CURRENT_SOLUTION, currentSolution);

        mSQLiteDatabase.update(LocalDbContract.Ciphers.TABLE_NAME, cv, selection, selectionArgs);
    }

    @Override
    public void setDelimitersOpened(int cipherId) {
        Log.i(TAG, "Executing query for setting delimitersOpened to true for a cipher. Cipher's id: " + cipherId);

        String selection = LocalDbContract.Ciphers._ID + " = ?";
        String[] selectionArgs = new String[]{Integer.toString(cipherId)};

        ContentValues cv = new ContentValues();
        cv.put(LocalDbContract.Ciphers.COLUMN_NAME_DELIMITERS_OPENED, 1);

        mSQLiteDatabase.update(LocalDbContract.Ciphers.TABLE_NAME, cv, selection, selectionArgs);
    }

    @Override
    public int getLevelCount() {
        return LocalDbHelper.LEVEL_COUNT;
    }

    // Notifies listeners.
    private void notifyListeners() {
        for (ChangesListener listener : mListeners) {
            listener.onChange();
        }
    }

    @Override
    public void addChangesListener(ChangesListener listener) {
        mListeners.add(listener);
    }

    @Override
    public void removeChangesListener(ChangesListener listener) {
        mListeners.remove(listener);
    }
}
