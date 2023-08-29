package org.excise.kafkaclients.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Configuration
public class KafkaConsumerConfig {

    @Value("${kafka.consumer.group}")
    private String groupId;

    private Properties genProperties() {
        Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "123.249.38.62:9092");

        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);


        // heartbeat.interval.ms 指定了 poll() 方法向协调器发送心跳的频率
        properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 1000);

        // 该属性指定了消费者在被认为死亡之前可以与服务器断开连接的时间，默认是 3s。
        // 如 果消费者没有在 session.timeout.ms 指定的时间内发送心跳给群组协调器，
        // 就被认为 已经死亡，协调器就会触发再均衡，把它的分区分配给群组里的其他消费者。
        // 该属性与 heartbeat.interval.ms紧密相关。heartbeat.interval.ms 指定了 poll() 方法向协调器发送心跳的频率，
        // session.timeout.ms 则指定了消费者可以多久不发送心跳。
        // 所以，一 般需要同时修改这两个属性，heartbeat.interval.ms 必须比 session.timeout.ms 小，一 般是 session.timeout.ms 的三分之一。
        // 如果 session.timeout.ms 是 3s，那么 heartbeat. interval.ms 应该是 1s。
        // 把 session.timeout.ms 值设得比默认值小，可以更快地检测和恢 复崩溃的节点，不过长时间的轮询或垃圾收集可能导致非预期的再均衡。
        // 把该属性的值设 置得大一些，可以减少意外的再均衡，不过检测节点崩溃需要更长的时间。
//        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 3000);

        // 如果两次poll操作间隔超过了这个时间，broker就会认为这个consumer处理能力太弱，会将其踢出消费组，
        // 将分区分配给别的consumer消费 ，触发rebalance 。
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 600 * 1000);

        // 该属性指定了消费者在读取一个没有偏移量的分区或者偏移量无效的情况下(因消费者长 时间失效，包含偏移量的记录已经过时并被删除)该作何处理。
        // 它的默认值是 latest，意 思是说，在偏移量无效的情况下，消费者将从最新的记录开始读取数据(在消费者启动之 后生成的记录)。
        // 另一个值是 earliest，意思是说，在偏移量无效的情况下，消费者将从 起始位置读取分区的记录。
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // 该属性指定了消费者是否自动提交偏移 量，默认值是 true。
        // 为了尽量避免出现重复数据和数据丢失，可以把它设为 false，由自 己控制何时提交偏移量。
        // 如果把它设为 true，还可以通过配置 auto.commit.interval.ms 属性来控制提交的频率。
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);

        // 如果 enable.auto.commit 被设为 true，那么每过5s(默认值是 5s)，消费者会自动把从 poll() 方法接收到的最大偏移量提交上去。
        // 消费者poll时会检查上次提交后间隔时间，如果超过auto.commit.interval.ms，就提交上次poll的offset
         properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 60 * 1000);

        // 该属性用于控制单次调用 call() 方法能够返回的记录数量，可以帮你控制在轮询里需要处理的数据量。
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 30);

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        return properties;
    }

    @Bean
    public KafkaConsumer<String, String> kafkaConsumer() {
        Properties properties = genProperties();
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);

        kafkaConsumer.subscribe(Collections.singleton(groupId));

        return kafkaConsumer;
    }
}
