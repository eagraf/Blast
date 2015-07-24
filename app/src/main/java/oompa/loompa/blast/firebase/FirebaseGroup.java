package oompa.loompa.blast.firebase;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import oompa.loompa.blast.Group;
import oompa.loompa.blast.GroupListener;
import oompa.loompa.blast.MessageActivity;
import oompa.loompa.blast.R;

/**
 * Created by Da-Jin on 7/14/2015.
 */
public class FirebaseGroup implements Group {
    private final Firebase groupMetaRef;
    private final Firebase groupMessageRef;
    private FirebaseMetadata metadata;
    private GroupListener listener;
    private String UID;
    private ValueEventListener metaListener, messageListener;
    private Map<String, Message> messages;


    public static Group createGroup(final FirebaseMetadata meta){
        Firebase messageRef = FirebaseHelper.getFirebaseRef().child("groups").push();
        String UID = messageRef.getKey();
        if(FirebaseHelper.isConnected()){
            /*FirebaseHelper.getFirebaseRef().child("groups/"+displayName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String available = dataSnapshot.getValue() == null ? "is":"is not";
                    Log.i("Group", displayName + " " + available + " available");
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });*/
            Group group = new FirebaseGroup(UID,meta);
            group.subscribe();
            return group;
        }
        return null;
    }

    public static Group accessGroup(String groupUID){
        if(FirebaseHelper.isConnected()){
            return new FirebaseGroup(groupUID);
        }
        return null;
    }

    private FirebaseGroup(String UID, FirebaseMetadata meta){
        //This is the constructor to create a group, and set meta
        this(UID);
        metadata = meta;
        groupMetaRef.setValue(metadata);
    }

    private FirebaseGroup(String UID){
        //This constructor just accesses a pre-existing group
        this.UID = UID;
        groupMessageRef = FirebaseHelper.getFirebaseRef().child("messages").child(UID);
        groupMetaRef = FirebaseHelper.getFirebaseRef().child("groups").child(UID);

    }

    @Override
    public void subscribe(){
        Firebase ref = FirebaseHelper.getFirebaseRef().child(FirebaseHelper.getUserDir()+"/subscriptions/"+ UID);
        ref.setValue(true);
    }
    @Override
    public void unsubscribe(){
        Firebase ref = FirebaseHelper.getFirebaseRef().child(FirebaseHelper.getUserDir()+"/subscriptions/"+ UID);
        ref.setValue(null);
    }

    @Override
    public void registerGroupListener(final GroupListener listener) {
        this.listener = listener;
        if(messageListener!=null) {
            groupMessageRef.removeEventListener(messageListener);
            groupMetaRef.removeEventListener(metaListener);
        }
        messageListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Group", "group data snap " + dataSnapshot.getValue());

                Map<String, Message> messageList = new TreeMap<>();
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                for(DataSnapshot datum:data){
                    Message msg = datum.getValue(Message.class);
                    Log.i("Group","subject: "+msg.getSubject()+"\nbody: "+msg.getBody());
                    messageList.put(datum.getKey(), msg);
                    listener.messageAdded(FirebaseGroup.this, msg);
                }
                messages = messageList;
                listener.messageChange(FirebaseGroup.this, messageList);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        metaListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Group", "group meta snap " + dataSnapshot.getValue());
                metadata = dataSnapshot.getValue(FirebaseMetadata.class);
                listener.metaDataChange(FirebaseGroup.this, metadata);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        groupMessageRef.addValueEventListener(messageListener);
        groupMetaRef.addValueEventListener(metaListener);
    }

    @Override
    public Map<String, Message> getMessages() {
        return messages;
    }

    @Override
    public void post(Message message){
        groupMessageRef.push().setValue(message);
    }

    public FirebaseMetadata getMetadata() {
        return metadata;
    }

    public String getUID() { return UID; }
}
