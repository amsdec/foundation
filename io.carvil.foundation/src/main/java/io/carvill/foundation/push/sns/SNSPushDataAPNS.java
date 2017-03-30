package io.carvill.foundation.push.sns;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class SNSPushDataAPNS extends SNSPushData {

    private static final long serialVersionUID = -4902085924121325191L;

    public SNSPushDataAPNS(final String alert) {
        this(alert, null);
    }

    public SNSPushDataAPNS(final String alert, final Map<String, Object> parameters) {
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

    @Override
    @JsonProperty("aps")
    public Map<String, Object> getParameters() {
        return super.getParameters();
    }

}
