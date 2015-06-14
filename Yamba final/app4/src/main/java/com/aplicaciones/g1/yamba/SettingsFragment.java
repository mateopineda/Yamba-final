package com.aplicaciones.g1.yamba;




import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Created by PINEDA on 17/05/2015.
 */

public class SettingsFragment extends PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener { //
    private SharedPreferences prefs;
    @Override
    public void onCreate(Bundle savedInstanceState) { //
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings); //
    }
    @Override
    public void onStart() {
        super.onStart();
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
