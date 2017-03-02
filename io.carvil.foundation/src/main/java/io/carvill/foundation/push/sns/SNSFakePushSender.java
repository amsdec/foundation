package io.carvill.foundation.push.sns;

import java.util.UUID;

import com.amazonaws.services.sns.model.PublishRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.carvill.foundation.push.Device;
import io.carvill.foundation.push.exception.PushException;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class SNSFakePushSender extends SNSPushSender {

    private boolean provideRequest = true;

    @Override
    protected String sendPush(final PublishRequest request) {
        if (this.provideRequest) {
            try {
                return this.objectMapper.writeValueAsString(request);
            } catch (final JsonProcessingException e) {
                return request.toString();
            }
        }
        return UUID.randomUUID().toString();
    }

    @Override
    public String registerDevice(final Device device) throws PushException {
        return UUID.randomUUID().toString();
    }

    @Override
    public void enableDevice(final Device device) throws PushException {

    }

    public void setProvideRequest(final boolean provideRequest) {
        this.provideRequest = provideRequest;
    }

}
