package io.carvill.foundation.push;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public interface Device {

    String getToken();

    String getExtra();

    OS getDevice();

    default String getCustomerUserData() {
        return "SNS push notification";
    }

}
