package io.carvill.foundation.queues.sqs;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.SdkBaseException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.InvalidMessageContentsException;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.carvill.foundation.push.sns.SNSPushSender;
import io.carvill.foundation.queues.PullQueue;
import io.carvill.foundation.queues.exception.QueueException;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class SQSPullQueue implements PullQueue {

    private final static Logger log = LoggerFactory.getLogger(SNSPushSender.class);

    private String key;

    private String secret;

    private String region;

    protected ObjectMapper objectMapper;

    private Integer visibilityTimeout;

    private AmazonSQS amazonSQS;

    protected SQSPullQueue() {
        this.objectMapper = new ObjectMapper();
    }

    public SQSPullQueue(final String key, final String secret, final String region) {
        this(key, secret, region, new ObjectMapper());
    }

    public SQSPullQueue(final String key, final String secret, final String region, final ObjectMapper objectMapper) {
        this.key = key;
        this.secret = secret;
        this.objectMapper = objectMapper;

        final AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(this.key, this.secret));
        final AmazonSQSClientBuilder builder = AmazonSQSClientBuilder.standard().withCredentials(credentialsProvider);
        if (StringUtils.isNotBlank(region)) {
            try {
                this.region = region;
                builder.withRegion(this.region);
            } catch (final RuntimeException e) {
                log.warn("Region parameter '{}' is invalid: {}", this.region, e.getMessage());
            }
        }
        this.amazonSQS = builder.build();
    }

    @Override
    public <T extends Serializable> String add(final String queueUrl, final T body, final int delay)
            throws QueueException {
        final String message;
        try {
            message = this.objectMapper.writeValueAsString(body);
        } catch (final JsonProcessingException e) {
            throw new QueueException("Unable to serialize your message", e);
        }

        final SendMessageRequest request = new SendMessageRequest();
        request.setQueueUrl(queueUrl);
        request.setMessageBody(message);
        request.setDelaySeconds(delay);

        try {
            final SendMessageResult response = this.amazonSQS.sendMessage(request);
            log.debug("Message {} was queued into {}", response.getMessageId(), queueUrl);
            return response.getMessageId();
        } catch (final InvalidMessageContentsException e) {
            throw new QueueException("Message content was reported as invalid", e);
        } catch (final SdkBaseException e) {
            throw new QueueException("General exception happened in queue '%s'", e, queueUrl);
        }
    }

    @Override
    public <T extends Serializable> List<T> pull(final String queueUrl, final int maxNumberOfMessages,
            final Class<T> type) throws QueueException {
        final ReceiveMessageRequest request = new ReceiveMessageRequest();
        request.setQueueUrl(queueUrl);
        request.setMaxNumberOfMessages(maxNumberOfMessages);
        request.setVisibilityTimeout(this.visibilityTimeout);

        try {
            final ReceiveMessageResult response = this.amazonSQS.receiveMessage(request);
            final List<Message> messages = response.getMessages();
            log.debug("{} of {} message were found in queued {}",
                    new Object[] { messages.size(), maxNumberOfMessages, queueUrl });

            final List<T> result = new ArrayList<>(messages.size());
            for (final Message message : messages) {
                final String body = message.getBody();
                result.add(this.objectMapper.readValue(body, type));
            }
            return result;
        } catch (final IOException e) {
            throw new QueueException("Unable to deserialize the content as {} type", e, type.getSimpleName());
        } catch (final InvalidMessageContentsException e) {
            throw new QueueException("Message content was reported as invalid", e);
        } catch (final SdkBaseException e) {
            throw new QueueException("General exception happened in queue '%s'", e, queueUrl);
        }
    }

    public Integer getVisibilityTimeout() {
        return this.visibilityTimeout;
    }

    public void setVisibilityTimeout(final Integer visibilityTimeout) {
        this.visibilityTimeout = visibilityTimeout;
    }

}