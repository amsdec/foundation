package io.carvill.foundation.email.sparkpost;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SparkpostResponse implements Serializable {

    private static final long serialVersionUID = 8183233004115747753L;

    private List<SparkpostError> errors;

    private SparkpostResults results;

    public List<SparkpostError> getErrors() {
        return errors;
    }

    public void setErrors(final List<SparkpostError> errors) {
        this.errors = errors;
    }

    public SparkpostResults getResults() {
        return results;
    }

    public void setResults(final SparkpostResults results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "SparkpostResponse [errors=" + this.errors + ", results=" + this.results + "]";
    }

}
