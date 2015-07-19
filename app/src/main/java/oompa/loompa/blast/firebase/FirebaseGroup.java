package oompa.loompa.blast.firebase;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import oompa.loompa.blast.Group;
import oompa.loompa.blast.GroupListener;

/**
 * Created by Da-Jin on 7/14/2015.
 */
public class FirebaseGroup implements Group{
    private final Firebase groupMetaRef;
    private final Firebase groupMessageRef;
    private FirebaseMetadata metadata;
    private GroupListener listener;
    private String name;


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
            Group group = new FirebaseGroup(groupName, meta);
            group.subscribe();
            return group;
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
        this.name = name;
        groupMessageRef = FirebaseHelper.getFirebaseRef().child("messages").child(name);
        groupMetaRef = FirebaseHelper.getFirebaseRef().child("groups").child(name);

    }

    @Override
    public void subscribe(){
        Firebase ref = FirebaseHelper.getFirebaseRef().child(FirebaseHelper.getUserDir()+"/subscriptions/"+name);
        ref.setValue(true);
    }
    @Override
    public void unsubscribe(){
        Firebase ref = FirebaseHelper.getFirebaseRef().child(FirebaseHelper.getUserDir()+"/subscriptions/"+name);
        ref.setValue(null);
    }

    @Override
    public void registerGroupListener(final GroupListener listener) {
        this.listener = listener;
        groupMessageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Group", "group data snap " + dataSnapshot.getValue());

                ArrayList<Message> messageList = new ArrayList<Message>();
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                for(DataSnapshot datum:data){
                    Message msg = datum.getValue(Message.class);
                    Log.i("Group","subject: "+msg.getSubject()+"\nbody: "+msg.getBody());
                    messageList.add(msg);
                }
                listener.messageChange(FirebaseGroup.this, messageList);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        groupMetaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Group", "group meta snap " + dataSnapshot.getValue());
                metadata = dataSnapshot.getValue(FirebaseMetadata.class);
                listener.metaDataChange(FirebaseGroup.this, metadata);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void post(Message message){
        groupMessageRef.push().setValue(message);
    }

    public FirebaseMetadata getMetadata() {
        return metadata;
    }

    public String getName() { return name; }
}
