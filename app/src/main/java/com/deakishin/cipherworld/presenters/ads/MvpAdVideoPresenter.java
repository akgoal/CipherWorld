package com.deakishin.cipherworld.presenters.ads;

/**
 * MVP Presenter interface for working with video ads.
 */
public interface MvpAdVideoPresenter {
    /**
     * Invoked when the user watched an ad video and now can be rewarded for it.
     */
    void onVideoAdWatched();
}
