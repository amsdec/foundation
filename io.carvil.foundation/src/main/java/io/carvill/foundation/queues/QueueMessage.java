package io.carvill.foundation.queues;

import java.io.Serializable;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class QueueMessage<T extends Serializable> {

    private String id;

    private String deletionId;

    private T message;

    public QueueMessage() {
    }

    public QueueMessage(final String id, final T message) {
        this.id = id;
        this.message = message;
    }

    public QueueMessage(final String id, final String deletionId, final T message) {
        this.id = id;
        this.deletionId = deletionId;
        this.message = message;
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getDeletionId() {
        return this.deletionId;
    }

    public void setDeletionId(final String deletionId) {
        this.deletionId = deletionId;
    }

    public T getMessage() {
        return this.message;
    }

    public void setMessage(final T message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("QueueMessage [id=%s, deletionId=%s, message=%s]", this.id, this.deletionId, this.message);
    }

}
