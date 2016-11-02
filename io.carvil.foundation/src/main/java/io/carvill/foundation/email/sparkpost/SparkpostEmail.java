package io.carvill.foundation.email.sparkpost;

import io.carvill.foundation.email.Email;
import io.carvill.foundation.email.Recipient;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class SparkpostEmail<T extends Recipient> extends Email<T> {

    public SparkpostEmail(final String template, final String subject) {
        super(template, subject);
    }

    @Override
    public Email<T> addAttachment(final String type, final String name, final String content) {
        return this.addAttachment(new SparkpostAttachment(type, name, content));
    }

    @Override
    public Email<T> addAttachment(final String type, final String name, final byte[] content) {
        return this.addAttachment(new SparkpostAttachment(type, name, content));
    }

}
