package com.deakishin.cipherworld.model.assets;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Helper for loading raw resources.
 */
public class AssetsHelperImpl implements AssetsHelper {

    private final String TAG = getClass().getSimpleName();

    // Object for working with assets.
    private AssetManager mAssets;

    public AssetsHelperImpl(AssetManager assets) {
        mAssets = assets;
    }

    @Override
    public Bitmap loadBitmap(String filename) {
        Log.i(TAG, "Loading image asset. Filename: " + filename);

        InputStream is = null;
        try {
            is = loadAsset(filename);
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            Log.e(TAG, "Error loading image asset (filename: " + filename + "): " + e);
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    @Override
    public String loadText(String filename) {
        Log.i(TAG, "Loading text asset. Filename: " + filename);

        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = loadAsset(filename);
            reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error loading text asset (filename: " + filename + "): " + e);
            return null;
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    // Loads assets with the given filename.
    private InputStream loadAsset(String filename) throws IOException {
        return mAssets.open(filename);
    }
}
