package io.carvill.foundation.mandrill;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class MergeVariables implements Serializable {

    private static final long serialVersionUID = 3644354607905219950L;

    @JsonProperty("rcpt")
    private final String recipient;

    @JsonProperty("vars")
    private List<Variable> variables;

    public MergeVariables(final String recipient) {
        this.recipient = recipient;
    }

    public MergeVariables withVariables(final Map<String, Object> variables) {
        if (variables != null) {
            for (final String name : variables.keySet()) {
                this.withVariable(name, variables.get(name));
            }
        }
        return this;
    }

    public MergeVariables withVariable(final String name, final Object content) {
        if (this.variables == null) {
            this.variables = new ArrayList<>();
        }
        this.variables.add(new Variable(name, content));
        return this;
    }

}
