package oompa.loompa.blast;

import java.util.List;

import oompa.loompa.blast.firebase.Message;

/**
 * Created by Da-Jin on 7/15/2015.
 */
public interface Group {
    void subscribe();

    void unsubscribe();

    public void registerGroupListener(GroupListener listener);
    public void post(Message message);
    public String getUID();
    public List<Message> getMessages();

    public interface Metadata {
        public String getDisplayName();
        public String getOwnerUID();
        public boolean getIsPublic();
    }
}
