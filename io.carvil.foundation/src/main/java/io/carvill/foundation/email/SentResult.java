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
        return this.success;
    }

    public void setSuccess(final int success) {
        this.success = success;
    }

    public int getFailed() {
        return this.failed;
    }

    public void setFailed(final int failed) {
        this.failed = failed;
    }

    public Map<String, String> getErrors() {
        return this.errors;
    }

    public void setErrors(final Map<String, String> errors) {
        this.errors = errors;
    }

    public boolean isSuccessfullSingle() {
        return this.isSuccessfull(1);
    }

    public boolean isSuccessfull(final int number) {
        return this.success == number;
    }

    @Override
    public String toString() {
        return String.format("Email stats [success=%s, failed=%s, errors=%s]", this.success, this.failed, this.errors);
    }

}
