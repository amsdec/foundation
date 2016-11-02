package io.carvill.foundation.email;

import java.util.Map;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public interface VariableProvider<T extends Recipient> {

    Map<String, Object> getVariables(final T recipient);

}
