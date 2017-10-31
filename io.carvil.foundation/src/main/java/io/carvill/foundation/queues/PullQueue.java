package io.carvill.foundation.queues;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

import io.carvill.foundation.queues.exception.QueueException;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public interface PullQueue {

    <T extends Serializable> String add(String queueUrl, T body, int delay) throws QueueException;

    <T extends Serializable> List<QueueMessage<T>> pull(String queueUrl, Class<T> type, int limit)
            throws QueueException;

    default <T extends Serializable> List<QueueMessage<T>> pull(final String queueUrl, final Class<T> type)
            throws QueueException {
        return this.pull(queueUrl, type, 0);
    }

    void remove(String queueUrl, String deletionId) throws QueueException;

    default boolean removeQuietly(final String queueUrl, final String messageId, final Consumer<Exception> reporter) {
        try {
            this.remove(queueUrl, messageId);
            return true;
        } catch (final QueueException e) {
            if (reporter != null) {
                reporter.accept(e);
            }
        }
        return false;
    }

}
