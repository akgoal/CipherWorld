package com.deakishin.cipherworld.app;

import android.app.Application;

import com.deakishin.cipherworld.dagger.AppComponent;
import com.deakishin.cipherworld.dagger.AppModule;
import com.deakishin.cipherworld.dagger.DaggerAppComponent;

/**
 * Extension of the Application class to provide access to an AppComponent for committing dependency injection.
 */
public class CipherWorldApplication extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = initDagger(this);
    }

    /**
     * Initializes Dagger for dependency injection.
     *
     * @param application Custom application object.
     */
    protected AppComponent initDagger(CipherWorldApplication application) {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(application))
                .build();
    }

    /**
     * @return AppComponent for dependency injection.
     */
    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
