package io.carvill.foundation.email;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public abstract class EmailSender {

    public abstract <T extends Recipient> void send(final Email<T> email);

    public abstract <T extends Recipient> Email<T> buildEmail(final String fromName, final String fromEmail,
            final String template, final String subject);

}
