package com.deakishin.cipherworld.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.deakishin.cipherworld.R;

/**
 * Activity that hosts a single fragment.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    /**
     * Returns a fragment that the activity has to host.
     *
     * @return fragment to host.
     */
    protected abstract Fragment createFragment();

    /**
     * Fragment that's being hosted.
     */
    protected Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        FragmentManager fm = getSupportFragmentManager();
        mFragment = fm.findFragmentById(R.id.fragmentContainer);
        if (mFragment == null) {
            mFragment = createFragment();
            fm.beginTransaction().add(R.id.fragmentContainer, mFragment).commit();
        }
    }
}
