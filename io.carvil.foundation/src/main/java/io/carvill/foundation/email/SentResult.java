package io.carvill.foundation.email;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class SentResult {

    private int success;

    private int failed;

    private Map<String, String> errors;

    public SentResult() {
        this.errors = new HashMap<>();
    }

    public void incrementSuccess() {
        this.success++;
    }

    public void reportError(final String email, final String error) {
        this.errors.put(email, error);
        this.failed++;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(final int success) {
        this.success = success;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(final int failed) {
        this.failed = failed;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(final Map<String, String> errors) {
        this.errors = errors;
    }

}
