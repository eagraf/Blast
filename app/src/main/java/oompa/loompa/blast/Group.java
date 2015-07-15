package oompa.loompa.blast;

import java.util.List;

import oompa.loompa.blast.firebase.Message;

/**
 * Created by Da-Jin on 7/15/2015.
 */
public interface Group {
    public boolean isGroupOwner(String UID);
    public FirebaseMetadata getGroupMetadata();
    public List<Message> getGroupMessages();
    public void post(Message message);

    public interface Metadata {
        public String getOwnerUID();
        public boolean getIsPublic();
    }
}
