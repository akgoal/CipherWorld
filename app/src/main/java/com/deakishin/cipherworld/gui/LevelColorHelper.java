package com.deakishin.cipherworld.gui;

import android.content.Context;
import android.graphics.Color;

import com.deakishin.cipherworld.R;

import butterknife.BindArray;

/**
 * Helper singleton class that provides colors for different levels.
 */
public class LevelColorHelper {

    // Arrays for background and status bar colors for different levels.
    int[] mBgColors, mStatusBarColors;

    private static LevelColorHelper sLevelColorHelper;

    /**
     * @param context Application context.
     * @return Object that provides level colors.
     */
    public static LevelColorHelper getInstance(Context context) {
        if (sLevelColorHelper == null) {
            sLevelColorHelper = new LevelColorHelper(context.getApplicationContext());
        }
        return sLevelColorHelper;
    }

    private LevelColorHelper(Context context) {
        String bgColors[] = context.getResources().getStringArray(R.array.levelBackgroundArray);
        mBgColors = new int[bgColors.length];
        for (int i = 0; i < bgColors.length; i++) {
            mBgColors[i] = Color.parseColor(bgColors[i]);
        }

        String sbColors[] = context.getResources().getStringArray(R.array.levelStatusBarArray);
        mStatusBarColors = new int[sbColors.length];
        for (int i = 0; i < sbColors.length; i++) {
            mStatusBarColors[i] = Color.parseColor(sbColors[i]);
        }
    }

    /**
     * @param pos Position (starting with 0).
     * @return Background color for the specified position in the sequence of levels.
     */
    public int getBackgroundColor(int pos) {
        return mBgColors[pos % mBgColors.length];
    }

    /**
     * @param pos Position (starting with 0).
     * @return Status bar color for the specified position in the sequence of levels.
     */
    public int getStatusBarColor(int pos) {
        return mStatusBarColors[pos % mStatusBarColors.length];
    }
}
