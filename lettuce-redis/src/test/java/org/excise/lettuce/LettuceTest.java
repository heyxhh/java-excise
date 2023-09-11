package org.excise.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class LettuceTest {

    private static final Logger logger = LoggerFactory.getLogger(LettuceTest.class);

    @Test
    public void testPing() {
        RedisURI redisURI = RedisURI.builder()
                .withHost("127.0.0.1")
                .withPort(6379)
                .withTimeout(Duration.of(60, ChronoUnit.SECONDS))
                .build();

        RedisClient redisClient = RedisClient.create(redisURI);
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        RedisCommands<String, String> cmd = connect.sync();
        logger.info("ping: {}", cmd.ping());
        connect.close();
        redisClient.shutdown();
    }

    @Test
    public void testSyncCmd() throws InterruptedException {
        RedisURI redisURI = RedisURI.builder()
                .withHost("127.0.0.1")
                .withPort(6379)
                .withTimeout(Duration.of(60, ChronoUnit.SECONDS))
                .build();
        RedisClient redisClient = RedisClient.create(redisURI);
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        // 获取同步命令工具
        RedisCommands<String, String> cmd = connect.sync();

        // 若uri上没指定，默认db=0
        logger.info("当前db: {}", redisURI.getDatabase());
        logger.info("清空数据: {}", cmd.flushdb());
        logger.info("判断某个键是否存在: {}", cmd.exists("name"));
        logger.info("新增<'username','xxx'>的键值对：{}", cmd.set("username", "xxx"));
        logger.info("新增<'password','ggg'>的键值对: {}", cmd.set("password", "ggg"));
        logger.info("获取<'password'>键的值：{}", cmd.get("password"));
        logger.info("系统中所有的键如下：{}", cmd.keys("*"));
        logger.info("删除键password: {}", cmd.del("password"));
        logger.info("判断键password是否存在：{}", cmd.exists("password"));
        logger.info("设置键username的过期时间为5s: {}", cmd.expire("username", 5));
        TimeUnit.SECONDS.sleep(1);
        logger.info("查看键username的剩余生存时间：{}", cmd.ttl("username"));
        // 移除生存时间后，生命变成永久
        logger.info("移除键username的生存时间：{}", cmd.persist("username"));
        TimeUnit.SECONDS.sleep(1);
        logger.info("查看键username的剩余生存时间：{}", cmd.ttl("username"));
        logger.info("查看键username所存储的值的类型：{}", cmd.type("username"));

        connect.close();
        redisClient.shutdown();
    }

    @Test
    public void testAsyncCmd() throws InterruptedException {
        RedisURI redisURI = RedisURI.builder()
                .withHost("127.0.0.1")
                .withPort(6379)
                .withTimeout(Duration.of(60, ChronoUnit.SECONDS))
                .build();
        RedisClient redisClient = RedisClient.create(redisURI);
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        // 获取同步命令工具
        RedisAsyncCommands<String, String> cmd = connect.async();

        // 若uri上没指定，默认db=0
        logger.info("当前db: {}", redisURI.getDatabase());
        logger.info("清空数据: {}", cmd.flushdb());
        logger.info("判断某个键是否存在: {}", cmd.exists("name"));
        logger.info("新增<'username','xxx'>的键值对：{}", cmd.set("username", "xxx"));
        logger.info("新增<'password','ggg'>的键值对: {}", cmd.set("password", "ggg"));
        logger.info("获取<'password'>键的值：{}", cmd.get("password"));
        logger.info("系统中所有的键如下：{}", cmd.keys("*"));
        logger.info("删除键password: {}", cmd.del("password"));
        logger.info("判断键password是否存在：{}", cmd.exists("password"));
        logger.info("设置键username的过期时间为5s: {}", cmd.expire("username", 5));
        TimeUnit.SECONDS.sleep(1);
        logger.info("查看键username的剩余生存时间：{}", cmd.ttl("username"));
        // 移除生存时间后，生命变成永久
        logger.info("移除键username的生存时间：{}", cmd.persist("username"));
        TimeUnit.SECONDS.sleep(1);
        logger.info("查看键username的剩余生存时间：{}", cmd.ttl("username"));
        logger.info("查看键username所存储的值的类型：{}", cmd.type("username"));

        connect.close();
        redisClient.shutdown();
    }

}
