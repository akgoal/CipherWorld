package com.deakishin.cipherworld.model.cipherstorage.impl.initialdataloader;

import android.util.Log;

import com.deakishin.cipherworld.model.assets.AssetsHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for loading initial data from a .json file in the project resources.
 */
public class InitialDataLoaderImpl implements InitialDataLoader {

    private final String TAG = getClass().getSimpleName();

    // Helper for working with assets.
    private AssetsHelper mAssetsHelper;

    /**
     * Parameters for reading data from JSON object.
     */
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

    // Mapping between versions and names of files with initial data.
    private static final Map<Integer, String> FILENAME_MAP = new HashMap<>();

    static {
        FILENAME_MAP.put(1, "ciphers.json");
        FILENAME_MAP.put(2, "ciphers_2.json");
    }

    public InitialDataLoaderImpl(AssetsHelper assetsHelper) {
        mAssetsHelper = assetsHelper;
    }

    @Override
    public void loadData(int version, LoadedItemHandler handler) {
        String filename = FILENAME_MAP.get(version);
        if (filename == null) {
            return;
        }

        JSONObject jsonData;
        try {
            jsonData = new JSONObject(mAssetsHelper.loadText(filename));
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

                Log.i(TAG, "Loaded initial data for a cipher"
                        + ". Version: " + version
                        + ". Id:" + id
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
