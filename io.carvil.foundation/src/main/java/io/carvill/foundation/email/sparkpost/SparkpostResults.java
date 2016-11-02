package io.carvill.foundation.email.sparkpost;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SparkpostResults implements Serializable {

    private static final long serialVersionUID = 8972868568203205421L;

    @JsonProperty("rcpt_to_errors")
    private List<SparkpostError> errors;

    @JsonProperty("total_rejected_recipients")
    private int rejected;

    @JsonProperty("total_accepted_recipients")
    private int accepted;

    private String id;

    public List<SparkpostError> getErrors() {
        return errors;
    }

    public void setErrors(final List<SparkpostError> errors) {
        this.errors = errors;
    }

    public int getRejected() {
        return rejected;
    }

    public void setRejected(final int rejected) {
        this.rejected = rejected;
    }

    public int getAccepted() {
        return accepted;
    }

    public void setAccepted(final int accepted) {
        this.accepted = accepted;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SparkpostResults [errors=" + this.errors + ", rejected=" + this.rejected + ", accepted=" + this.accepted
                + ", id=" + this.id + "]";
    }

}
