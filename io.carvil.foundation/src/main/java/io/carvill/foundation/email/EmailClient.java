package io.carvill.foundation.email;

import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class EmailClient {

    private final static Logger log = LoggerFactory.getLogger(EmailClient.class);

    private final EmailSender emailSender;

    private final TaskExecutor taskExecutor;

    public EmailClient(final EmailSender emailSender, final TaskExecutor taskExecutor) {
        this.emailSender = emailSender;
        this.taskExecutor = taskExecutor;
    }

    public <T extends Recipient> SentResult send(final Email<T> email) {
        if (CollectionUtils.isEmpty(email.getRecipients())) {
            return new SentResult();
        }

        final SentResult result = EmailClient.this.emailSender.send(email);
        log.debug("Showing email result: {}", result);
        return result;
    }

    public <T extends Recipient> void sendAsync(final Email<T> email) {
        this.sendAsync(email, null);
    }

    public <T extends Recipient> void sendAsync(final Email<T> email, final Consumer<SentResult> notifier) {
        this.taskExecutor.execute(new Runnable() {

            @Override
            public void run() {
                log.debug("Sending template {} using {}", email.getTemplate(),
                        EmailClient.this.emailSender.getClass().getSimpleName());
                final SentResult result = EmailClient.this.send(email);
                if (notifier != null) {
                    notifier.accept(result);
                }
            }

        });
    }

    public <T extends Recipient> Email<T> buildEmail(final String template, final String subject) {
        return this.emailSender.buildEmail(template, subject);
    }

    public <T extends Recipient> Email<T> buildEmail(final String template, final String subject,
            final MergeLanguage mergeLanguage) {
        return this.emailSender.buildEmail(template, subject, mergeLanguage);
    }

}
