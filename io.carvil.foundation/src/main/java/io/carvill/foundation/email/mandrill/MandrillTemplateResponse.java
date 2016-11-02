package io.carvill.foundation.email.mandrill;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class MandrillTemplateResponse implements Serializable {

    private static final long serialVersionUID = -8307385983070851158L;

    private String email;

    private MandrillResponseStatus status;

    @JsonProperty("reject_reason")
    private MandrillRejectReason rejectReason;

    @JsonProperty("_id")
    private String id;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public MandrillResponseStatus getStatus() {
        return this.status;
    }

    public void setStatus(final MandrillResponseStatus status) {
        this.status = status;
    }

    public MandrillRejectReason getRejectReason() {
        return this.rejectReason;
    }

    public void setRejectReason(final MandrillRejectReason rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

}
