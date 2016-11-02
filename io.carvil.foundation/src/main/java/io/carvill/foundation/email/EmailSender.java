package io.carvill.foundation.email;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public abstract class EmailSender {

    private final String fromEmail;

    private final String fromName;

    private String replyTo;

    public EmailSender(final String fromEmail, final String fromName) {
        this.fromEmail = fromEmail;
        this.fromName = fromName;
    }

    public abstract <T extends Recipient> SentResult send(final Email<T> email);

    public abstract <T extends Recipient> Email<T> buildEmail(final String template, final String subject);

    public String getFromEmail() {
        return this.fromEmail;
    }

    public String getFromName() {
        return this.fromName;
    }

    public String getReplyTo() {
        return this.replyTo;
    }

    public void setReplyTo(final String replyTo) {
        this.replyTo = replyTo;
    }

}
