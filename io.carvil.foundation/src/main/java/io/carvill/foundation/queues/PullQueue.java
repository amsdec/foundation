package io.carvill.foundation.queues;

import java.io.Serializable;
import java.util.List;

import io.carvill.foundation.queues.exception.QueueException;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public interface PullQueue {

    <T extends Serializable> String add(String queueUrl, T body, int delay) throws QueueException;

    <T extends Serializable> List<T> pull(String queueUrl, int maxNumberOfMessages, Class<T> type)
            throws QueueException;

}
