package io.carvill.foundation.email.mandrill;

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
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class MandrillTemplateMessage<T extends Recipient> implements Serializable {

    private static final long serialVersionUID = 7003667543188258113L;

    @JsonIgnore
    private final String template;

    private final String subject;

    @JsonProperty("from_email")
    private final String fromEmail;

    @JsonProperty("from_name")
    private final String fromName;

    @JsonProperty("to")
    private List<To> recipients;

    private boolean merge;

    @JsonProperty("merge_language")
    private MandrillMergeLanguage mergeLanguage = MandrillMergeLanguage.mailchimp;

    @JsonProperty("merge_vars")
    private List<MandrillMergeVariables> mergeVariables;

    @JsonProperty("attachments")
    private List<Attachment> attachments;

    private Map<String, String> headers;

    @JsonProperty("html")
    private String html;

    public MandrillTemplateMessage(final String template, final String subject, final String fromEmail,
            final String fromName) {
        this.template = template;
        this.subject = subject;
        this.fromEmail = fromEmail;
        this.fromName = fromName;
    }

    public MandrillTemplateMessage<T> withRecipients(final List<T> recipients) {
        if (CollectionUtils.isNotEmpty(recipients)) {
            for (T t : recipients) {
                this.withRecipient(t);
            }
        }
        return this;
    }

    public MandrillTemplateMessage<T> withRecipient(final T recipient) {
        return this.withRecipient(new To(recipient));
    }

    public MandrillTemplateMessage<T> withRecipient(final To to) {
        if (this.recipients == null) {
            this.recipients = new ArrayList<>();
        }
        this.recipients.add(to);
        return this;
    }

    public MandrillTemplateMessage<T> withAttachments(final List<Attachment> attachments) {
        if (CollectionUtils.isNotEmpty(attachments)) {
            if (this.attachments == null) {
                this.attachments = attachments;
            } else {
                this.attachments.addAll(attachments);
            }
        }
        return this;
    }

    public MandrillTemplateMessage<T> withAttachment(final Attachment attachment) {
        if (this.attachments == null) {
            this.attachments = new ArrayList<>();
        }
        this.attachments.add(attachment);
        return this;
    }

    public MandrillTemplateMessage<T> withMergeLanguage(final MandrillMergeLanguage mergeLanguage) {
        this.mergeLanguage = mergeLanguage;
        return this;
    }

    public MandrillTemplateMessage<T> withHeaders(final Map<String, String> headers) {
        if (MapUtils.isNotEmpty(headers)) {
            if (this.headers == null) {
                this.headers = headers;
            } else {
                this.headers.putAll(headers);
            }
        }
        return this;
    }

    public MandrillTemplateMessage<T> withHeader(final String name, final String value) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(name, value);
        return this;
    }

    public MandrillTemplateMessage<T> replyTo(final String replyEmail) {
        return this.withHeader("Reply-To", replyEmail);
    }

    public MandrillTemplateMessage<T> applyVariables(final List<T> recipients,
            final VariableProvider<T> variableProvider) {
        if (this.mergeVariables == null) {
            this.mergeVariables = new ArrayList<>();
        }

        if (CollectionUtils.isEmpty(this.recipients)) {
            throw new IllegalStateException("First add recipients and then call this method");
        }

        for (final T recipient : recipients) {
            this.mergeVariables.add(new MandrillMergeVariables(recipient.getEmail())
                    .withVariables(variableProvider.getVariables(recipient)));
        }
        return this;
    }

    public List<To> getRecipients() {
        return this.recipients;
    }

    public void setRecipients(final List<To> recipients) {
        this.recipients = recipients;
    }

    public boolean isMerge() {
        return this.merge;
    }

    public void setMerge(final boolean merge) {
        this.merge = merge;
    }

    public MandrillMergeLanguage getMergeLanguage() {
        return this.mergeLanguage;
    }

    public void setMergeLanguage(final MandrillMergeLanguage mergeLanguage) {
        this.mergeLanguage = mergeLanguage;
    }

    public List<MandrillMergeVariables> getMergeVariables() {
        return this.mergeVariables;
    }

    public void setMergeVariables(final List<MandrillMergeVariables> mergeVariables) {
        this.mergeVariables = mergeVariables;
    }

    public List<Attachment> getAttachments() {
        return this.attachments;
    }

    public void setAttachments(final List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders(final Map<String, String> headers) {
        this.headers = headers;
    }

    public String getHtml() {
        return this.html;
    }

    public void setHtml(final String html) {
        this.html = html;
    }

    public String getTemplate() {
        return this.template;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getFromEmail() {
        return this.fromEmail;
    }

    public String getFromName() {
        return this.fromName;
    }

}
