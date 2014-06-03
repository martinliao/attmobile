package net.mindeos.attmobile.app;

import android.app.ActionBar;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;


/**
 * The activity for the TakeAttendance activity
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class TakeAttendanceActivity extends AttControlBase
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, TakeAttendanceFragment.OnFragmentInteractionListener {

    /**
     * The Taf.
     */
    TakeAttendanceFragment taf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takeattendance);

        taf = new TakeAttendanceFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, (Fragment) taf)
                    .commit();
        }

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_takeattendance, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_take_attendance_save:
                taf.saveData();
                return true;
            case R.id.action_take_attendance_setallto:
                taf.showSetAllDialog();
                return true;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
