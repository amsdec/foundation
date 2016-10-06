package io.carvill.foundation.mandrill;

import java.io.Serializable;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class Variable implements Serializable {

    private static final long serialVersionUID = 662152663728538654L;

    private String name;

    private Object content;

    public Variable(final String name, final Object content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Object getContent() {
        return this.content;
    }

    public void setContent(final Object content) {
        this.content = content;
    }

}
