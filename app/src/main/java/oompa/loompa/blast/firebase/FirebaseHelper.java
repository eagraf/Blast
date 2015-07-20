package oompa.loompa.blast.firebase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import oompa.loompa.blast.Group;
import oompa.loompa.blast.GroupListener;
import oompa.loompa.blast.GroupManager;
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
    private static GroupManager groupManager;

    private static class AuthResultHandler implements Firebase.AuthResultHandler {
        Context context;

        public AuthResultHandler(Context context){
            this.context = context;
        }
        @Override
        public void onAuthenticated(AuthData authData) {
            user = new FirebaseGoogleUser(authData);
            userDir = "users/"+user.getUID();

            Map<String, Object> children = new HashMap<>();
            children.put("displayName",user.getDisplayName());
            children.put("email",user.getEmail());
            children.put("profileImageURL",user.getProfileImageURL());
            children.put("UID",user.getUID());
            mFirebaseRef.child(userDir).updateChildren(children);


            groupManager.onAuthorization();
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
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });

        groupManager = new GroupManager(context);
    }
    public static void onStop(){
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
    }
    public static boolean isConnected(){
        return connected;
    }

    public static GroupManager getGroupManager(){
        return groupManager;
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
        mFirebaseRef.authWithOAuthToken(provider, token, new AuthResultHandler(context));
    }
    public static String getUserDir(){
        return userDir;
    }
    public static void registerSubscriptionListener(final SubscriptionListener listener){
        Log.i("Helerp", getUserDir());
        mFirebaseRef.child(getUserDir()+"/subscriptions/").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                user.getSubscriptions().add(dataSnapshot.getKey());
                FirebaseGroup.accessGroup(dataSnapshot.getKey()).registerGroupListener(new GroupListener() {
                    @Override
                    public void messageChange(Group group, List<Message> msgs) {

                    }

                    @Override
                    public void metaDataChange(Group group, Group.Metadata meta) {
                        //Only add the sub when metadata is in so that ui can display it's name properly
                        listener.subAdded(group);
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                user.getSubscriptions().remove(dataSnapshot.getKey());
                listener.subRemoved(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public interface SubscriptionListener {
        public void subAdded(Group sub);
        public void subRemoved(String subName);
    }

    public static User getCurrentUserInfo(){
        return user;
    }
    public static void getOtherUserInfo(String UID, final UserInfoCallback callback){
        FirebaseHelper.getFirebaseRef().child("users/"+UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> children = dataSnapshot.getChildren().iterator();
                callback.infoArrived(new FirebaseGoogleUser((String)children.next().getValue(),(String)children.next().getValue(),(String)children.next().getValue(),(String)children.next().getValue()));
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
