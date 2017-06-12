package com.deakishin.cipherworld.model.cipherstorage.impl.initialdataloader;

import android.util.Log;

import com.deakishin.cipherworld.model.assets.AssetsHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class for loading initial data from a .json file in the project resources.
 */
public class InitialDataLoaderImpl implements InitialDataLoader {

    private final String TAG = getClass().getSimpleName();

    // Name of the file where ciphers are stored.
    private static final String FILENAME = "ciphers.json";

    // Helper for working with assets.
    private AssetsHelper mAssetsHelper;

    // Parameters for reading data from JSON object.
    private static class PARAMS {
        private static final String COUNT = "count";
        private static final String CIPHERS = "ciphers";

        private static final String CIPHER_ID = "id";
        private static final String CIPHER_LEVEL = "lvl";
        private static final String CIPHER_NUMBER = "num";
        private static final String CIPHER_DESCRIPTION = "descr";
        private static final String CIPHER_SOLUTION = "sol";
        private static final String CIPHER_OPENED_LETTERS = "opnd";
    }

    public InitialDataLoaderImpl(AssetsHelper assetsHelper) {
        mAssetsHelper = assetsHelper;
    }

    @Override
    public void loadData(LoadedItemHandler handler) {
        JSONObject jsonData;
        try {
            jsonData = new JSONObject(mAssetsHelper.loadText(FILENAME));
        } catch (JSONException e) {
            Log.e(TAG, "Unable to load initial ciphers' data: " + e);
            return;
        }

        try {
            int count = jsonData.getInt(PARAMS.COUNT);
            JSONArray jsonCiphersList = jsonData.getJSONArray(PARAMS.CIPHERS);
            for (int i = 0; i < count; i++) {
                JSONObject jsonCipher = jsonCiphersList.getJSONObject(i);
                int id = jsonCipher.getInt(PARAMS.CIPHER_ID);
                int level = jsonCipher.getInt(PARAMS.CIPHER_LEVEL);
                int number = jsonCipher.getInt(PARAMS.CIPHER_NUMBER);
                String description = jsonCipher.getString(PARAMS.CIPHER_DESCRIPTION);
                String solution = jsonCipher.getString(PARAMS.CIPHER_SOLUTION);
                String openedLetters = jsonCipher.getString(PARAMS.CIPHER_OPENED_LETTERS);

                Log.i(TAG, "Loaded initial data for a cipher."
                        + " Id:" + id
                        + ". Level:" + level
                        + ". Number:" + number
                        + ". Description:(" + description + ")"
                        + ". Solution:(" + solution + ")"
                        + ". OpenedLetters:(" + openedLetters + ")");

                handler.onCipherLoaded(id, level, number, description, solution, openedLetters);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error loading initial ciphers' data: " + e);
        }
    }
}
