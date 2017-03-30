package io.carvill.foundation.push.sns;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public abstract class SNSPushData implements Serializable {

    private static final long serialVersionUID = -1505349149455790313L;

    private final Map<String, Object> parameters;

    public SNSPushData() {
        this.parameters = new HashMap<>();
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
