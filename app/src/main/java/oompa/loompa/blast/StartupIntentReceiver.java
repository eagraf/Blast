package oompa.loompa.blast;

/**
 * Created by Ethan on 7/23/2015.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartupIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, BlastService.class);
        context.startService(serviceIntent);
    }
}
