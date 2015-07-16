package oompa.loompa.blast;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener {
    private RecyclerView mNotificationListView;
    private NotificationListAdapter mNotificationListAdapter;
    private RecyclerView.LayoutManager mNotificationListLayoutManager;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private static final String planets[] = new String[] {"Sun", "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNotificationListView = (RecyclerView) findViewById(R.id.notification_list_view);

        //Enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.navigation_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        //Create the drawer listener(listens for events involving the navigation drawer being opened and closed).
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mNotificationListView.setHasFixedSize(true);

        // use a linear layout manager
        mNotificationListLayoutManager = new LinearLayoutManager(this);
        mNotificationListView.setLayoutManager(mNotificationListLayoutManager);

        // specify an adapter (see also next example)
        mNotificationListAdapter = new NotificationListAdapter(new ArrayList<String>(Arrays.asList(planets)));
        mNotificationListView.setAdapter(mNotificationListAdapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        //For the hamburger icon.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerSlide(View drawerView, float offset) {
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        System.out.println("HI");
    }

    @Override
    public void onDrawerClosed(View DrawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    //Remove a notification from the notification list view.
    public void removeNotification(View v) {
        int position = mNotificationListView.getChildAdapterPosition((RelativeLayout) v.getParent().getParent().getParent());
        mNotificationListAdapter.mDataSet.remove(position);
        mNotificationListAdapter.notifyItemRemoved(position);
        mNotificationListAdapter.notifyItemRangeChanged(position, mNotificationListAdapter.mDataSet.size());
    }
}