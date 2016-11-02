package io.carvill.foundation.email.mandrill;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.carvill.foundation.email.Recipient;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class MandrillTemplateRequest<T extends Recipient> implements Serializable {

    private static final long serialVersionUID = 6388281617597361944L;

    private final String key;

    @JsonProperty("template_name")
    private final String templateName;

    @JsonProperty("template_content")
    private final Object[] templateContent = new Object[] {};

    private final MandrillTemplateMessage<T> message;

    public MandrillTemplateRequest(final String key, final String templateName, final MandrillTemplateMessage<T> message) {
        this.key = key;
        this.templateName = templateName;
        this.message = message;
    }

    public String getKey() {
        return this.key;
    }

    public String getTemplateName() {
        return this.templateName;
    }

    public Object[] getTemplateContent() {
        return this.templateContent;
    }

    public MandrillTemplateMessage<T> getMessage() {
        return this.message;
    }

}
