package io.carvill.foundation.push.exception;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class PushException extends Exception {

    private static final long serialVersionUID = -1418991595314193796L;

    public PushException(final String message, final Throwable cause, final Object... args) {
        super(ArrayUtils.isEmpty(args) ? message : String.format(message, args), cause);
    }

    public PushException(final String message, final Object... args) {
        super(ArrayUtils.isEmpty(args) ? message : String.format(message, args));
    }

}
