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

import android.os.Bundle;
import android.support.v7.preference.Preference;

import com.android.internal.logging.MetricsProto.MetricsEvent;

import com.android.settings.SettingsPreferenceFragment;
import android.content.pm.PackageManager;

public class JDCSettings extends SettingsPreferenceFragment {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.jdc_settings_main);
        if (!isAppInstalled("eu.chainfire.supersu"))
        {
            final Preference preference = findPreference("supersu_settings");
            getPreferenceScreen().removePreference(preference);
        }
        if (!isAppInstalled("com.gokhanmoral.stweaks.app"))
        {
            final Preference preference = findPreference("stweaks_settings");
            getPreferenceScreen().removePreference(preference);
        }
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.JDC_SETTINGS;
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
