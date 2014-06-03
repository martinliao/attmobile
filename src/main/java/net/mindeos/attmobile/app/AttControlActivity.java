package net.mindeos.attmobile.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import net.mindeos.attmobile.webservice.Att;

/**
 * Activity for the AttControl screen.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class AttControlActivity extends AttControlBase
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, AttControlFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attcontrol);

        getActionBar().setSubtitle(Att.getFirstname() + " " + Att.getLastname());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AttControlFragment())
                    .commit();
        }

        NavigationDrawerFragment mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                dl);

        dl.closeDrawers();
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_attcontrol, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.sessions_newsession:
                Intent intent = new Intent(this, NewSessionActivity.class);

                TextView tv = (TextView) findViewById(R.id.tvSessionsDate);
                intent.putExtra("currentdate", tv.getText());

                startActivity(intent);
                return true;
            case R.id.sessions_reports:
                Intent intent2 = new Intent(this, ReportsActivity.class);

                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            moveTaskToBack(true);
            return true; // return
        }

        return false;
    }

}
