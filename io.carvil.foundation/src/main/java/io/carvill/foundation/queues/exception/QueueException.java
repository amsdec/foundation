package io.carvill.foundation.queues.exception;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class QueueException extends Exception {

    private static final long serialVersionUID = -4874412890736779720L;

    public QueueException(final String message, final Throwable cause, final Object... args) {
        super(ArrayUtils.isEmpty(args) ? message : String.format(message, args), cause);
    }

    public QueueException(final String message, final Object... args) {
        super(ArrayUtils.isEmpty(args) ? message : String.format(message, args));
    }

}
