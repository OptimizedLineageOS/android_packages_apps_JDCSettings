/*
 * Copyright (C) 2016 The Pure Nexus Project
 * Copyright (C) 2016 The JDCTeam
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdc.settings;

import android.app.Activity;
import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.provider.Settings;
import android.os.Bundle;
import android.os.UserHandle;
import android.app.Fragment;
import android.support.v7.preference.ListPreference;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceScreen;
import android.text.TextUtils;
import android.view.View;

import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.SettingsPreferenceFragment;
import android.content.pm.PackageManager;

public class JDCSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener{

    public static final String BACKKILL_TIMEOUT_MILLI = "backkill_timeout_milli";
    private static final String PREF_ROWS_PORTRAIT = "qs_rows_portrait";
    private static final String PREF_ROWS_LANDSCAPE = "qs_rows_landscape";
    private static final String PREF_COLUMNS = "qs_columns";
	
    private ListPreference mBackKillDuration;
    private ListPreference mRowsPortrait;
    private ListPreference mRowsLandscape;
    private ListPreference mQsColumns;
	
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.jdc_settings_main);
        if (!isAppInstalled("eu.chainfire.supersu"))
        {
            final Preference preference = findPreference("supersu_settings");
            getPreferenceScreen().removePreference(preference);
        }
	
	final ContentResolver resolver = getContentResolver();
        final PreferenceScreen prefSet = getPreferenceScreen();

	mBackKillDuration = (ListPreference) prefSet.findPreference(BACKKILL_TIMEOUT_MILLI);
        int duration = Settings.System.getIntForUser(resolver,
                Settings.System.BACKKILL_TIMEOUT_MILLI, 3, UserHandle.USER_CURRENT);
        mBackKillDuration.setValue(String.valueOf(duration));
        mBackKillDuration.setSummary(mBackKillDuration.getEntry());
        mBackKillDuration.setOnPreferenceChangeListener(this);
	
	 mRowsPortrait = (ListPreference) findPreference(PREF_ROWS_PORTRAIT);
        int rowsPortrait = Settings.Secure.getInt(resolver,
                Settings.Secure.QS_ROWS_PORTRAIT, 3);
        mRowsPortrait.setValue(String.valueOf(rowsPortrait));
        mRowsPortrait.setSummary(mRowsPortrait.getEntry());
        mRowsPortrait.setOnPreferenceChangeListener(this);

        int defaultValue = getResources().getInteger(com.android.internal.R.integer.config_qs_num_rows_landscape_default);
        mRowsLandscape = (ListPreference) findPreference(PREF_ROWS_LANDSCAPE);
        int rowsLandscape = Settings.Secure.getInt(resolver,
                Settings.Secure.QS_ROWS_LANDSCAPE, defaultValue);
        mRowsLandscape.setValue(String.valueOf(rowsLandscape));
        mRowsLandscape.setSummary(mRowsLandscape.getEntry());
        mRowsLandscape.setOnPreferenceChangeListener(this);

        mQsColumns = (ListPreference) findPreference(PREF_COLUMNS);
        int columnsQs = Settings.Secure.getInt(resolver,
                Settings.Secure.QS_COLUMNS, 3);
        mQsColumns.setValue(String.valueOf(columnsQs));
        mQsColumns.setSummary(mQsColumns.getEntry());
        mQsColumns.setOnPreferenceChangeListener(this);
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.JDC_SETTINGS;
    }
    
    @Override	
    public boolean onPreferenceChange(Preference preference, Object newValue){
       ContentResolver resolver = getActivity().getContentResolver();
	int intValue;
        int index2;
       if (preference == mBackKillDuration) {
		int duration = Integer.valueOf((String) newValue);
		int index = mBackKillDuration.findIndexOfValue((String) newValue);
		Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.BACKKILL_TIMEOUT_MILLI, duration, UserHandle.USER_CURRENT);
            mBackKillDuration.setSummary(mBackKillDuration.getEntries()[index]);
            return true;
	} else if (preference == mRowsPortrait) {
            intValue = Integer.valueOf((String) newValue);
            index2 = mRowsPortrait.findIndexOfValue((String) newValue);
            Settings.Secure.putInt(resolver,
                    Settings.Secure.QS_ROWS_PORTRAIT, intValue);
            preference.setSummary(mRowsPortrait.getEntries()[index2]);
            return true;
        } else if (preference == mRowsLandscape) {
            intValue = Integer.valueOf((String) newValue);
            index2 = mRowsLandscape.findIndexOfValue((String) newValue);
            Settings.Secure.putInt(resolver,
                    Settings.Secure.QS_ROWS_LANDSCAPE, intValue);
            preference.setSummary(mRowsLandscape.getEntries()[index2]);
            return true;
        } else if (preference == mQsColumns) {
            intValue = Integer.valueOf((String) newValue);
            index2 = mQsColumns.findIndexOfValue((String) newValue);
            Settings.Secure.putInt(resolver,
                    Settings.Secure.QS_COLUMNS, intValue);
            preference.setSummary(mQsColumns.getEntries()[index2]);
            return true;
	}
        return false;
    }
    
    private boolean isAppInstalled(String uri)
    {
        try
        {
            getContext().getPackageManager().getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
        }
        return false;
    }
}
