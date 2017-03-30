package io.carvill.foundation.push.sns;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class SNSPushDataAndroid extends SNSPushData {

    private static final long serialVersionUID = 8929634219781222098L;

    @JsonProperty("data")
    private Map<String, Object> data;

    public SNSPushDataAndroid(final String alert) {
        this(alert, null);
    }

    public SNSPushDataAndroid(final String alert, final Map<String, Object> parameters) {
        if (StringUtils.isNotBlank(alert)) {
            this.putData("title", alert);
        }

        if (MapUtils.isNotEmpty(parameters)) {
            this.data = parameters;
        }
    }

    @Override
    @JsonProperty("notification")
    public Map<String, Object> getParameters() {
        return super.getParameters();
    }

}
