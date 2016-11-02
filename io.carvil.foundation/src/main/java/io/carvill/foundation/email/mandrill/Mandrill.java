package io.carvill.foundation.email.mandrill;

import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import io.carvill.foundation.email.Email;
import io.carvill.foundation.email.EmailSender;
import io.carvill.foundation.email.Recipient;
import io.carvill.foundation.email.SentResult;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class Mandrill extends EmailSender {

    public static final String TEMPLATE_API_URL = "https://mandrillapp.com/api/1.0/messages/send-template.json";

    private final RestTemplate restTemplate;

    private String apiKey;

    public Mandrill(final String fromEmail, final String fromName, final String apiKey) {
        this(fromEmail, fromName, apiKey, new RestTemplate());
    }

    public Mandrill(final String fromEmail, final String fromName, final String apiKey,
            final RestTemplate restTemplate) {
        super(fromEmail, fromName);
        this.apiKey = apiKey;
        this.restTemplate = restTemplate;
    }

    @Override
    public <T extends Recipient> SentResult send(final Email<T> email) {
        Assert.isTrue(email instanceof MandrillEmail);
        final MandrillEmail<T> info = (MandrillEmail<T>) email;

        final MandrillTemplateMessage<T> message = new MandrillTemplateMessage<T>(info.getTemplate(), info.getSubject(),
                this.getFromEmail(), this.getFromName()).replyTo(this.getReplyTo()).withHeaders(info.getHeaders())
                        .withAttachments(info.getAttachments()).withRecipients(info.getRecipients())
                        .withMergeLanguage(info.getMandrillMergeLanguage())
                        .applyVariables(info.getRecipients(), info.getVariableProvider());

        final MandrillTemplateRequest<T> request = new MandrillTemplateRequest<>(this.apiKey, message.getTemplate(),
                message);
        return this.send(request);
    }

    protected <T extends Recipient> SentResult send(final MandrillTemplateRequest<T> request) {
        final SentResult result = new SentResult();
        final MandrillTemplateResponse[] responses = this.restTemplate.postForObject(TEMPLATE_API_URL, request,
                MandrillTemplateResponse[].class);
        for (final MandrillTemplateResponse response : responses) {
            switch (response.getStatus()) {
            case invalid:
            case rejected:
                result.reportError(response.getEmail(), response.getRejectReason().name());
                break;
            default:
                result.incrementSuccess();
            }
        }
        return result;
    }

    @Override
    public <T extends Recipient> Email<T> buildEmail(final String template, final String subject) {
        return new MandrillEmail<>(template, subject);
    }

}
