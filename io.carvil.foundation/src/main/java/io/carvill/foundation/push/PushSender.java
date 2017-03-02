package io.carvill.foundation.push;

import java.util.Map;

import io.carvill.foundation.push.exception.PushException;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public interface PushSender {

    String sendPush(final Device device, final String alert, final Map<String, Object> parameters,
            final boolean isSandbox) throws PushException;

    String registerDevice(final Device device) throws PushException;

    void enableDevice(final Device device) throws PushException;

}
