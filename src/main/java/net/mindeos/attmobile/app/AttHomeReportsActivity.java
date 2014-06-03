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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import net.mindeos.attmobile.dialogs.AttHomeReportPicker;
import net.mindeos.attmobile.webservice.Att;

import java.util.Date;


/**
 * The activity for the atthome reports screen
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class AttHomeReportsActivity extends AttHomeBase implements AttHomeReportPicker.OnFragmentInteractionListener {

    /**
     * The fragment included in this activity.
     */
    AttHomeReportsFragment ahrf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atthomereports);

        ahrf = new AttHomeReportsFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, ahrf)
                    .commit();
        }

        ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(Att.getFirstname()+" "+Att.getLastname());
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reports, menu);
        super.onCreateOptionsMenu(menu);

        changeMenuItem(R.id.reports_settings, false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.reports_settings:
                ahrf.showReportsDialog(this);
                break;
            case R.id.reports_save:
                ahrf.saveData();
                return true;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPositiveButtonClicked(int selcourse, int selgroup, int selstudent, Date startdate, Date enddate) {
        ahrf.positiveClicked(selcourse, selgroup, selstudent, startdate, enddate);
    }
}
