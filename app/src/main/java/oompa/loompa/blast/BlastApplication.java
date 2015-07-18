package oompa.loompa.blast;

import oompa.loompa.blast.firebase.FirebaseHelper;

/**
 * Created by Da-Jin on 7/15/2015.
 */
public class BlastApplication extends android.app.Application {

    public GroupManager groupManager;

    @Override
    public void onCreate() {
        FirebaseHelper.onCreate(this);
        super.onCreate();
        groupManager = new GroupManager();

    }

    public void onConnected() {
        groupManager.onConnected();
    }
}

