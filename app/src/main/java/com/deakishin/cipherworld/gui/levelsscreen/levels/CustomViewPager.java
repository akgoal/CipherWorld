package com.deakishin.cipherworld.gui.levelsscreen.levels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Custom ViewPager that allows enabling/disabling scrolling pages.
 * It also allows to change duration of the scrolling animation.
 */
public class CustomViewPager extends ViewPager {

    // Flag that indicates whether paging must be enabled or not.
    private boolean mPagingEnabled = true;

    private ScrollerCustomDuration mScroller = null;

    public CustomViewPager(Context context) {
        this(context, null);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        postInitViewPager();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mPagingEnabled && super.onInterceptTouchEvent(event);
    }

    /**
     * Enables/disables paging.
     *
     * @param enabled True if paging must be enabled, false otherwise.
     */
    public void setPagingEnabled(boolean enabled) {
        mPagingEnabled = enabled;
    }


    /**
     * Override the Scroller instance with our own class so we can change the duration
     */
    private void postInitViewPager() {

        try {

            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = viewpager.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            mScroller = new ScrollerCustomDuration(getContext(), (Interpolator) interpolator.get(null));
            scroller.set(this, mScroller);

        } catch (Exception e) {
        }

    }

    /**
     * Sets the factor by which the scroll duration will change.
     */
    public void setScrollDurationFactor(double scrollFactor) {
        mScroller.setScrollDurationFactor(scrollFactor);
    }

    private static class ScrollerCustomDuration extends Scroller {

        private double mScrollFactor = 1;

        public ScrollerCustomDuration(Context context) {
            super(context);
        }

        public ScrollerCustomDuration(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @SuppressLint("NewApi")
        public ScrollerCustomDuration(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        /**
         * Sets the factor by which the duration will change
         */
        void setScrollDurationFactor(double scrollFactor) {
            mScrollFactor = scrollFactor;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, (int) (duration * mScrollFactor));
        }

    }
}
