package oompa.loompa.blast;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;

import java.util.HashMap;
import java.util.Map;

import oompa.loompa.blast.firebase.FirebaseGroup;
import oompa.loompa.blast.firebase.FirebaseHelper;
import oompa.loompa.blast.firebase.Message;

/**
 * Created by Ethan on 7/23/2015.
 */
public class BlastService extends Service implements GroupListener,
    FirebaseHelper.SubscriptionListener {

    private HashMap<String, Group> groups;

    private static boolean running = false;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        groups = new HashMap<>();
        GoogleOAuth googleOAuth = new GoogleOAuth(this);
        running = true;
    }

    @Override
    public void onDestroy() {
        running = false;
    }

    public void onAuthorized() {
        FirebaseHelper.registerSubscriptionListener(this);
    }

    public void messageAdded(Group group, Message message) {
        Toast.makeText(this, "Message Added", Toast.LENGTH_LONG).show();
    }

    public void messageChange(Group group, Map<String, Message> msgs) {

    }

    public void metaDataChange(Group group, Group.Metadata meta) {

    }

    public void subAdded(Group group) {
        group.registerGroupListener(this);
        groups.put(group.getUID(), group);
    }

    public void subRemoved(String name) {
        groups.remove(name);
    }

    public static boolean getRunning() {
        return running;
    }

    public static void setRunning(boolean b) {
        running = b;
    }
}
