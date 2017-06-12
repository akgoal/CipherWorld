package com.deakishin.cipherworld.model.assets;

import android.graphics.Bitmap;

/**
 * Interface for working with raw resources.
 */
public interface AssetsHelper {

    /**
     * Loads Bitmap image.
     *
     * @param filename Name of the file with the image.
     * @return Loaded image, or null if the image is not found / cannot be loaded.
     */
    Bitmap loadBitmap(String filename);

    /**
     * Loads text.
     *
     * @param filename Name of the file with the text.
     * @return Loaded text, or null if the file is not found / cannot be loaded.
     */
    String loadText(String filename);
}
