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

    private Mandrill mandril = new MandrillFake("[your_key_here]", new ObjectMapper());

    // private Mandrill mandril = new Mandrill("[your_key_here]");

    @Test
    public void test() throws IOException {
        this.send();
    }

    @Override
    public Email<Recipient> build() {
        return this.mandril.buildEmail("from name", "no-reply@yourdomain.com", "template-id", "subject");
    }

    @Override
    public void send(final Email<Recipient> email) {
        this.mandril.send(email);
    }
}
