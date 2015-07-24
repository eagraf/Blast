package oompa.loompa.blast;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import oompa.loompa.blast.firebase.FirebaseHelper;
import oompa.loompa.blast.firebase.Message;

/**
 * Created by Ethan on 7/17/2015.
 */
public class GroupManager implements GroupListener, FirebaseHelper.SubscriptionListener, FirebaseHelper.AllGroupsListener {

    public ArrayList<Group> groups;
    public List<Group.Metadata> allGroupsMeta = new ArrayList<>();
    public GroupListAdapter groupAdapter;
    public FindGroupsListAdapter findGroupsAdapater;

    public Context context;
    public MultiGroupMessageListAdapter inboxAdapter;

    public GroupManager(Context context) {
        this.context = context;
    }
    public void onAuthorization() {
        groups = new ArrayList<>();

        FirebaseHelper.registerSubscriptionListener(this);
        FirebaseHelper.registerAllGroupsMetaListener(this);
        this.groupAdapter = new GroupListAdapter(this);
        this.findGroupsAdapater = new FindGroupsListAdapter(this);
        this.inboxAdapter = new MultiGroupMessageListAdapter();


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
    public void subRemoved(String groupUID) {
        for(int i = 0; i < groups.size(); i++) {
            System.out.println(groups.get(i).getUID() + ", " + groupUID);
            if(groups.get(i).getUID().equals(groupUID)) {
                groups.remove(i);
                groupAdapter.removeGroup(i,groups.size()-1);
            }
        }
    }

    @Override
    public void metaDataAdded(Group.Metadata meta) {
        allGroupsMeta.add(meta);
        findGroupsAdapater.addGroup(allGroupsMeta.size()-1);
    }

    @Override
    public void metaDataRemoved(Group.Metadata removed) {
        for(int i = 0; i < allGroupsMeta.size(); i++) {
            if(allGroupsMeta.get(i).getGroupUID().equals(removed.getGroupUID())) {
                allGroupsMeta.remove(i);
                findGroupsAdapater.removeGroup(i, allGroupsMeta.size() - 1);
            }
        }
    }
}
