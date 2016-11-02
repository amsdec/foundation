package io.carvill.foundation.email;

import io.carvill.foundation.email.mandrill.MandrillRejectReason;
import io.carvill.foundation.email.mandrill.MandrillResponseStatus;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public interface FailedCallback {

    void report(String email, MandrillResponseStatus responseStatus, MandrillRejectReason rejectReason);

}
