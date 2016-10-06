package io.carvill.foundation.mandrill;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class TemplateResponse implements Serializable {

    private static final long serialVersionUID = -8307385983070851158L;

    private String email;

    private ResponseStatus status;

    @JsonProperty("reject_reason")
    private RejectReason rejectReason;

    @JsonProperty("_id")
    private String id;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public ResponseStatus getStatus() {
        return this.status;
    }

    public void setStatus(final ResponseStatus status) {
        this.status = status;
    }

    public RejectReason getRejectReason() {
        return this.rejectReason;
    }

    public void setRejectReason(final RejectReason rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

}
