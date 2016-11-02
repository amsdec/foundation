package io.carvill.foundation.email.sparkpost;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.carvill.foundation.email.Email;
import io.carvill.foundation.email.EmailTest;
import io.carvill.foundation.email.Recipient;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class SparkpostFakeTest extends EmailTest {

    private Sparkpost sparkpost;

    public SparkpostFakeTest() throws UnsupportedEncodingException {
        this.sparkpost = new SparkpostFake("team@yourdomain.com", "from name", "[your_api_key]", new ObjectMapper());
        // this.sparkpost = new Sparkpost("team@yourdomain.com", "from name", "[your_api_key]");
        this.sparkpost.setReplyTo("no-reply@yourdomain.com");
    }

    @Test
    public void test() throws IOException {
        this.send();
    }

    @Override
    public Email<Recipient> build() {
        return this.sparkpost.buildEmail("template-id", "subject");
    }

    @Override
    public void send(final Email<Recipient> email) {
        this.sparkpost.send(email);
    }
}
