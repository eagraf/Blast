package oompa.loompa.blast;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

import oompa.loompa.blast.firebase.FirebaseHelper;
import oompa.loompa.blast.firebase.Message;

/**
 * Created by Ethan on 7/17/2015.
 */
public class GroupManager implements GroupListener, FirebaseHelper.SubscriptionListener {

    public ArrayList<Group> groups;
    public GroupListAdapter groupAdapter;

    public MessageListAdapter messageAdapter;

    public Context context;
    public MultiGroupMessageListAdapter inboxAdapter;

    public GroupManager(Context context) {
        this.context = context;
    }
    public void onAuthorization() {
        groups = new ArrayList<>();

        this.inboxAdapter = new MultiGroupMessageListAdapter();
        FirebaseHelper.registerSubscriptionListener(this);
        this.groupAdapter = new GroupListAdapter(this);
        this.messageAdapter = new MessageListAdapter();

        Log.i("Manager","auth");
    }

    public void addGroup() {

    }

    //Remove a group.
    public void removeGroup(Group group) {

    }

    @Override
    public void messageChange(Group group, Map<String,Message> msgs) {
        Log.i("Manager","Message Change");
        messageAdapter.updateGroup(group);
        inboxAdapter.updateGroup(group);
    }

    @Override
    public void metaDataChange(Group group, Group.Metadata meta) {
    }

    @Override
    //Listener method for when a subscription is added. Creates a new group to be added to the model.
    public void subAdded(Group group) {
        group.registerGroupListener(this);
        groups.add(group);
        groupAdapter.addGroup(groups.size() - 1);
    }

    @Override
    public void subRemoved(String groupName) {
        for(int i = 0; i < groups.size(); i++) {
            System.out.println(groups.get(i).getUID() + ", " + groupName);
            if(groups.get(i).getUID().equals(groupName)) {
                groups.remove(i);
                groupAdapter.removeGroup(i);
            }
        }
    }
}
