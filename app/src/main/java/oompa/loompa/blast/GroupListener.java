package oompa.loompa.blast;

import java.util.List;

import oompa.loompa.blast.firebase.Message;

/**
 * Created by Da-Jin on 7/17/2015.
 */
public interface GroupListener {
    public void messageChange(Group group, List<Message> msgs);
    public void metaDataChange(Group group, Group.Metadata meta);
}
