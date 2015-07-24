package oompa.loompa.blast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Map;

import oompa.loompa.blast.firebase.FirebaseGroup;
import oompa.loompa.blast.firebase.FirebaseHelper;
import oompa.loompa.blast.firebase.Message;

/**
 * Created by Ethan on 7/18/2015.
 */
public class MessageActivity extends AppCompatActivity implements GroupListener {
    private RecyclerView mMessageListView;
    private RecyclerView.LayoutManager mMessageListLayoutManager;
    private MessageListAdapter mMessageListAdapter;

    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //Set up the toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Get the title from the intent and set it as the title for the activity.
        Intent intent = getIntent();
        String title = intent.getStringExtra(MainActivity.MESSAGE_VIEW_TITLE);
        getSupportActionBar().setTitle(title);

        //Sets icon to go back to previous activity.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(MessageActivity.this);
            }
        });

        String uid = intent.getStringExtra(MainActivity.MESSAGE_VIEW_GROUP_UID);
        group = FirebaseGroup.accessGroup(uid);
        group.registerGroupListener(this);

        mMessageListView = (RecyclerView) findViewById(R.id.message_list_view);

        mMessageListLayoutManager = new LinearLayoutManager(this);
        mMessageListView.setLayoutManager(mMessageListLayoutManager);

        // specify an adapter (see also next example)
        mMessageListAdapter =new MessageListAdapter();
        mMessageListAdapter.setGroup(group);
        mMessageListView.setAdapter(mMessageListAdapter);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mMessageListView.setHasFixedSize(true);


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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subscribe, menu);
        final MenuItem switchItem = menu.findItem(R.id.subscribe_switch_item);
        final MenuItem textItem = menu.findItem(R.id.subscribe_text_item);

        if(FirebaseHelper.getCurrentUserInfo().getSubscriptions().contains(group.getUID())) {
            ((Switch) switchItem.getActionView().findViewById(R.id.subscription_switch)).setChecked(true);
            textItem.setTitle("Subscribed");
        }

        Switch subscribeSwitch = (Switch) switchItem.getActionView().findViewById(R.id.subscription_switch);
        subscribeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is
                    textItem.setTitle("Subscribed");
                    Toast.makeText(MessageActivity.this, "Subscribed to group " + "\""  + group.getMetadata().getDisplayName() + "\"", Toast.LENGTH_LONG).show();
                    group.subscribe();
                } else {
                    // The toggle is disabled
                    textItem.setTitle("Subscribe");
                    Toast.makeText(MessageActivity.this, "Unsubscribed from group " + "\""  + group.getMetadata().getDisplayName() + "\"", Toast.LENGTH_LONG).show();
                    group.unsubscribe();
                }
            }
        });
        return true;
    }

    public void postMessage(View view) {
        EditText subject = (EditText) findViewById(R.id.subject);
        EditText body = (EditText) findViewById(R.id.body);
        Message message = new Message(subject.getText().toString(), body.getText().toString());
        group.post(message);
        subject.setText("");
        body.setText("");
        //Close the keyboard.
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void messageChange(Group group, Map<String, Message> msgs) {
        mMessageListAdapter.updateGroup(group);
    }

    @Override
    public void metaDataChange(Group group, Group.Metadata meta) {
        if(group.getMetadata().getOwnerUID().equals(FirebaseHelper.getCurrentUserInfo().getUID())) {
            findViewById(R.id.post_view).setVisibility(View.VISIBLE);
            System.out.println("hi");
        }
    }
}
