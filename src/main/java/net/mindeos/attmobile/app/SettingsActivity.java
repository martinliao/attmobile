// This file is part of AttMobile - http://att.mindeos.net/
//
// AttMobile is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// AttMobile is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with AttMobile. If not, see <http://www.gnu.org/licenses/>.

package net.mindeos.attmobile.app;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

import net.margaritov.preference.colorpicker.ColorPickerPreference;
import net.mindeos.attmobile.webservice.Att;
import net.mindeos.attmobile.pojos.Status;


/**
 * The activity for the Settings screen.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupSimplePreferencesScreen();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupSimplePreferencesScreen() {
        addPreferencesFromResource(R.xml.settings);

        for(Status s: Att.getAllStatuses()) {
            ColorPickerPreference pr = ((ColorPickerPreference)findPreference("color"+s.getId()));

            pr.setEnabled(true);
            pr.setSummary(s.getDescription());
        }

        ColorPickerPreference colora = ((ColorPickerPreference)findPreference("colora"));
        ColorPickerPreference color1 = ((ColorPickerPreference)findPreference("color1"));
        ColorPickerPreference color2 = ((ColorPickerPreference)findPreference("color2"));
        ColorPickerPreference color3 = ((ColorPickerPreference)findPreference("color3"));
        ColorPickerPreference color4 = ((ColorPickerPreference)findPreference("color4"));
        ColorPickerPreference color5 = ((ColorPickerPreference)findPreference("color5"));
        ColorPickerPreference color6 = ((ColorPickerPreference)findPreference("color6"));
        ColorPickerPreference color7 = ((ColorPickerPreference)findPreference("color7"));
        ColorPickerPreference color8 = ((ColorPickerPreference)findPreference("color8"));

        colora.setValue(Att.getColor(0));
        color1.setValue(Att.getColor(1));
        color2.setValue(Att.getColor(2));
        color3.setValue(Att.getColor(3));
        color4.setValue(Att.getColor(4));
        color5.setValue(Att.getColor(5));
        color6.setValue(Att.getColor(6));
        color7.setValue(Att.getColor(7));
        color8.setValue(Att.getColor(8));

        colora.setOnPreferenceChangeListener(this);
        color1.setOnPreferenceChangeListener(this);
        color2.setOnPreferenceChangeListener(this);
        color3.setOnPreferenceChangeListener(this);
        color4.setOnPreferenceChangeListener(this);
        color5.setOnPreferenceChangeListener(this);
        color6.setOnPreferenceChangeListener(this);
        color7.setOnPreferenceChangeListener(this);
        color8.setOnPreferenceChangeListener(this);

    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        SharedPreferences pref = getSharedPreferences(Preferences.PREFERENCES, Context.MODE_PRIVATE);

        int newcolor = (Integer)newValue;

        if( preference.equals(findPreference("colora"))){
            pref.edit().putInt(Preferences.COLORA, newcolor).commit();
            Att.setColor(0, newcolor);
        }

        if( preference.equals(findPreference("color1"))){
            pref.edit().putInt(Preferences.COLOR1, newcolor).commit();
            Att.setColor(1, newcolor);
        }

        if( preference.equals(findPreference("color2"))){
            pref.edit().putInt(Preferences.COLOR2, newcolor).commit();
            Att.setColor(2, newcolor);
        }

        if( preference.equals(findPreference("color3"))){
            pref.edit().putInt(Preferences.COLOR3, newcolor).commit();
            Att.setColor(3, newcolor);
        }

        if( preference.equals(findPreference("color4"))){
            pref.edit().putInt(Preferences.COLOR4, newcolor).commit();
            Att.setColor(4, newcolor);
        }

        if( preference.equals(findPreference("color5"))){
            pref.edit().putInt(Preferences.COLOR5, newcolor).commit();
            Att.setColor(5, newcolor);
        }

        if( preference.equals(findPreference("color6"))){
            pref.edit().putInt(Preferences.COLOR6, newcolor).commit();
            Att.setColor(6, newcolor);
        }

        if( preference.equals(findPreference("color7"))){
            pref.edit().putInt(Preferences.COLOR7, newcolor).commit();
            Att.setColor(7, newcolor);
        }

        if( preference.equals(findPreference("color8"))){
            pref.edit().putInt(Preferences.COLOR8, newcolor).commit();
            Att.setColor(8, newcolor);
        }

        return true;
    }
}
