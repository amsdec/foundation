package io.carvill.foundation.email.mandrill;

import org.springframework.web.client.RestTemplate;

import io.carvill.foundation.email.Email;
import io.carvill.foundation.email.Recipient;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class MandrillHandlebars extends Mandrill {

    public MandrillHandlebars(final String apiKey, final RestTemplate restTemplate) {
        super(apiKey, restTemplate);
    }

    public MandrillHandlebars(final String apiKey) {
        super(apiKey);
    }

    @Override
    public <T extends Recipient> Email<T> buildEmail(final String fromName, final String fromEmail,
            final String template, final String subject) {
        return new MandrillEmailHandlebars<>(fromName, fromEmail, template, subject);
    }

}
