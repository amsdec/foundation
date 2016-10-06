package io.carvill.foundation.mandrill;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class TemplateRequest implements Serializable {

    private static final long serialVersionUID = 6388281617597361944L;

    private final String key;

    @JsonProperty("template_name")
    private final String templateName;

    @JsonProperty("template_content")
    private final Object[] templateContent = new Object[] {};

    private final TemplateMessage message;

    public TemplateRequest(final String key, final String templateName, final TemplateMessage message) {
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

    public TemplateMessage getMessage() {
        return this.message;
    }

}
