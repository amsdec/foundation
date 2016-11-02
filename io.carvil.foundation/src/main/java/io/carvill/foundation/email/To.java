package io.carvill.foundation.email;

import java.io.Serializable;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class To implements Serializable {

    private static final long serialVersionUID = -8976054935536921406L;

    private String email;

    private String name;

    public To() {
    }

    public To(final String email, final String name) {
        this.email = email;
        this.name = name;
    }

    public To(final Recipient recipient) {
        this(recipient.getEmail(), recipient.getName());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

}
