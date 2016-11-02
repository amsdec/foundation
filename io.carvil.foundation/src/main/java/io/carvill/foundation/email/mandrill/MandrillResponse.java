package io.carvill.foundation.email.mandrill;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class MandrillResponse {

    private String email;

    private MandrillResponseStatus responseStatus;

    private MandrillRejectReason rejectReason;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public MandrillResponseStatus getResponseStatus() {
        return this.responseStatus;
    }

    public void setResponseStatus(final MandrillResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public MandrillRejectReason getRejectReason() {
        return this.rejectReason;
    }

    public void setRejectReason(final MandrillRejectReason rejectReason) {
        this.rejectReason = rejectReason;
    }

}
