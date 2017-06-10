package io.carvill.foundation.email;

import java.util.function.Consumer;

/**
 * Esta clase sólo sobreescribe el método {@link SyncEmailClient#sendAsync(Email, Consumer)} para que los correos
 * SIEMPRE sean enviados en el mismo thread.
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class SyncEmailClient extends EmailClient {

    public SyncEmailClient(final EmailSender emailSender) {
        super(emailSender, null);
    }

    @Override
    public <T extends Recipient> void sendAsync(final Email<T> email, final Consumer<SentResult> notifier) {
        final SentResult result = super.send(email);
        if (notifier != null) {
            notifier.accept(result);
        }
    }
}
