package com.barryku.android.tool;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class MyPreference extends PreferenceActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.layout.prefs);
    }
}
