package io.carvill.foundation.email.sparkpost;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.carvill.foundation.email.To;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SparkpostRecipient implements Serializable {

    private static final long serialVersionUID = -684603955733791414L;

    private To address;

    private Map<String, Object> substitutionData;

    public To getAddress() {
        return address;
    }

    public void setAddress(final To address) {
        this.address = address;
    }

    public Map<String, Object> getSubstitutionData() {
        return substitutionData;
    }

    public void setSubstitutionData(final Map<String, Object> substitutionData) {
        this.substitutionData = substitutionData;
    }

}
