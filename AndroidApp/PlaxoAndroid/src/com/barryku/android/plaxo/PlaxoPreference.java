package com.barryku.android.plaxo;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PlaxoPreference extends PreferenceActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.plaxo_pref);
    }
}
