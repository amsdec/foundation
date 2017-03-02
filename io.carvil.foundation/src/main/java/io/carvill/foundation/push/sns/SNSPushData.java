package io.carvill.foundation.push.sns;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class SNSPushData implements Serializable {

    private static final long serialVersionUID = -4902085924121325191L;

    @JsonProperty("aps")
    private final Map<String, Object> parameters;

    public SNSPushData(final String alert) {
        this(alert, null);
    }

    public SNSPushData(final String alert, final Map<String, Object> parameters) {
        this.parameters = new HashMap<>();
        if (StringUtils.isBlank(alert)) {
            this.putData("content-available", true);
            this.putData("priority", "10");
            this.putData("badge", "1");
        } else {
            this.putData("sound", "default");
            this.putData("alert", alert);
        }
        this.putData(parameters);
    }

    public void putData(final Map<String, Object> parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            this.parameters.putAll(parameters);
        }
    }

    public void putData(final String name, final Object value) {
        if (value != null) {
            this.parameters.put(name, value);
        }
    }

    public Map<String, Object> getParameters() {
        return this.parameters;
    }

}
