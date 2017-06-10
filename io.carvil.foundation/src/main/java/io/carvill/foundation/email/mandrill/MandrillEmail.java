package io.carvill.foundation.email.mandrill;

import io.carvill.foundation.email.Email;
import io.carvill.foundation.email.MergeLanguage;
import io.carvill.foundation.email.Recipient;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class MandrillEmail<T extends Recipient> extends Email<T> {

    private MergeLanguage mergeLanguage = MergeLanguage.mailchimp;

    public MandrillEmail(final String template, final String subject) {
        super(template, subject);
    }

    @Override
    public Email<T> addAttachment(final String type, final String name, final String content) {
        return this.addAttachment(new MandrillAttachment(type, name, content));
    }

    @Override
    public Email<T> addAttachment(final String type, final String name, final byte[] content) {
        return this.addAttachment(new MandrillAttachment(type, name, content));
    }

    public MergeLanguage getMandrillMergeLanguage() {
        return this.mergeLanguage;
    }

    @Override
    public void setMergeLanguage(final MergeLanguage mergeLanguage) {
        if (mergeLanguage != null) {
            this.mergeLanguage = mergeLanguage;
        }
    }

}
