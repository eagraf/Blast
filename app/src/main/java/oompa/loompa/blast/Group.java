package oompa.loompa.blast;

import oompa.loompa.blast.firebase.Message;

/**
 * Created by Da-Jin on 7/15/2015.
 */
public interface Group {
    public void registerGroupListener(GroupListener listener);
    public void post(Message message);

    public interface Metadata {
        public String getOwnerUID();
        public boolean getIsPublic();
    }
}
