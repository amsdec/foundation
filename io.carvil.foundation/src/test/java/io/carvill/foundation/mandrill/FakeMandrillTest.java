package io.carvill.foundation.mandrill;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class FakeMandrillTest {

    private Mandrill mandril = new FakeMandrill("b5bJ9OZjxGF7x4r_xt6o6g", new ObjectMapper());

    // private Mandrill mandril = new Mandrill("b5bJ9OZjxGF7x4r_xt6o6g");

    @Test
    public void test() throws IOException {
        final Recipient recipient = new Recipient() {

            @Override
            public String getName() {
                return "Carlos Carpio";
            }

            @Override
            public String getEmail() {
                return "carlos.carpio07@gmail.com";
            }
        };
        final Recipient recipient2 = new Recipient() {

            @Override
            public String getName() {
                return "Hilda Villegas";
            }

            @Override
            public String getEmail() {
                return "hvillegasv@gmail.com";
            }
        };

        final TemplateMessage message = new TemplateMessage("cok-signup", "Establece tu contraseña",
                "no-reply@cuentasok.com", "Equipo CuentasOK").withHeader("Reply-To", "carlos.carpio@tcpip.tech")
                        .addRecipient(recipient).addRecipient(recipient2).withVariables(new VariableProvider() {

                            @Override
                            public Map<String, Object> getVariables(final Recipient recipient) {
                                final Map<String, Object> var = new HashMap<>();
                                var.put("USER_NAME", recipient.getName());
                                var.put("MAIN_URL", "https://colonos.mx/login");
                                return var;
                            }

                        }).addAttachment(new Attachment("application/json", "cok-ds-example.json", IOUtils.toByteArray(
                                new FileInputStream("/Users/carpio/Desktop/cok-account-statmente-example-3.json"))));
        this.mandril.sendTemplate(message);
    }

}
