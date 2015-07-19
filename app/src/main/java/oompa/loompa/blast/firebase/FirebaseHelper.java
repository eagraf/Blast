package oompa.loompa.blast.firebase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import oompa.loompa.blast.BlastApplication;
import oompa.loompa.blast.MainActivity;
import oompa.loompa.blast.User;

/**
 * Created by Da-Jin on 7/14/2015.
 */
public class FirebaseHelper {
    private static String FIREBASE_URL = "https://blastmvp.firebaseio.com/";
    private static Firebase mFirebaseRef;
    private static ValueEventListener mConnectedListener;
    private static Boolean connected = false;
    private static FirebaseGoogleUser user;
    private static String userDir;

    private static class AuthResultHandler implements Firebase.AuthResultHandler {
        Context context;

        public AuthResultHandler(Context context){
            this.context = context;
        }
        @Override
        public void onAuthenticated(AuthData authData) {
            user = new FirebaseGoogleUser(authData);
            userDir = "users/"+user.getUID();
            mFirebaseRef.child(userDir).setValue(user);
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
    protected static Firebase getFirebaseRef(){
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
    public static String getUserDir(){
        return userDir;
    }
    public static void registerSubscriptionListener(final SubscriptionListener listener){
        mFirebaseRef.child(getUserDir()+"/subscriptions/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> groupNames = new ArrayList<>();
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                for (DataSnapshot datum : data) {
                    groupNames.add(datum.getKey());
                }
                listener.subscriptionChanged(groupNames);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public interface SubscriptionListener {
        public void subscriptionChanged(List<String> subs);
    }

    public static User getCurrentUserInfo(){
        return user;
    }
    public void getOtherUserInfo(String UID, final UserInfoCallback callback){
        FirebaseHelper.getFirebaseRef().child("users/"+UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.infoArrived(dataSnapshot.getValue(FirebaseGoogleUser.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public interface UserInfoCallback{
        public void infoArrived(User user);
    }

}
