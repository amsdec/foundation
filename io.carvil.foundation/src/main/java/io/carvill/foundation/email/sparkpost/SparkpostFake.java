package io.carvill.foundation.email.sparkpost;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.carvill.foundation.email.Recipient;
import io.carvill.foundation.email.SentResult;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class SparkpostFake extends Sparkpost {

    private final static Logger log = LoggerFactory.getLogger(SparkpostFake.class);

    private ObjectMapper objectMapper;

    public SparkpostFake(final String fromEmail, final String fromName, final String apiKey,
            final ObjectMapper objectMapper) throws UnsupportedEncodingException {
        super(fromEmail, fromName, apiKey);
        this.objectMapper = objectMapper;
    }

    @Override
    protected <T extends Recipient> SentResult send(final SparkpostTemplateMessage<T> request) {
        final SentResult result = new SentResult();
        try {
            log.info("EMAIL '{}'", this.objectMapper.writeValueAsString(request));
            result.setSuccess(request.getRecipients().size());
        } catch (final JsonProcessingException e) {
            log.error("Unable to generate JSON of message", e);
            result.setFailed(request.getRecipients().size());
        }
        return result;
    }

}
