package oompa.loompa.blast;

import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import oompa.loompa.blast.firebase.FirebaseGroup;
import oompa.loompa.blast.firebase.FirebaseHelper;
import oompa.loompa.blast.firebase.Message;

/**
 * Created by Ethan on 7/17/2015.
 */
public class GroupManager implements GroupListener, FirebaseHelper.SubscriptionListener {

    public ArrayList<Group> groups;
    public GroupListAdapter adapter;

    public void onConnected() {
        groups = new ArrayList<>();

        FirebaseHelper.registerSubscriptionListener(this);
        this.adapter = new GroupListAdapter(this);
    }

    public void addGroup() {

    }

    //Remove a group.
    public void removeGroup(Group group) {

    }

    @Override
    public void messageChange(Group group, List<Message> msgs) {

    }

    @Override
    public void metaDataChange(Group group, Group.Metadata meta) {

    }

    @Override
    //Listener method for when a subscription is added. Creates a new group to be added to the model.
    public void subAdded(String groupName) {
        Group group = FirebaseGroup.accessGroup(groupName);
        group.registerGroupListener(this);
        groups.add(group);
        adapter.addGroup(groups.size() - 1);
    }

    @Override
    public void subRemoved(String groupName) {
        System.out.println("Remove " + groupName);
        for(int i = 0; i < groups.size(); i++) {
            System.out.println(groups.get(i).getName() + ", " + groupName);
            if(groups.get(i).getName().equals(groupName)) {
                System.out.println("YOOY");
                groups.remove(i);
                adapter.removeGroup(i);
            }
        }
    }
}
