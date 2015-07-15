package oompa.loompa.blast.firebase;

import android.content.Context;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import oompa.loompa.blast.FirebaseMetadata;

/**
 * Created by Da-Jin on 7/14/2015.
 */
public class FirebaseHelper {
    private static String FIREBASE_URL = "https://android-chat-1.firebaseio.com/";
    private static Firebase mFirebaseRef;
    private static ValueEventListener mConnectedListener;
    private static Boolean connected = false;


    public static void onCreate(Context context){
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
    public static void authEmail(String email, String password){
        mFirebaseRef.authWithPassword(email,password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("provider", authData.getProvider());
                if(authData.getProviderData().containsKey("id")) {
                    map.put("provider_id", authData.getProviderData().get("id").toString());
                }
                if(authData.getProviderData().containsKey("displayName")) {
                    map.put("displayName", authData.getProviderData().get("displayName").toString());
                }
                mFirebaseRef.child("users").child(authData.getUid()).setValue(map);

                FirebaseMetadata meta = new FirebaseMetadata(authData.getUid(),true);
                FirebaseGroup.createGroup("beta",meta).post(new Message("First","lel"));
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // there was an error
            }
        });
    }
}
