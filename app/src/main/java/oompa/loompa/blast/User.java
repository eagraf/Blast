package oompa.loompa.blast;

import java.util.List;

/**
 * Created by Da-Jin on 7/18/2015.
 */
public interface User {
    public String getProfileImageURL();
    public String getUID();
    public String getDisplayName();
    public String getEmail();

    List<String> getSubscriptions();
}
