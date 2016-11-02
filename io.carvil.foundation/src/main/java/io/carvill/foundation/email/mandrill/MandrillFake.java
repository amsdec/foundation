package io.carvill.foundation.email.mandrill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.carvill.foundation.email.Recipient;
import io.carvill.foundation.email.SentResult;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class MandrillFake extends Mandrill {

    private final static Logger log = LoggerFactory.getLogger(MandrillFake.class);

    private ObjectMapper objectMapper;

    public MandrillFake(final String fromEmail, final String fromName, final String apiKey,
            final ObjectMapper objectMapper) {
        super(fromEmail, fromName, apiKey);
        this.objectMapper = objectMapper;
    }

    @Override
    protected <T extends Recipient> SentResult send(final MandrillTemplateRequest<T> request) {
        final SentResult result = new SentResult();
        try {
            log.info("EMAIL '{}'", this.objectMapper.writeValueAsString(request));
            result.setSuccess(request.getMessage().getRecipients().size());
        } catch (final JsonProcessingException e) {
            log.error("Unable to generate JSON of message", e);
            result.setFailed(request.getMessage().getRecipients().size());
        }
        return result;
    }

}
