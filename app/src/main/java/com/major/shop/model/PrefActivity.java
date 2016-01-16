package com.major.shop.model;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.major.shop.R;

/**
 * Created by Sergey on 08.01.14.
 */
public class PrefActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
