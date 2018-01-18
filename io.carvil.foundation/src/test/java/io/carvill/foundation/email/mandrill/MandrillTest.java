package io.carvill.foundation.email.mandrill;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.carvill.foundation.email.Recipient;
import io.carvill.foundation.email.SentResult;
import io.carvill.foundation.email.VariableProvider;

/**
 * @author alberto, carlos.carpio07@gmail.com
 */
public class MandrillTest {

    @Test
    public void testReplyToInEmailOnly() throws Exception {
        MandrillMock mandrill = new MandrillMock("junit@calvill.io", "Unit Test", "api_key");
        MandrillEmail<Recipient> email = new MandrillEmail("junit-template", "my-subject");
        email.withRecipient(new RecipientMock());
        email.withReplyTo("reply-to@carvill.io");
        email.withVariableProvider(new VariableProvider<Recipient>() {

            @Override
            public Map<String, Object> getVariables(Recipient recipient) {
                return new HashMap();
            }
        });
        SentResult send = mandrill.send(email);
        Assert.assertNotNull(send);
        Assert.assertFalse(mandrill.request.getMessage().getHeaders().isEmpty());
        Assert.assertEquals("reply-to@carvill.io", mandrill.request.getMessage().getHeaders().get("Reply-To"));
    }

    @Test
    public void testReplyToInInstanceOnly() throws Exception {
        MandrillMock mandrill = new MandrillMock("junit@calvill.io", "Unit Test", "api_key");
        MandrillEmail<Recipient> email = new MandrillEmail("junit-template", "my-subject");
        email.withRecipient(new RecipientMock());
        email.withVariableProvider(new VariableProvider<Recipient>() {

            @Override
            public Map<String, Object> getVariables(Recipient recipient) {
                return new HashMap();
            }
        });

        mandrill.setReplyTo("reply-to@carvill.io");
        SentResult send = mandrill.send(email);
        Assert.assertNotNull(send);
        Assert.assertFalse(mandrill.request.getMessage().getHeaders().isEmpty());
        Assert.assertEquals("reply-to@carvill.io", mandrill.request.getMessage().getHeaders().get("Reply-To"));
    }

    @Test
    public void testReplyToInInstanceAndEmail() throws Exception {
        MandrillMock mandrill = new MandrillMock("junit@calvill.io", "Unit Test", "api_key");
        mandrill.setReplyTo("instance@carvill.io");
        MandrillEmail<Recipient> email = new MandrillEmail("junit-template", "my-subject");
        email.withRecipient(new RecipientMock());
        email.withReplyTo("reply-to@carvill.io");
        email.withVariableProvider(new VariableProvider<Recipient>() {

            @Override
            public Map<String, Object> getVariables(Recipient recipient) {
                return new HashMap();
            }
        });
        SentResult send = mandrill.send(email);
        Assert.assertNotNull(send);
        Assert.assertFalse(mandrill.request.getMessage().getHeaders().isEmpty());
        Assert.assertEquals("reply-to@carvill.io", mandrill.request.getMessage().getHeaders().get("Reply-To"));
    }

    class MandrillMock extends Mandrill {

        private MandrillTemplateRequest request;

        public MandrillMock(String fromEmail, String fromName, String apiKey) throws UnsupportedEncodingException {
            super(fromEmail, fromName, apiKey);
        }

        protected SentResult send(final MandrillTemplateRequest request) {
            this.request = request;
            return new SentResult();
        }

    }

    class RecipientMock implements Recipient {

        @Override
        public String getEmail() {
            return "recipient-junir@carvill.io";
        }

        @Override
        public String getName() {
            return "Recipient Junit";
        }

    }

}
