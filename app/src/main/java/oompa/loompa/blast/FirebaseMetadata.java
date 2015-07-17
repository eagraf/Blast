package oompa.loompa.blast;

/**
 * Created by Da-Jin on 7/15/2015.
 */

public class FirebaseMetadata implements Group.Metadata {
    private String ownerUID;
    private boolean isPublic;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private FirebaseMetadata() {
    }

    public FirebaseMetadata(String ownerUID, boolean isPublic) {
        this.ownerUID = ownerUID;
        this.isPublic = isPublic;
    }
    public String getOwnerUID(){
        return ownerUID;
    }
    public boolean getIsPublic(){
        return isPublic;
    }
}
