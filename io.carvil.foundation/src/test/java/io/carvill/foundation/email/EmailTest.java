package io.carvill.foundation.email;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import io.carvill.foundation.email.Email;
import io.carvill.foundation.email.Recipient;
import io.carvill.foundation.email.VariableProvider;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public abstract class EmailTest {

    public void send() throws IOException {
        final Recipient recipient = new Recipient() {

            @Override
            public String getName() {
                return "Carlos";
            }

            @Override
            public String getEmail() {
                return "carlos.carpio07@noexisteestedominio.com";
            }
        };
        final Recipient recipient2 = new Recipient() {

            @Override
            public String getName() {
                return "Beto";
            }

            @Override
            public String getEmail() {
                return "carlos.carpio07+beto@gmail.com";
            }
        };

        try (InputStream attachment = this.getClass().getClassLoader().getResourceAsStream("readme.txt")) {
            final Email<Recipient> email = this.build().withRecipients(Arrays.asList(recipient, recipient2))
                    .withVariableProvider(new VariableProvider<Recipient>() {

                        @Override
                        public Map<String, Object> getVariables(final Recipient recipient) {
                            final Map<String, Object> var = new HashMap<>();
                            var.put("USER_NAME", recipient.getName());
                            var.put("MAIN_URL", "https://someurl.mx/login");
                            return var;
                        }

                    }).addAttachment("text/plain", "readme.txt", IOUtils.toByteArray(attachment));
            this.send(email);
        }
    }

    public abstract Email<Recipient> build();

    public abstract void send(final Email<Recipient> email);

}
