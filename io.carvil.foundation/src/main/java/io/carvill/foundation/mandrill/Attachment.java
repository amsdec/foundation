package io.carvill.foundation.mandrill;

import java.io.Serializable;

import org.apache.commons.codec.binary.Base64;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class Attachment implements Serializable {

    private static final long serialVersionUID = -2058150331677030664L;

    private String type;

    private String name;

    private String content;

    public Attachment() {
    }

    /**
     * @param type mime type
     * @param name attachment name
     * @param content content coded as base 64 string
     */
    public Attachment(final String type, final String name, final String content) {
        this.type = type;
        this.name = name;
        this.content = content;
    }

    /**
     * @param type mime type
     * @param name attachment name
     * @param content binary content
     */
    public Attachment(final String type, final String name, final byte[] content) {
        this(type, name, Base64.encodeBase64String(content));
    }

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

}
