package oompa.loompa.blast.firebase;

/**
 * Created by Da-Jin on 7/14/2015.
 */
public class Message {

    private String subject;
    private String body;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Message() {
    }

    public Message(String subject, String body) {
        this.body = body;
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public String getSubject() {
        return subject;
    }
}
