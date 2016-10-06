package io.carvill.foundation.mandrill;

import org.springframework.web.client.RestTemplate;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class Mandrill {

    public static final String TEMPLATE_API_URL = "https://mandrillapp.com/api/1.0/messages/send-template.json";

    private final RestTemplate restTemplate;

    private String apiKey;

    public Mandrill(final String apiKey) {
        this(apiKey, new RestTemplate());
    }

    public Mandrill(final String apiKey, final RestTemplate restTemplate) {
        this.apiKey = apiKey;
        this.restTemplate = restTemplate;
    }

    public void sendTemplate(final TemplateMessage message) {
        this.sendTemplate(message, null);
    }

    public void sendTemplate(final TemplateMessage message, final FailedCallback failedCallback) {
        final TemplateRequest request = new TemplateRequest(this.apiKey, message.getTemplate(), message);
        final TemplateResponse[] responses = this.restTemplate.postForObject(TEMPLATE_API_URL, request,
                TemplateResponse[].class);
        if (failedCallback != null) {
            for (final TemplateResponse response : responses) {
                switch (response.getStatus()) {
                case invalid:
                case rejected:
                    failedCallback.report(response.getEmail(), response.getStatus(), response.getRejectReason());
                default:
                }
            }
        }
    }

}
