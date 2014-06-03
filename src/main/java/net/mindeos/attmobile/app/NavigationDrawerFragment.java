package net.mindeos.attmobile.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import net.mindeos.attmobile.adapters.NavDrawerItem;
import net.mindeos.attmobile.adapters.NavDrawerListAdapter;
import net.mindeos.attmobile.webservice.Att;
import net.mindeos.attmobile.webservice.AttControlCaller;

import java.util.ArrayList;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    /**
     * The List.
     */
    ArrayList<NavDrawerItem> list;


    private final int ACTION_ATTCONTROL = 0;
    private final int ACTION_ATTHOME = 1;
    private final int ACTION_SETTINGS = 2;
    private final int ACTION_HELP = 3;
    private final int ACTION_ABOUT = 4;
    private final int ACTION_LOGOUT = 5;

    /**
     * Instantiates a new Navigation drawer fragment.
     */
    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mDrawerListView = (ListView) inflater.inflate(
                R.layout.fragment_navigationdrawer, container, false);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        list = new ArrayList<NavDrawerItem>();
        list.add(new NavDrawerItem(getString(R.string.attmobile), true));
        if (Att.hasAttControl()) {
            list.add(new NavDrawerItem(getString(R.string.attcontrol), R.drawable.ic_attcontrollogo, true, ACTION_ATTCONTROL));
        }
        if (Att.hasAttHome()) {
            list.add(new NavDrawerItem(getString(R.string.atthome), R.drawable.ic_atthomelogo , true, ACTION_ATTHOME));
        }

        list.add(new NavDrawerItem(getString(R.string.settings), true));
        list.add(new NavDrawerItem(getString(R.string.settings), R.drawable.ic_action_settings, true, ACTION_SETTINGS));
        list.add(new NavDrawerItem(getString(R.string.help), R.drawable.ic_action_help, true, ACTION_HELP));
        list.add(new NavDrawerItem(getString(R.string.about), R.drawable.ic_action_about, true, ACTION_ABOUT));
        list.add(new NavDrawerItem(getString(R.string.logout), R.drawable.ic_action_logout, true, ACTION_LOGOUT));

        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

        View v = inflater.inflate(R.layout.itemgroupadapter_navigatorheader, null);

        TextView tvNavigatorHeader1 = (TextView) v.findViewById(R.id.tvNavigatorHeader1);
        tvNavigatorHeader1.setText(Att.getFirstname()+" "+Att.getLastname());

        TextView tvNavigatorHeader2 = (TextView) v.findViewById(R.id.tvNavigatorHeader2);
        tvNavigatorHeader2.setText(Att.getUsername());

        ImageView imNavigatorHeader = (ImageView) v.findViewById(R.id.imNavigatorHeader);

        if (Att.getUser().getPic1() != Preferences.NOPICTURE) {
            UrlImageViewHelper.setUrlDrawable(imNavigatorHeader, AttControlCaller.getUserImageURL(Att.getUser().getPic1(), Att.getUser().getPic2()), R.drawable.userplaceholder);
        }
        else {
            UrlImageViewHelper.setUrlDrawable(imNavigatorHeader, "", R.drawable.userplaceholder);
        }

        mDrawerListView.addHeaderView(v);

        mDrawerListView.setAdapter(new NavDrawerListAdapter(this.getActivity(), list));

        return mDrawerListView;
    }

    /**
     * Is drawer open.
     *
     * @return the boolean
     */
    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).commit();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;

        if (position == 0 || list.get(position - 1).isTitle()) {
            return;
        }

        NavDrawerItem ndi = list.get(position-1);

        boolean needclose = false;

        switch (ndi.getAction()){
            case ACTION_ATTCONTROL:
                if (getActivity().getClass() != AttControlActivity.class) {
                    needclose = false;
                    if (getActivity().getClass() == AttHomeActivity.class) getActivity().finish();
                    Intent attcontrol = new Intent(getActivity(), AttControlActivity.class);
                    startActivity(attcontrol);
                }
                else {
                    needclose = true;
                }
                break;
            case ACTION_ATTHOME:
                if (getActivity().getClass() != AttHomeActivity.class) {
                    needclose = false;
                    if (getActivity().getClass() == AttControlActivity.class) getActivity().finish();
                    Intent atthome = new Intent(getActivity(), AttHomeActivity.class);
                    startActivity(atthome);
                }
                else {
                    needclose = true;
                }
                break;
            case ACTION_ABOUT:
                Intent about = new Intent(getActivity(), AboutActivity.class);
                startActivity(about);
                needclose = true;
                break;
            case ACTION_SETTINGS:
                Intent settings = new Intent(getActivity(), SettingsActivity.class);
                startActivity(settings);
                needclose = true;
                break;
            case ACTION_HELP:
                Uri uri = Uri.parse("http://att.mindeos.net");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                needclose = true;
                break;
            case ACTION_LOGOUT:
                Att.clearUserData();

                SharedPreferences pref = getActivity().getSharedPreferences(Preferences.PREFERENCES, Context.MODE_PRIVATE);

                pref.edit().remove("moodlesite").commit();
                pref.edit().remove("moodleuser").commit();
                pref.edit().remove("moodlepass").commit();

                needclose = false;
                getActivity().finish();

                Intent attmobile = new Intent(getActivity(), AttMobile.class);
                startActivity(attmobile);

                break;
        }

        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null && needclose) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }

    private ActionBar getActionBar() {
        return getActivity().getActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         * @param position the position
         */
        void onNavigationDrawerItemSelected(int position);
    }
}
