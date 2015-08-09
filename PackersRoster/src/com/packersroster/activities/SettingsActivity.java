package com.packersroster.activities;

import com.example.packersroster.R;
import com.example.packersroster.R.layout;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.util.Log;

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
	
    public static class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.layout.preferences);
            
            SettingsFragment.setSummary(findPreference("website_pref"));
            SettingsFragment.setSummary(findPreference("website_mlb_pref"));
            SettingsFragment.setSummary(findPreference("website_nba_pref"));
        }
        
        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        }

        @Override
        public void onPause() {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }

        public static void setSummary(Preference pref) {
            if (pref instanceof ListPreference) {
                ListPreference listPref = (ListPreference) pref;
                pref.setSummary(listPref.getEntry());
            }
        }

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			SettingsFragment.setSummary(findPreference(key));
		}
    }

}
