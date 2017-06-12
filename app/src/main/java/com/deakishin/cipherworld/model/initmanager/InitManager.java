package com.deakishin.cipherworld.model.initmanager;

/**
 * Interface for a manager that initializes and sets up all components.
 */
public interface InitManager {

    /**
     * Initializes and sets up all components in the app.
     */
    void init();

    /**
     * @return True if all components are initialized, false otherwise.
     */
    boolean isInitialized();

    /**
     * Listener interface for getting notified when the components are initialized.
     */
    interface OnInitListener {
        /**
         * Invoked when all components are initialized.
         */
        void onInit();
    }

    /**
     * Adds a listener that will be notified when all components are initialized.
     * Once components are initialized and the listener in notified,
     * the listener gets removed.
     *
     * @param listener Listener to add.
     */
    void addOnInitListener(OnInitListener listener);

    /**
     * Removes a listener.
     *
     * @param listener Listener to remove.
     */
    void removeOnInitListener(OnInitListener listener);
}
