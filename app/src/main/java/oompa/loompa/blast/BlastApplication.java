package oompa.loompa.blast;

import android.content.Intent;

import oompa.loompa.blast.firebase.FirebaseHelper;

/**
 * Created by Da-Jin on 7/15/2015.
 */
public class BlastApplication extends android.app.Application {

    private static GroupManager groupManager;

    @Override
    public void onCreate() {
        FirebaseHelper.onCreate(this);
        super.onCreate();

        if(!BlastService.getRunning()) {
            Intent serviceIntent = new Intent(this, BlastService.class);
            this.startService(serviceIntent);
        }
    }

    public void onAuthenticated() {
        groupManager = new GroupManager(this);
        groupManager.onAuthorization();
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }
}

