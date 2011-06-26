package com.barryku.android.instapaper;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class InstapaperPreference extends PreferenceActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.layout.prefs);
    }
}
