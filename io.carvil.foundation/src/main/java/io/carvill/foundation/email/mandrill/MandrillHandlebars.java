package io.carvill.foundation.email.mandrill;

import org.springframework.web.client.RestTemplate;

import io.carvill.foundation.email.Email;
import io.carvill.foundation.email.Recipient;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class MandrillHandlebars extends Mandrill {

    public MandrillHandlebars(final String fromEmail, final String fromName, final String apiKey,
            final RestTemplate restTemplate) {
        super(fromEmail, fromName, apiKey, restTemplate);
    }

    public MandrillHandlebars(final String fromEmail, final String fromName, final String apiKey) {
        super(fromEmail, fromName, apiKey);
    }

    @Override
    public <T extends Recipient> Email<T> buildEmail(final String template, final String subject) {
        return new MandrillEmailHandlebars<>(template, subject);
    }

}
