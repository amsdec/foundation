package io.carvill.foundation.email.mandrill;

import io.carvill.foundation.email.Email;
import io.carvill.foundation.email.Recipient;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class MandrillEmail<T extends Recipient> extends Email<T> {

    public MandrillEmail(final String fromName, final String fromEmail, final String template, final String subject) {
        super(fromName, fromEmail, template, subject);
    }

    @Override
    public Email<T> addAttachment(final String type, final String name, final String content) {
        return this.addAttachment(new MandrillAttachment(type, name, content));
    }

    @Override
    public Email<T> addAttachment(final String type, final String name, final byte[] content) {
        return this.addAttachment(new MandrillAttachment(type, name, content));
    }

    public MandrillMergeLanguage getMandrillMergeLanguage() {
        return MandrillMergeLanguage.mailchimp;
    }

}
