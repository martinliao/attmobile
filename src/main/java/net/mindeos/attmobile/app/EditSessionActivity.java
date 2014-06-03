package net.mindeos.attmobile.app;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


/**
 * Activity for the Edit Session screen.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class EditSessionActivity extends AttControlBase {

    /**
     * The fragment included in this activity
     */
    EditSessionFragment esf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editsession);

        esf = new EditSessionFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, esf)
                    .commit();
        }

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editsession, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // Handle presses on the action bar items
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_edit_session_save:
                esf.saveData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
