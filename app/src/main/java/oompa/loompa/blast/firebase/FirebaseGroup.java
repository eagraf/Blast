package oompa.loompa.blast.firebase;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;

import oompa.loompa.blast.FirebaseMetadata;
import oompa.loompa.blast.Group;

/**
 * Created by Da-Jin on 7/14/2015.
 */
public class FirebaseGroup implements Group{
    private final Firebase groupMetaRef;
    private final Firebase groupMessageRef;
    private FirebaseMetadata metadata;


    public static Group createGroup(final String groupName, final FirebaseMetadata meta){
        if(FirebaseHelper.isConnected()){
            FirebaseHelper.getFirebaseRef().child("groups/"+groupName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String available = dataSnapshot.getValue() == null ? "is":"is not";
                    Log.i("Group", groupName+" "+available+" available");
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            return new FirebaseGroup(groupName, meta);
        }
        return null;
    }

    public static Group accessGroup(String groupName){
        if(FirebaseHelper.isConnected()){
            return new FirebaseGroup(groupName);
        }
        return null;
    }

    private FirebaseGroup(String name, FirebaseMetadata meta){
        //This is the constructor to create a group, and set meta
        this(name);
        metadata = meta;
        groupMetaRef.setValue(metadata);
    }

    private FirebaseGroup(String name){
        //This constructor just accesses a pre-existing group

        groupMessageRef = FirebaseHelper.getFirebaseRef().child("messages").child(name);
        groupMessageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Group", "group data snap " + dataSnapshot.getValue());

                Iterable<DataSnapshot> messages = dataSnapshot.getChildren();
                for(DataSnapshot data:messages){
                    Message msg = data.getValue(Message.class);
                    Log.i("Group","subject: "+msg.getSubject()+"\nbody: "+msg.getBody());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        groupMetaRef = FirebaseHelper.getFirebaseRef().child("groups").child(name);
        groupMetaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Group", "group meta snap " + dataSnapshot.getValue());
                metadata = dataSnapshot.getValue(FirebaseMetadata.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public boolean isGroupOwner(String UID) {
        return metadata.getOwnerUID().equals(UID);
    }

    @Override
    public FirebaseMetadata getGroupMetadata() {
        return metadata;
    }

    @Override
    public List<Message> getGroupMessages() {
        return null;
    }

    @Override
    public void post(Message message){
        groupMessageRef.push().setValue(message);
    }
}