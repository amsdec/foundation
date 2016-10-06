package io.carvill.foundation.mandrill;

import java.util.Map;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public interface VariableProvider {

    Map<String, Object> getVariables(final Recipient recipient);

}
