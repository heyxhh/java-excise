package org.excise.kafkaclients.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.ByteBuffer;
import java.util.Properties;

@Configuration
public class KafkaProducerConfig {

    private Properties genProperties() {

        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "123.249.38.62:9092");

        // acks 参数指定了必须要有多少个分区副本收到消息，生产者才会认为消息写入是成功的
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        // retries 参数的值决定了生产者可以重发消息的次数，如果达到这个次数，生产者会 放弃重试并返回错误。
        props.put(ProducerConfig.RETRIES_CONFIG, 1);

        // 该参数指定了生产者在收到服务器响应之前可以发送多少个消息。它的值越高，就会占用 越多的内存，不过也会提升吞吐量。
        // 把它设为 1 可以保证消息是按照发送的顺序写入服务 器的，即使发生了重试。
        // props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);

        // 当有多个消息需要被发送到同一个分区时，生产者会把它们放在同一个批次里。
        // 该参数指 定了一个批次可以使用的内存大小，按照字节数计算(而不是消息个数)。当批次被填满， 批次里的所有消息会被发送出去。
        // 不过生产者并不一定都会等到批次被填满才发送，半满 的批次，甚至只包含一个消息的批次也有可能被发送。
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);

        // 该参数指定了生产者在发送批次之前等待更多消息加入批次的时间。KafkaProducer 会在 批次填满或 linger.ms 达到上限时把批次发送出去。
        // 默认情况下，只要有可用的线程，生产者就会把消息发送出去，就算批次里只有一个消息。
        // 把 linger.ms 设置成比 0 大的数， 让生产者在发送批次之前等待一会儿，使更多的消息加入到这个批次。
        // 虽然这样会增加延迟，但也会提升吞吐量(因为一次性发送更多的消息，每个消息的开销就变小了)。
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1000);

        // 该参数用来设置生产者内存缓冲区的大小，生产者用它缓冲要发送到服务器的消息。
        // 如果 应用程序发送消息的速度超过发送到服务器的速度，会导致生产者空间不足。
        // 这个时候， send() 方法调用要么被阻塞，要么抛出异常，取决于如何设置 block.on.buffer.full 参数
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        return props;
    }

    @Bean
    public KafkaProducer<String, String> defaultProducer() {
        Properties properties = genProperties();
        return new KafkaProducer<>(properties);
    }
}
