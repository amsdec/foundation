package io.carvill.foundation.mandrill;

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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplateMessage implements Serializable {

    private static final long serialVersionUID = 7003667543188258113L;

    @JsonIgnore
    private final String template;

    private final String subject;

    @JsonProperty("from_email")
    private final String fromEmail;

    @JsonProperty("from_name")
    private final String fromName;

    @JsonProperty("to")
    private List<Recipient> recipients;

    private boolean merge;

    @JsonProperty("merge_language")
    private MergeLanguage mergeLanguage = MergeLanguage.mailchimp;

    @JsonProperty("merge_vars")
    private List<MergeVariables> mergeVariables;

    @JsonProperty("attachments")
    private List<Attachment> attachments;

    private Map<String, String> headers;

    @JsonProperty("html")
    private String html;

    public TemplateMessage(final String template, final String subject, final String fromEmail, final String fromName) {
        this.template = template;
        this.subject = subject;
        this.fromEmail = fromEmail;
        this.fromName = fromName;
    }

    public TemplateMessage withRecipient(final List<Recipient> recipients) {
        if (CollectionUtils.isNotEmpty(recipients)) {
            if (this.recipients == null) {
                this.recipients = recipients;
            } else {
                this.recipients.addAll(recipients);
            }
        }
        return this;
    }

    public TemplateMessage addRecipient(final Recipient recipient) {
        if (this.recipients == null) {
            this.recipients = new ArrayList<>();
        }
        this.recipients.add(recipient);
        return this;
    }

    public TemplateMessage withAttachments(final List<Attachment> attachments) {
        if (CollectionUtils.isNotEmpty(attachments)) {
            if (this.attachments == null) {
                this.attachments = attachments;
            } else {
                this.attachments.addAll(attachments);
            }
        }
        return this;
    }

    public TemplateMessage addAttachment(final Attachment attachment) {
        if (this.attachments == null) {
            this.attachments = new ArrayList<>();
        }
        this.attachments.add(attachment);
        return this;
    }

    public TemplateMessage useHandlebars() {
        return this.withMergeLanguage(MergeLanguage.handlebars);
    }

    public TemplateMessage withMergeLanguage(final MergeLanguage mergeLanguage) {
        this.mergeLanguage = mergeLanguage;
        return this;
    }

    public TemplateMessage withHeaders(final Map<String, String> headers) {
        if (MapUtils.isNotEmpty(headers)) {
            if (this.headers == null) {
                this.headers = headers;
            } else {
                this.headers.putAll(headers);
            }
        }
        return this;
    }

    public TemplateMessage withHeader(final String name, final String value) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(name, value);
        return this;
    }

    public TemplateMessage withVariables(final VariableProvider variableProvider) {
        if (this.mergeVariables == null) {
            this.mergeVariables = new ArrayList<>();
        }

        if (CollectionUtils.isEmpty(this.recipients)) {
            throw new IllegalStateException("First add recipients and then call this method");
        }

        for (final Recipient recipient : this.recipients) {
            this.mergeVariables.add(
                    new MergeVariables(recipient.getEmail()).withVariables(variableProvider.getVariables(recipient)));
        }
        return this;
    }

    public List<Recipient> getRecipients() {
        return this.recipients;
    }

    public void setRecipients(final List<Recipient> recipients) {
        this.recipients = recipients;
    }

    public boolean isMerge() {
        return this.merge;
    }

    public void setMerge(final boolean merge) {
        this.merge = merge;
    }

    public MergeLanguage getMergeLanguage() {
        return this.mergeLanguage;
    }

    public void setMergeLanguage(final MergeLanguage mergeLanguage) {
        this.mergeLanguage = mergeLanguage;
    }

    public List<MergeVariables> getMergeVariables() {
        return this.mergeVariables;
    }

    public void setMergeVariables(final List<MergeVariables> mergeVariables) {
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
