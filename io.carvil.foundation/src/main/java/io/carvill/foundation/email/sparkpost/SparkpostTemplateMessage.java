package io.carvill.foundation.email.sparkpost;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.carvill.foundation.email.Attachment;
import io.carvill.foundation.email.Recipient;
import io.carvill.foundation.email.To;
import io.carvill.foundation.email.VariableProvider;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SparkpostTemplateMessage<T extends Recipient> implements Serializable {

    private static final long serialVersionUID = 7003667543188258113L;

    private final Map<String, Object> options;

    private final Map<String, Object> content;

    private final Map<String, String> headers;

    private final List<Attachment> attachments;

    private final List<SparkpostRecipient> recipients;

    public SparkpostTemplateMessage(final String template, final String subject, final String fromEmail,
            final String fromName) {
        this.options = new HashMap<>();
        this.options.put("open_tracking", true);
        this.options.put("click_tracking", true);
        this.options.put("transactional", true);
        this.options.put("sandbox", false);

        this.headers = new HashMap<>();
        this.attachments = new ArrayList<>();

        this.content = new HashMap<>();
        this.content.put("template_id", template);
        this.content.put("use_draft_template", false);
        this.content.put("from", new To(fromEmail, fromName));
        this.content.put("subject", subject);
        this.content.put("headers", this.headers);
        this.content.put("attachments", this.attachments);

        this.recipients = new ArrayList<>();
    }

    public SparkpostTemplateMessage<T> withRecipients(final List<T> recipients,
            final VariableProvider<T> variableProvider) {
        if (CollectionUtils.isNotEmpty(recipients)) {
            for (T t : recipients) {
                this.withRecipient(t, variableProvider);
            }
        }
        return this;
    }

    public SparkpostTemplateMessage<T> withRecipient(final T recipient, final VariableProvider<T> variableProvider) {
        final SparkpostRecipient item = new SparkpostRecipient();
        item.setAddress(new To(recipient));
        if (variableProvider != null) {
            item.setSubstitutionData(variableProvider.getVariables(recipient));
        }
        this.recipients.add(item);
        return this;
    }

    public SparkpostTemplateMessage<T> withHeaders(final Map<String, String> headers) {
        if (MapUtils.isNotEmpty(headers)) {
            for (String name : headers.keySet()) {
                this.withHeader(name, headers.get(name));
            }
        }
        return this;
    }

    public SparkpostTemplateMessage<T> withHeader(final String name, final String value) {
        this.headers.put(name, value);
        return this;
    }

    public SparkpostTemplateMessage<T> replyTo(final String replyEmail) {
        return this.withHeader("reply_to", replyEmail);
    }

    public SparkpostTemplateMessage<T> withAttachments(final List<Attachment> attachments) {
        if (CollectionUtils.isNotEmpty(attachments)) {
            for (Attachment attachment : attachments) {
                this.withAttachment(attachment);
            }
        }
        return this;
    }

    public SparkpostTemplateMessage<T> withAttachment(final Attachment attachment) {
        this.attachments.add(attachment);
        return this;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    @JsonIgnore
    public Map<String, String> getHeaders() {
        return headers;
    }

    @JsonIgnore
    public List<Attachment> getAttachments() {
        return attachments;
    }

    public List<SparkpostRecipient> getRecipients() {
        return recipients;
    }

}
