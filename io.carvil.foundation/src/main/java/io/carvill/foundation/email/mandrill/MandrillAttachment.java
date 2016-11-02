package io.carvill.foundation.email.mandrill;

import io.carvill.foundation.email.Attachment;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class MandrillAttachment extends Attachment {

    private static final long serialVersionUID = -4984231283029180190L;

    public MandrillAttachment(final String type, final String name, final byte[] content) {
        super(type, name, content);
    }

    public MandrillAttachment(final String type, final String name, final String content) {
        super(type, name, content);
    }

}
