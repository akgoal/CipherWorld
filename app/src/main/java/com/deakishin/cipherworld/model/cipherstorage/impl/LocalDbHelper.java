package com.deakishin.cipherworld.model.cipherstorage.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.deakishin.cipherworld.model.cipherstorage.impl.initialdataloader.InitialDataLoader;

import java.util.Random;

/**
 * Helper for opening the local database.
 */
public class LocalDbHelper extends SQLiteOpenHelper {

    private final String TAG = getClass().getSimpleName();

    // Number of levels.
    static final int LEVEL_COUNT = 5;

    // DB version.
    // TODO Fix database version.
    private static final int DATABASE_VERSION = 4;
    // Db name.
    private static final String DATABASE_NAME = "LocalDb.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_CIPHERS =
            "CREATE TABLE " + LocalDbContract.Ciphers.TABLE_NAME + " (" +
                    LocalDbContract.Ciphers._ID + INT_TYPE + " PRIMARY KEY," +
                    LocalDbContract.Ciphers.COLUMN_NAME_NUMBER + INT_TYPE + COMMA_SEP +
                    LocalDbContract.Ciphers.COLUMN_NAME_LEVEL + INT_TYPE + COMMA_SEP +
                    LocalDbContract.Ciphers.COLUMN_NAME_QUESTION + TEXT_TYPE + COMMA_SEP +
                    LocalDbContract.Ciphers.COLUMN_NAME_SOLUTION + TEXT_TYPE + COMMA_SEP +
                    LocalDbContract.Ciphers.COLUMN_NAME_SOLVED + INT_TYPE + COMMA_SEP +
                    LocalDbContract.Ciphers.COLUMN_NAME_OPENED_LETTERS + TEXT_TYPE + COMMA_SEP +
                    LocalDbContract.Ciphers.COLUMN_NAME_CURRENT_SOLUTION + TEXT_TYPE + " )";


    private static final String SQL_DELETE_CIPHERS =
            "DROP TABLE IF EXISTS " + LocalDbContract.Ciphers.TABLE_NAME;

    // Helper for loading initial data.
    private InitialDataLoader mInitialDataLoader;

    public LocalDbHelper(Context context, InitialDataLoader initialDataLoader) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mInitialDataLoader = initialDataLoader;
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        Log.i(TAG, "Creating local database");
        db.execSQL(SQL_CREATE_CIPHERS);

        // TODO Remove demo cipher.
        loadDemoCipher(db);

        mInitialDataLoader.loadData(new InitialDataLoader.LoadedItemHandler() {
            @Override
            public void onCipherLoaded(int cipherId, int level, int number, String description, String solution, String openedLetters) {
                ContentValues cv = new ContentValues();

                cv.put(LocalDbContract.Ciphers._ID, cipherId);
                cv.put(LocalDbContract.Ciphers.COLUMN_NAME_NUMBER, number);
                cv.put(LocalDbContract.Ciphers.COLUMN_NAME_LEVEL, level);
                cv.put(LocalDbContract.Ciphers.COLUMN_NAME_QUESTION, description);
                cv.put(LocalDbContract.Ciphers.COLUMN_NAME_SOLUTION, solution);

                cv.put(LocalDbContract.Ciphers.COLUMN_NAME_OPENED_LETTERS, openedLetters);

                cv.put(LocalDbContract.Ciphers.COLUMN_NAME_SOLVED, 0);

                db.insert(LocalDbContract.Ciphers.TABLE_NAME, null, cv);
            }
        });

    }

    // Loads ciphers for testing.
    private void loadStubCiphers(SQLiteDatabase db) {
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            ContentValues cv = new ContentValues();
            cv.put(LocalDbContract.Ciphers.COLUMN_NAME_NUMBER, i % 20 + 1);
            cv.put(LocalDbContract.Ciphers.COLUMN_NAME_LEVEL, i / 20 + 1);
            StringBuilder sb = new StringBuilder();
            sb.append("Lalala: ");
            for (int j = 0; j < i + 1; j++) {
                sb.append("Cipher №");
                sb.append(i + 1);
                sb.append(" ");
            }
            cv.put(LocalDbContract.Ciphers.COLUMN_NAME_QUESTION, sb.toString());
            StringBuilder cipherTextBuilder = new StringBuilder();
            for (int j = 0; j < 100; j++) {
                cipherTextBuilder.append("Cipher ");
            }
            cv.put(LocalDbContract.Ciphers.COLUMN_NAME_SOLUTION, cipherTextBuilder.toString());
            cv.put(LocalDbContract.Ciphers.COLUMN_NAME_SOLVED, rand.nextFloat() > 0.5 ? 1 : 0);
            db.insert(LocalDbContract.Ciphers.TABLE_NAME, null, cv);
        }
    }

    // Loads demonstration cipher.
    private void loadDemoCipher(SQLiteDatabase db) {
        for (int i = 0; i < 15; i++) {
            ContentValues cv = new ContentValues();
            cv.put(LocalDbContract.Ciphers._ID, i + 9000);
            cv.put(LocalDbContract.Ciphers.COLUMN_NAME_NUMBER, 0);
            cv.put(LocalDbContract.Ciphers.COLUMN_NAME_LEVEL, 1);
            cv.put(LocalDbContract.Ciphers.COLUMN_NAME_QUESTION, "Reddit.");
            cv.put(LocalDbContract.Ciphers.COLUMN_NAME_SOLUTION, "The front page of the internet");
            cv.put(LocalDbContract.Ciphers.COLUMN_NAME_OPENED_LETTERS, "TNRG");
            cv.put(LocalDbContract.Ciphers.COLUMN_NAME_SOLVED, 0);
            db.insert(LocalDbContract.Ciphers.TABLE_NAME, null, cv);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_CIPHERS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}