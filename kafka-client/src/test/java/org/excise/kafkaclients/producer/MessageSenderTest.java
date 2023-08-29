package org.excise.kafkaclients.producer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class MessageSenderTest {

    @Autowired
    private MessageSender sender;

    @Test
    public void testSender() throws ExecutionException, InterruptedException {
         sender.send("dididi", "xixixi");
//        TimeUnit.SECONDS.sleep(2);
    }
}
