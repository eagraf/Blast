package oompa.loompa.blast;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mDrawerView;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private NotificationListFragment notificationListFragment;
    private GroupListFragment groupListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up the toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        //Enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_launcher);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //Initialize some navigation drawer stuff.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.navigation_layout);
        mDrawerView = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerView.setNavigationItemSelectedListener(this);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        notificationListFragment = new NotificationListFragment();
        groupListFragment = new GroupListFragment();
        fragmentTransaction.add(R.id.main_content_frame, notificationListFragment);
        fragmentTransaction.commit();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    //Handle click events on navigation drawer items.
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        System.out.println("HIIIIIIIIIIIIII");
        menuItem.setChecked(true);
        fragmentTransaction =getFragmentManager().beginTransaction();
        switch (menuItem.getItemId()) {
            //Go to the notification list.
            case R.id.notification_list_item:
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_content_frame, notificationListFragment);
                fragmentTransaction.commit();
                return true;
            //Go to the group list.
            case R.id.group_list_item:
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_content_frame, groupListFragment);
                fragmentTransaction.commit();
                return true;
            default:
                return true;
        }
    }


    //Remove a notification from the notification list view.
    public void removeNotification(View v) {
        int position = notificationListFragment.mNotificationListView.getChildAdapterPosition((RelativeLayout) v.getParent().getParent().getParent());
        notificationListFragment.mNotificationListAdapter.mDataSet.remove(position);
        notificationListFragment.mNotificationListAdapter.notifyItemRemoved(position);
        notificationListFragment.mNotificationListAdapter.notifyItemRangeChanged(position, notificationListFragment.mNotificationListAdapter.mDataSet.size());
    }
}