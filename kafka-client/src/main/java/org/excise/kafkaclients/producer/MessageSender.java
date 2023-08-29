package org.excise.kafkaclients.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class MessageSender {

    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    @Autowired
    @Qualifier("defaultProducer")
    private KafkaProducer<String, String> kafkaProducer;

    public void send(String k, String v) throws ExecutionException, InterruptedException {
        ProducerRecord<String, String> record = new ProducerRecord<>("kafka-demo", k, v);
        RecordMetadata meta = kafkaProducer.send(record).get();
        logger.info("kafka返回的结果: partition: {}, topic: {}, offset: {}", meta.partition(), meta.topic(), meta.offset());
    }

    public void sendAsync(String k, String v) throws ExecutionException, InterruptedException {
        ProducerRecord<String, String> record = new ProducerRecord<>("kafka-demo", k, v);
        kafkaProducer.send(record, ((meta, e) -> {
            if (e != null) {
                e.printStackTrace();
            }
            logger.info("partition: {}, topic: {}, offset: {}", meta.partition(), meta.topic(), meta.offset());
        }));

    }
}
