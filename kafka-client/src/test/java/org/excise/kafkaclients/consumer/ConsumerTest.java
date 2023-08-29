package org.excise.kafkaclients.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

@SpringBootTest
public class ConsumerTest {

    @Autowired
    private KafkaConsumer<String, String> kafkaConsumer;

    @Test
    void testConsumer() {
//        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(3000));
        long start = System.currentTimeMillis();
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(10000));
            System.out.println("是否为空:" + records.isEmpty());
            records.forEach(record -> {
                System.out.println("嘻嘻" + record.key() + "" + record.value() + "" + record.offset());
            });
            if (System.currentTimeMillis() - start > 30 * 1000) {
                break;
            }
        }





//        ConsumerRecords<String, String> records2 = kafkaConsumer.poll(Duration.ofMillis(500));
//        records2.forEach(record -> {
//            System.out.println("鸡鸡" + record.key() + "" + record.value() + "" + record.offset());
//        });

    }
}
