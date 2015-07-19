package oompa.loompa.blast.firebase;

import oompa.loompa.blast.Group;

/**
 * Created by Da-Jin on 7/15/2015.
 */

public class FirebaseMetadata implements Group.Metadata {
    private String ownerUID, displayName;
    private boolean isPublic;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private FirebaseMetadata() {
    }

    public FirebaseMetadata(String displayName, String ownerUID, boolean isPublic) {
        this.displayName = displayName;
        this.ownerUID = ownerUID;
        this.isPublic = isPublic;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
    public String getOwnerUID(){
        return ownerUID;
    }
    public boolean getIsPublic(){
        return isPublic;
    }
}
