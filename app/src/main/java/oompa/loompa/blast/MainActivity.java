package oompa.loompa.blast;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import oompa.loompa.blast.firebase.FirebaseGroup;
import oompa.loompa.blast.firebase.FirebaseHelper;
import oompa.loompa.blast.firebase.FirebaseMetadata;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        NewGroupDialogFragment.NewGroupDialogListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mDrawerView;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private NotificationListFragment notificationListFragment;
    private GroupListFragment groupListFragment;

    public final static String MESSAGE_VIEW_TITLE = "oompa.loompa.blast.MESSAGE_VIEW_TITLE";
    public final static String MESSAGE_VIEW_GROUP_NAME = "oompa.loompa.blast.MESSAGE_VIEW_GROUP_NAME";

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

        //Set the navigation drawer information.
        ((TextView) findViewById(R.id.navigation_header).findViewById(R.id.name)).setText(FirebaseHelper.getCurrentUserInfo().getDisplayName());
        ((TextView) findViewById(R.id.navigation_header).findViewById(R.id.email)).setText(FirebaseHelper.getCurrentUserInfo().getEmail());

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
        menuItem.setChecked(true);
        fragmentTransaction =getFragmentManager().beginTransaction();
        switch (menuItem.getItemId()) {
            //Go to the notification list.
            case R.id.notification_list_item:
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_content_frame, notificationListFragment);
                fragmentTransaction.commit();
                break;
            //Go to the group list.
            case R.id.group_list_item:
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_content_frame, groupListFragment);
                fragmentTransaction.commit();
                break;
            default:
                return true;
        }
        mDrawerLayout.closeDrawers();
        return true;
    }


    //Remove a notification from the notification list view.
    public void removeNotification(View v) {
        notificationListFragment.removeNotification(v);
    }

    //Create a new group
    public void newGroup(View v) {
        // Create an instance of the new group dialog fragment and show it
        NewGroupDialogFragment dialog = new NewGroupDialogFragment();
        dialog.setTitle(getResources().getString(R.string.dialog_new_group));
        dialog.show(getSupportFragmentManager(), "NewGroupDialogFragment");
    }

    //Open a new activity to view messages within a group.
    public void openMessageView(View v) {
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra(MESSAGE_VIEW_TITLE, ((TextView) v.findViewById(R.id.firstLine)).getText().toString());
        intent.putExtra(MESSAGE_VIEW_GROUP_NAME, ((TextView) v.findViewById(R.id.uid)).getText().toString());
        this.startActivity(intent);
    }

    @Override
    public void onDialogPositiveClick(NewGroupDialogFragment dialog, String groupName) {
        FirebaseMetadata metadata = new FirebaseMetadata(groupName, FirebaseHelper.getCurrentUserInfo().getUID(), true);
        FirebaseGroup.createGroup(metadata);
    }

    @Override
    //Negative click on New Group Dialog
    public void onDialogNegativeClick(NewGroupDialogFragment dialog) {
        // User touched the dialog's negative button
        // Nothing happens
    }
}