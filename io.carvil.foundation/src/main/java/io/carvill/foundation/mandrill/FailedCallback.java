package io.carvill.foundation.mandrill;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public interface FailedCallback {

    void report(String email, ResponseStatus responseStatus, RejectReason rejectReason);

}
