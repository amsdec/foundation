package io.carvill.foundation.queues.sqs;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import io.carvill.foundation.queues.QueueMessage;
import io.carvill.foundation.queues.exception.QueueException;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class SQSPullQueueTest {

    private SQSPullQueue queue;

    private String queueUrl;

    private ThreadPoolTaskExecutor taskExecutor;

    public SQSPullQueueTest() {
        this.queue = new SQSPullQueue("your_key", "your_secret", "your_queue_region");
        this.queue.setVisibilityTimeout(60);
        this.queueUrl = "your_queue_url";

        this.taskExecutor = new ThreadPoolTaskExecutor();
        this.taskExecutor.setMaxPoolSize(10);
        this.taskExecutor.setQueueCapacity(100);
        this.taskExecutor.setKeepAliveSeconds(1);
        this.taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        this.taskExecutor.setCorePoolSize(5);
        this.taskExecutor.initialize();
    }

    @Test
    @Ignore
    public void test01_add() throws QueueException {
        final TestMessage body = new TestMessage(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date()));
        final String messageId = this.queue.add(this.queueUrl, body, 10);
        System.out.println(messageId);
    }

    @Test
    @Ignore
    public void test02_pull() throws InterruptedException {
        final AtomicInteger counter = new AtomicInteger(0);
        final AtomicInteger success = new AtomicInteger(0);
        final AtomicInteger errors = new AtomicInteger(0);
        final int items = 10;
        for (int t = 0; t < items; t++) {
            this.taskExecutor.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        final List<QueueMessage<TestMessage>> messages = SQSPullQueueTest.this.queue
                                .pull(SQSPullQueueTest.this.queueUrl, TestMessage.class, 5);
                        System.out.println(messages);
                        if (!messages.isEmpty()) {
                            success.incrementAndGet();

                            for (final QueueMessage<TestMessage> queueMessage : messages) {
                                SQSPullQueueTest.this.queue.remove(SQSPullQueueTest.this.queueUrl,
                                        queueMessage.getDeletionId());
                                System.out.printf("Message %s was deleted\n", queueMessage.getId());
                            }
                        }
                    } catch (final QueueException e) {
                        errors.incrementAndGet();
                    }
                    counter.incrementAndGet();
                }

            });

        }

        while (counter.get() != items) {
            Thread.sleep(250);
        }
        System.out.println("Success: " + success.get());
        System.out.println("Errors: " + errors.get());
    }

    @Test
    @Ignore
    public void test03_remove() throws QueueException {
        this.queue.remove(this.queueUrl, "message_deletion_id");
    }

    static class TestMessage implements Serializable {

        private static final long serialVersionUID = 7579578108194876575L;

        private String data;

        public TestMessage() {
        }

        public TestMessage(final String data) {
            this.data = data;
        }

        public String getData() {
            return this.data;
        }

        public void setData(final String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return String.format("TestMessage [data=%s]", this.data);
        }

    }

}
