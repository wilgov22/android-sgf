package com.sgf.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.sgf.activities.R;

public class PreferencesActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.main_preferences);
	}
}
