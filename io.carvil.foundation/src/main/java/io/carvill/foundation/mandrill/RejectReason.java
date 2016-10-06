package io.carvill.foundation.mandrill;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public enum RejectReason {

    rejected,

    hard_bounce,

    soft_bounce,

    spam,

    unsub,

    custom,

    invalid_sender,

    invalid,

    test_mode_limit,

    rule,

    unsigned;

    @JsonCreator
    public static RejectReason forValue(final String value) {
        if (value == null) {
            return null;
        }
        return valueOf(StringUtils.replaceChars(value, '-', '_'));
    }

    @JsonValue
    public String toValue() {
        return this.name().replace('_', '-');
    }

}
