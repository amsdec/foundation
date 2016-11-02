package io.carvill.foundation.email.sparkpost;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.carvill.foundation.email.Attachment;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class SparkpostAttachment extends Attachment {

    private static final long serialVersionUID = -9071841402617919011L;

    public SparkpostAttachment(final String type, final String name, final byte[] content) {
        super(type, name, content);
    }

    public SparkpostAttachment(final String type, final String name, final String content) {
        super(type, name, content);
    }

    @JsonProperty("data")
    @Override
    public String getContent() {
        return super.getContent();
    }

}
