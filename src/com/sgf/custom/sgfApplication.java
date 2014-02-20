package com.sgf.custom;

import java.util.Map;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class sgfApplication extends Application {
	private final String TAG= "SGF_APPLICATION";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(TAG,"Application Started");
		Log.d(TAG,"Reading Preferences");
		
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		if(!preferences.contains("sgf_api_host")){
			Editor editor=preferences.edit();
			editor.putString("sgf_api_host", "http://sgf.luisduarte.net");
			editor.commit();
		}

		//Ler preferencias

		Map<String, ?> l=preferences.getAll();
		String res=l.toString();
		l.size();
	}
}
