package oompa.loompa.blast;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener {
    private RecyclerView mNotificationListView;
    private RecyclerView.Adapter mNotificationListAdapter;
    private RecyclerView.LayoutManager mNotificationListLayoutManager;

    private static final String planets[] = new String[] {"Sun", "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNotificationListView = (RecyclerView) findViewById(R.id.notification_list_view);

        //Create the drawer listener(listens for events involving the navigation drawer being opened and closed).
        ((DrawerLayout) this.findViewById(R.id.navigation_layout)).setDrawerListener(this);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mNotificationListView.setHasFixedSize(true);

        // use a linear layout manager
        mNotificationListLayoutManager = new LinearLayoutManager(this);
        mNotificationListView.setLayoutManager(mNotificationListLayoutManager);

        // specify an adapter (see also next example)
        mNotificationListAdapter = new NotificationListAdapter(planets);
        mNotificationListView.setAdapter(mNotificationListAdapter);
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
}