package io.carvill.foundation.email.mandrill;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.carvill.foundation.email.Email;
import io.carvill.foundation.email.EmailTest;
import io.carvill.foundation.email.Recipient;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class MandrillFakeTest extends EmailTest {

    private Mandrill mandril;

    public MandrillFakeTest() {
        this.mandril = new MandrillFake("team@yourdomain.com", "from name", "[your_key_here]", new ObjectMapper());
        // mandril = new Mandrill("team@yourdomain.com", "from name", "[your_key_here]");
        this.mandril.setReplyTo("no-reply@yourdomain.com");
    }

    @Test
    public void test() throws IOException {
        this.send();
    }

    @Override
    public Email<Recipient> build() {
        return this.mandril.buildEmail("template-id", "subject");
    }

    @Override
    public void send(final Email<Recipient> email) {
        this.mandril.send(email);
    }
}
