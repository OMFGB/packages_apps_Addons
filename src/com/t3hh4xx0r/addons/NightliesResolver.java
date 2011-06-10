package com.t3hh4xx0r.addons;

import android.content.Intent;

import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

import android.os.Bundle;
import android.os.Build;

import android.util.Slog;

public class NightliesResolver extends PreferenceActivity {
        private static final String TAG = "god_mode.Nightlys";

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        
        // What i will do here is pass an intent to Nightlies.Java
        // that will tell it which JSON file to download and use
        // We can optionally add a cache location

Intent intent = new Intent(Intent.ACTION_MAIN);
	intent.setClassName("com.t3hh4xx0r.addons", "com.t3hh4xx0r.addons.nightlies.Nightlies");
	if ((Build.MODEL.equals("Incredible"))) {
	intent.putExtra("DownloadScript", "inc.js");
	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	startActivity(intent);
	
	} else if ((Build.MODEL.equals("Eris"))) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        } else if ((Build.MODEL.equals("Evo"))) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        } else if ((Build.MODEL.equals("Hero"))) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

	}
    }
}

