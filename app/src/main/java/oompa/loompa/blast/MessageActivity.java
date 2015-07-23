package oompa.loompa.blast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import oompa.loompa.blast.firebase.FirebaseGroup;
import oompa.loompa.blast.firebase.FirebaseHelper;
import oompa.loompa.blast.firebase.Message;

/**
 * Created by Ethan on 7/18/2015.
 */
public class MessageActivity extends AppCompatActivity {
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

        String name = intent.getStringExtra(MainActivity.MESSAGE_VIEW_GROUP_NAME);
        GroupManager groupManager = FirebaseHelper.getGroupManager();
        for(int i = 0; i < groupManager.groups.size(); i++) {
            if(name.equals(((FirebaseGroup) groupManager.groups.get(i)).getUID())) {
                group = groupManager.groups.get(i);
            }
        }

        mMessageListView = (RecyclerView) findViewById(R.id.message_list_view);

        mMessageListLayoutManager = new LinearLayoutManager(this);
        mMessageListView.setLayoutManager(mMessageListLayoutManager);

        // specify an adapter (see also next example)
        mMessageListAdapter = groupManager.messageAdapter;
        mMessageListAdapter.resetGroup(group);
        mMessageListView.setAdapter(mMessageListAdapter);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mMessageListView.setHasFixedSize(true);

        if(FirebaseHelper.getCurrentUserInfo().getUID().equals(((FirebaseGroup) group).getMetadata().getOwnerUID())) {
            findViewById(R.id.post_view).setVisibility(View.VISIBLE);
        }
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
}
