package oompa.loompa.blast;

import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import oompa.loompa.blast.firebase.FirebaseGroup;
import oompa.loompa.blast.firebase.Message;

/**
 * Created by Ethan on 7/17/2015.
 */
public class GroupManager implements GroupListener {

    public ArrayList<Group> groups;
    GroupListAdapter adapter;

    public void onConnected() {
        groups = new ArrayList<>();
        groups.add(FirebaseGroup.accessGroup("alpha"));
        groups.add(FirebaseGroup.accessGroup("beta"));

        for(int i = 0; i < groups.size(); i++) {
            groups.get(i).registerGroupListener(this);
        }
    }

    public void setAdapter(GroupListAdapter adapter) {
        this.adapter = adapter;
    }

    public void addGroup() {

    }

    //Remove a group.
    public void removeGroup(Group group) {
        int position = groups.indexOf(group);
        adapter.removeGroup(position);
        groups.remove(group);
    }

    @Override
    public void messageChange(Group group, List<Message> msgs) {

    }

    @Override
    public void metaDataChange(Group group, Group.Metadata meta) {

    }
}
