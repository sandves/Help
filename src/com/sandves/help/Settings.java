package com.sandves.help;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Settings {

	public static int getPref(Context context, String key, String defaultValue) {
		int value = 0;
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String pref = sharedPref.getString(key, defaultValue);
		if(!pref.equals("") || pref!=null)
			try {
				value = Integer.parseInt(pref);
			} catch (NumberFormatException e) {
				Toast.makeText(context, "Error: NumberFormatException", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		return value;
	}

}
