package io.carvill.foundation.email.mandrill;

import io.carvill.foundation.email.Recipient;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class MandrillEmailHandlebars<T extends Recipient> extends MandrillEmail<T> {

    public MandrillEmailHandlebars(final String template, final String subject) {
        super(template, subject);
    }

    @Override
    public MandrillMergeLanguage getMandrillMergeLanguage() {
        return MandrillMergeLanguage.handlebars;
    }

}