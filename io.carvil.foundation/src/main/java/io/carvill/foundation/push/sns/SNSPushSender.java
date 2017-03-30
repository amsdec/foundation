package io.carvill.foundation.push.sns;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.EndpointDisabledException;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SetEndpointAttributesRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.carvill.foundation.push.Device;
import io.carvill.foundation.push.OS;
import io.carvill.foundation.push.PushSender;
import io.carvill.foundation.push.exception.PushException;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class SNSPushSender implements PushSender {

    private final static Logger log = LoggerFactory.getLogger(SNSPushSender.class);

    private String key;

    private String secret;

    private String applicationARN;

    private String region;

    protected ObjectMapper objectMapper;

    private AmazonSNS amazonSNS;

    protected SNSPushSender() {
        this.objectMapper = new ObjectMapper();
    }

    public SNSPushSender(final String key, final String secret, final String applicationARN, final String region) {
        this(key, secret, applicationARN, region, new ObjectMapper());
    }

    public SNSPushSender(final String key, final String secret, final String applicationARN, final String region,
            final ObjectMapper objectMapper) {
        this.key = key;
        this.secret = secret;
        this.applicationARN = applicationARN;
        this.objectMapper = objectMapper;

        this.amazonSNS = new AmazonSNSClient(new BasicAWSCredentials(this.key, this.secret));
        if (StringUtils.isNotBlank(region)) {
            try {
                this.amazonSNS.setRegion(Region.getRegion(Regions.fromName(region)));
                this.region = region;
            } catch (final RuntimeException e) {
                log.warn("Region parameter '{}' is invalid: {}", this.region, e.getMessage());
            }
        }
    }

    @Override
    public String sendPush(final Device device, final String alert, final Map<String, Object> parameters,
            final boolean isSandbox) throws PushException {
        if (StringUtils.isBlank(device.getExtra())) {
            log.warn("Device data object does not containt endpoint ARN into 'extra' field");
        } else {
            final SNSPushPlatform platform;
            final SNSPushData data;
            if (OS.IOS.equals(device.getDevice())) {
                platform = isSandbox ? SNSPushPlatform.APNS_SANDBOX : SNSPushPlatform.APNS;
                data = new SNSPushDataAPNS(alert, parameters);
            } else {
                platform = SNSPushPlatform.GCM;
                data = new SNSPushDataAndroid(alert, parameters);
            }

            final PublishRequest request = new PublishRequest();
            try {
                final Map<String, Object> message = new HashMap<>();
                message.put("default", "");
                message.put(platform.name(), this.objectMapper.writeValueAsString(data));

                final String json = this.objectMapper.writeValueAsString(message);
                log.info("Sending push with JSON: {}", json);

                request.setTargetArn(device.getExtra());
                request.setMessageStructure("json");
                request.setMessage(json);

                return this.sendPush(request);
            } catch (final EndpointDisabledException e) {
                try {
                    log.info("Endpoint disabled, try enable and retry send push...");
                    this.enableDevice(device);
                    return this.sendPush(request);
                } catch (final RuntimeException re) {
                    throw new PushException("AWS SNS throws an error on retry sending push: %s ", re, re.getMessage());
                }
            } catch (final JsonProcessingException e) {
                throw new PushException("JSON to push can not be generated: %s", e, e.getMessage());
            } catch (final RuntimeException e) {
                throw new PushException("AWS SNS throws an error sending push: %s", e, e.getMessage());
            }
        }
        return null;
    }

    protected String sendPush(final PublishRequest request) {
        final PublishResult result = this.amazonSNS.publish(request);
        log.info("Push was processed with AWS SNS #{}", result.getMessageId());
        return result.getMessageId();
    }

    @Override
    public String registerDevice(final Device device) throws PushException {
        final CreatePlatformEndpointRequest platformEndpointRequest = new CreatePlatformEndpointRequest();
        platformEndpointRequest.setCustomUserData(device.getCustomerUserData());
        platformEndpointRequest.setToken(device.getToken());
        platformEndpointRequest.setPlatformApplicationArn(this.applicationARN);

        log.debug("Registering {}  device at Amazon SNS", device.getDevice());
        try {
            final CreatePlatformEndpointResult response = this.amazonSNS
                    .createPlatformEndpoint(platformEndpointRequest);

            return response.getEndpointArn();
        } catch (final InvalidParameterException e) {
            throw new PushException("Invalid token: %s", e, e.getMessage());
        } catch (final RuntimeException e) {
            throw new PushException("Device %s was not registered into Amazon SNS: %s", e, device.getDevice(),
                    e.getMessage());
        }
    }

    @Override
    public void enableDevice(final Device device) throws PushException {
        final SetEndpointAttributesRequest endpointAttributesRequest = new SetEndpointAttributesRequest();
        endpointAttributesRequest.setEndpointArn(device.getExtra());
        endpointAttributesRequest.addAttributesEntry("Enabled", "true");

        try {
            this.amazonSNS.setEndpointAttributes(endpointAttributesRequest);
            log.debug("Endpoint was enabled");
        } catch (final RuntimeException e) {
            throw new PushException("Device %s was not enabled into Amazon SNS: %s", e, device.getDevice(),
                    e.getMessage());
        }
    }

    public String getKey() {
        return this.key;
    }

    public String getSecret() {
        return this.secret;
    }

    public String getApplicationARN() {
        return this.applicationARN;
    }

    public String getRegion() {
        return this.region;
    }

}
