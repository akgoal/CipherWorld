package com.deakishin.cipherworld.model.cipherstorage.impl;

import android.provider.BaseColumns;

/**
 * Contract for working with the local database.
 */
class LocalDbContract {

    private LocalDbContract() {
    }

    /**
     * Contract for the table of ciphers.
     */
    static abstract class Ciphers implements BaseColumns {
        static final String TABLE_NAME = "ciphers";
        static final String COLUMN_NAME_NUMBER = "number";
        static final String COLUMN_NAME_LEVEL = "level";
        static final String COLUMN_NAME_QUESTION = "question";
        static final String COLUMN_NAME_SOLUTION = "solution";
        static final String COLUMN_NAME_SOLVED = "solved";
        static final String COLUMN_NAME_OPENED_LETTERS = "opened_letters";
        static final String COLUMN_NAME_CURRENT_SOLUTION = "current_solution";
    }
}
