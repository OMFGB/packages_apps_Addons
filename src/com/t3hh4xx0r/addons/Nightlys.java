package com.t3hh4xx0r.addons;

import android.content.Intent;

import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

import android.os.Bundle;
import android.os.Build;

import android.util.Slog;

public class Nightlys extends PreferenceActivity {
        private static final String TAG = "god_mode.Nightlys";

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);


	if ((Build.MODEL.equals("Incredible"))) {
	Intent intent = new Intent(Intent.ACTION_MAIN);
	intent.setClassName("com.t3hh4xx0r.addons", "com.t3hh4xx0r.addons.IncNightlys");
	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	startActivity(intent);
	
	} else if ((Build.MODEL.equals("Eris"))) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName("com.t3hh4xx0r.addons", "com.t3hh4xx0r.addons.DesirecNightlys");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        } else if ((Build.MODEL.equals("Evo"))) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName("com.t3hh4xx0r.addons", "com.t3hh4xx0r.addons.SupersonicNightlys");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        } else if ((Build.MODEL.equals("Hero"))) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName("com.t3hh4xx0r.addons", "com.t3hh4xx0r.addons.HerocNightlys");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

	}
    }
}

