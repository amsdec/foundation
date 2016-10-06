package io.carvill.foundation.data.exception;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class ExpectedResourceException extends RuntimeException {

    private static final long serialVersionUID = 7700362717021263554L;

    public ExpectedResourceException(final String message, final Throwable cause, final Object... args) {
        super(ArrayUtils.isEmpty(args) ? message : String.format(message, args), cause);
    }

    public ExpectedResourceException(final String message, final Object... args) {
        super(ArrayUtils.isEmpty(args) ? message : String.format(message, args));
    }

}
