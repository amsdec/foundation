package io.carvill.foundation.mandrill;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class Response {

    private String email;

    private ResponseStatus responseStatus;

    private RejectReason rejectReason;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public ResponseStatus getResponseStatus() {
        return this.responseStatus;
    }

    public void setResponseStatus(final ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public RejectReason getRejectReason() {
        return this.rejectReason;
    }

    public void setRejectReason(final RejectReason rejectReason) {
        this.rejectReason = rejectReason;
    }

}
