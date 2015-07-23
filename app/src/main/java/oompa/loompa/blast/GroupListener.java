package oompa.loompa.blast;

import java.util.Map;

import oompa.loompa.blast.firebase.Message;

/**
 * Created by Da-Jin on 7/17/2015.
 */
public interface GroupListener {
    public void messageChange(Group group, Map<String, Message> msgs);
    public void metaDataChange(Group group, Group.Metadata meta);
}
