package oompa.loompa.blast.firebase;

import com.firebase.client.AuthData;

import java.util.ArrayList;
import java.util.List;

import oompa.loompa.blast.User;

/**
 * Created by Da-Jin on 7/18/2015.
 */
public class FirebaseGoogleUser implements User {
    private String profileImageURL, UID, displayName, email;
    private List<String> subscriptions;

    @SuppressWarnings("unused")
    private FirebaseGoogleUser(){

    }

    public FirebaseGoogleUser(String UID, String displayName, String email, String profileImageURL){
        this.UID = UID;
        this.displayName = displayName;
        this.email = email;
        this.profileImageURL = profileImageURL;
    }
    //This is only for the logged in user, because you can only get authdata for yourself
    protected FirebaseGoogleUser(AuthData authData){
        profileImageURL = (String) authData.getProviderData().get("profileImageURL");
        UID = authData.getUid();
        displayName = (String) authData.getProviderData().get("displayName");
        email = (String) authData.getProviderData().get("email");
        subscriptions = new ArrayList<>();
    }

    @Override
    public String getProfileImageURL() {
        return profileImageURL;
    }

    @Override
    public String getUID() {
        return UID;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public List<String> getSubscriptions(){
        return subscriptions;
    }

}
