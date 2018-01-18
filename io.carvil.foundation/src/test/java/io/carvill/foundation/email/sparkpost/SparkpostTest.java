package io.carvill.foundation.email.sparkpost;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.carvill.foundation.email.Recipient;
import io.carvill.foundation.email.SentResult;

/**
 * @author alberto, carlos.carpio07@gmail.com
 */
public class SparkpostTest {

    @Test
    public void testReplyToInEmailOnly() throws Exception {
        SparkpostMock sparkpost = new SparkpostMock("junit@calvill.io", "Unit Test", "api_key");
        SparkpostEmail<Recipient> email = new SparkpostEmail("junit-template", "my-subject");
        email.withReplyTo("reply-to@carvill.io");
        SentResult send = sparkpost.send(email);
        Assert.assertNotNull(send);
        Assert.assertFalse(sparkpost.request.getSubstitutionData().isEmpty());
        Assert.assertEquals("reply-to@carvill.io", sparkpost.request.getSubstitutionData().get("reply_to"));
        Assert.assertEquals("reply-to@carvill.io",
                ((Map<String, Object>) sparkpost.request.getContent().get("headers")).get("reply_to"));
    }

    @Test
    public void testReplyToInInstanceOnly() throws Exception {
        SparkpostMock sparkpost = new SparkpostMock("junit@calvill.io", "Unit Test", "api_key");
        SparkpostEmail<Recipient> email = new SparkpostEmail("junit-template", "my-subject");
        sparkpost.setReplyTo("reply-to@carvill.io");
        SentResult send = sparkpost.send(email);
        Assert.assertNotNull(send);
        Assert.assertFalse(sparkpost.request.getSubstitutionData().isEmpty());
        Assert.assertEquals("reply-to@carvill.io", sparkpost.request.getSubstitutionData().get("reply_to"));
        Assert.assertEquals("reply-to@carvill.io",
                ((Map<String, Object>) sparkpost.request.getContent().get("headers")).get("reply_to"));
    }

    @Test
    public void testReplyToInInstanceAndEmail() throws Exception {
        SparkpostMock sparkpost = new SparkpostMock("junit@calvill.io", "Unit Test", "api_key");
        SparkpostEmail<Recipient> email = new SparkpostEmail("junit-template", "my-subject");
        email.withReplyTo("reply-to@carvill.io");
        sparkpost.setReplyTo("instance@carvill.io");
        SentResult send = sparkpost.send(email);
        Assert.assertNotNull(send);
        Assert.assertFalse(sparkpost.request.getSubstitutionData().isEmpty());
        Assert.assertEquals("reply-to@carvill.io", sparkpost.request.getSubstitutionData().get("reply_to"));
        Assert.assertEquals("reply-to@carvill.io",
                ((Map<String, Object>) sparkpost.request.getContent().get("headers")).get("reply_to"));
    }

    class SparkpostMock extends Sparkpost {

        private SparkpostTemplateMessage request;

        public SparkpostMock(String fromEmail, String fromName, String apiKey) throws UnsupportedEncodingException {
            super(fromEmail, fromName, apiKey);
        }

        protected SentResult send(final SparkpostTemplateMessage request) {
            this.request = request;
            return new SentResult();
        }

    }

    class RecipientMock implements Recipient {

        @Override
        public String getEmail() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

    }

}
