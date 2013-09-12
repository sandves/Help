package com.sandves.help;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {//implements OnSharedPreferenceChangeListener 
	
	public static final String KEY_PREF_GEOPARSE = "pref_geoparse";
	public static final String KEY_PREF_CALL = "pref_emg_number";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	/*@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(KEY_PREF_GEOPARSE)) {
            Preference geoparsePref = findPreference(key);
            // Set summary to be the user-description for the selected value
            geoparsePref.setSummary(sharedPreferences.getString(key, ""));
        }
		if (key.equals(KEY_PREF_CALL)) {
            Preference geoparsePref = findPreference(key);
            // Set summary to be the user-description for the selected value
            geoparsePref.setSummary(sharedPreferences.getString(key, ""));
        }
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}*/
}
