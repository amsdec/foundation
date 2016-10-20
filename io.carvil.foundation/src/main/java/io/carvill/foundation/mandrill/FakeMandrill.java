package io.carvill.foundation.mandrill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class FakeMandrill extends Mandrill {

    private final static Logger log = LoggerFactory.getLogger(FakeMandrill.class);

    private ObjectMapper objectMapper;

    public FakeMandrill(final String apiKey, final ObjectMapper objectMapper) {
        super(apiKey);
        this.objectMapper = objectMapper;
    }

    @Override
    public <T extends Recipient> void sendTemplate(final TemplateMessage<T> message,
            final FailedCallback failedCallback) {
        try {
            log.info("EMAIL '{}'", this.objectMapper.writeValueAsString(message));
        } catch (final JsonProcessingException e) {
            log.error("Unable to generate JSON of message", e);
        }
    }

}
