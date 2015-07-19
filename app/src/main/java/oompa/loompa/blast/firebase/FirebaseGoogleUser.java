package oompa.loompa.blast.firebase;

import com.firebase.client.AuthData;

import oompa.loompa.blast.User;

/**
 * Created by Da-Jin on 7/18/2015.
 */
public class FirebaseGoogleUser implements User {
    private String profileImageURL, UID, displayName, email;

    @SuppressWarnings("unused")
    private FirebaseGoogleUser(){

    }

    //This is only for the logged in user, because you can only get authdata for yourself
    protected FirebaseGoogleUser(AuthData authData){
        profileImageURL = (String) authData.getProviderData().get("profileImageURL");
        UID = authData.getUid();
        displayName = (String) authData.getProviderData().get("displayName");
        email = (String) authData.getProviderData().get("email");
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
}
