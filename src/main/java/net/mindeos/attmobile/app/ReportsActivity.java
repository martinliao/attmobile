package net.mindeos.attmobile.app;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import net.mindeos.attmobile.dialogs.AttControlReportPicker;
import net.mindeos.attmobile.pojos.User;

import java.util.Date;


/**
 * Activity for the reports screen.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class ReportsActivity extends AttControlBase implements AttControlReportPicker.OnFragmentInteractionListener {

    /**
     * The fragment included in this activity.
     */
    ReportsFragment rf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        rf = new ReportsFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, rf)
                    .commit();
        }

        ActionBar actionBar = getActionBar();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.reports_settings:
                rf.showReportsDialog(this);
                break;
            case R.id.reports_save:
                rf.saveData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPositiveButtonClicked(int course, int attcontrol, int grouppos, User user, Date startDate, Date endDate) {
        //communicate information to fragment
        rf.positiveClicked(course, attcontrol, grouppos, user, startDate, endDate);
    }
}
