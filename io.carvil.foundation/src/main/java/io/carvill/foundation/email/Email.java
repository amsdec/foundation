package io.carvill.foundation.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public abstract class Email<T extends Recipient> {

    private String fromName;

    private String fromEmail;

    private String template;

    private String subject;

    private List<T> recipients;

    private List<Attachment> attachments;

    private Map<String, String> headers;

    private VariableProvider<T> variableProvider;

    private SentResult result;

    public Email(final String fromName, final String fromEmail, final String template, final String subject) {
        this.fromName = fromName;
        this.fromEmail = fromEmail;
        this.template = template;
        this.subject = subject;
        this.result = new SentResult();
    }

    public Email<T> withRecipient(final List<T> recipients) {
        if (CollectionUtils.isNotEmpty(recipients)) {
            if (this.recipients == null) {
                this.recipients = recipients;
            } else {
                this.recipients.addAll(recipients);
            }
        }
        return this;
    }

    public Email<T> withRecipient(final T recipient) {
        if (this.recipients == null) {
            this.recipients = new ArrayList<>();
        }
        this.recipients.add(recipient);
        return this;
    }

    public Email<T> withHeaders(final Map<String, String> headers) {
        if (MapUtils.isNotEmpty(headers)) {
            if (this.headers == null) {
                this.headers = headers;
            } else {
                this.headers.putAll(headers);
            }
        }
        return this;
    }

    public Email<T> withHeader(final String name, final String value) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(name, value);
        return this;
    }

    public Email<T> withVariableProvider(final VariableProvider<T> variableProvider) {
        this.variableProvider = variableProvider;
        return this;
    }

    public Email<T> replyTo(final String replyEmail) {
        return this.withHeader("Reply-To", replyEmail);
    }

    public abstract Email<T> addAttachment(final String type, final String name, final String content);

    public abstract Email<T> addAttachment(final String type, final String name, final byte[] content);

    protected Email<T> addAttachment(final Attachment attachment) {
        if (this.attachments == null) {
            this.attachments = new ArrayList<>();
        }
        this.attachments.add(attachment);
        return this;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(final String fromName) {
        this.fromName = fromName;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(final String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(final String template) {
        this.template = template;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public List<T> getRecipients() {
        return recipients;
    }

    public void setRecipients(final List<T> recipients) {
        this.recipients = recipients;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(final List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(final Map<String, String> headers) {
        this.headers = headers;
    }

    public VariableProvider<T> getVariableProvider() {
        return variableProvider;
    }

    public void setVariableProvider(final VariableProvider<T> variableProvider) {
        this.variableProvider = variableProvider;
    }
    
    public SentResult getResult() {
        return result;
    }
    
    public void setResult(final SentResult result) {
        this.result = result;
    }

}
