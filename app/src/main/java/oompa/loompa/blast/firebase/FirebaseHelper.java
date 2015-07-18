package oompa.loompa.blast.firebase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import oompa.loompa.blast.BlastApplication;
import oompa.loompa.blast.MainActivity;

/**
 * Created by Da-Jin on 7/14/2015.
 */
public class FirebaseHelper {
    private static String FIREBASE_URL = "https://blastmvp.firebaseio.com/";
    private static Firebase mFirebaseRef;
    private static ValueEventListener mConnectedListener;
    private static Boolean connected = false;

    private static class AuthResultHandler implements Firebase.AuthResultHandler {
        Context context;
        public AuthResultHandler(Context context){
            this.context = context;
        }
        @Override
        public void onAuthenticated(AuthData authData) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("provider", authData.getProvider());
            if (authData.getProviderData().containsKey("id")) {
                map.put("provider_id", authData.getProviderData().get("id").toString());
            }
            if (authData.getProviderData().containsKey("displayName")) {
                map.put("displayName", authData.getProviderData().get("displayName").toString());
            }
            mFirebaseRef.child("users").child(authData.getUid()).setValue(map);
            context.startActivity(new Intent(context, MainActivity.class));
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            new AlertDialog.Builder(context)
                    .setTitle("Error")
                    .setMessage(firebaseError.toString())
                    .setPositiveButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    public static void onCreate(final Context context){
        Firebase.setAndroidContext(context);
        mFirebaseRef = new Firebase(FIREBASE_URL);
        //TODO move this to onstart?
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                connected = (Boolean) dataSnapshot.getValue();
                if(connected) {
                    ((BlastApplication) context).onConnected();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });
    }
    public static void onStop(){
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
    }
    public static boolean isConnected(){
        return connected;
    }
    //TODO NOT sure if synchronize is needed.
    public static Firebase getFirebaseRef(){
        if(mFirebaseRef==null){
            throw new RuntimeException("Tried to get FirebaseRef before it was created. You dummy probs didn't call FirebaseHelper.onCreate");
        }
        return mFirebaseRef;
    }
    public static void createAccountEmail(String email, String password){
        mFirebaseRef.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
            }
            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
            }
        });
    }
    public static void authEmail(String email, String password,Context context){
        mFirebaseRef.authWithPassword(email,password,new AuthResultHandler(context));
    }

    public static void authWithToken(String provider, String token, Context context){
        mFirebaseRef.authWithOAuthToken(provider,token,new AuthResultHandler(context));
    }
}
