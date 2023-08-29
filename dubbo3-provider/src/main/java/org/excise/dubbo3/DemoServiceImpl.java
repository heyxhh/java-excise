package org.excise.dubbo3;

import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.rpc.RpcContext;
import org.excise.dubbo3.api.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoServiceImpl implements DemoService {

    private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Override
    public String sayHello(String name) {
        String msg = String.format("Hello %s, request from consumer: %s .", name, RpcContext.getServerContext().getRemoteAddress());
        logger.info(msg);
        return msg;
    }

    @Override
    public void sayHelloServerStream(String name, StreamObserver<String> response) {
        response.onNext("你好啊：" + name);
        logger.info("第一次已经发出");
        response.onNext(name + "好不好啊！");
        logger.info("第一次已经发出");
        response.onCompleted();
    }

    @Override
    public StreamObserver<String> sayHelloBiStream(StreamObserver<String> response) {
        return new StreamObserver<>() {

            @Override
            public void onNext(String data) {
                logger.info("收到消息: {}", data);

                // 第二次执行onNext的时候，ThreadLocal变量的值跟上一次onNext赋值的值一样
                // 说明两次onNext的是用的同一个线程
                logger.info("1-LocalString的值：{}", LocalContext.get());
                LocalContext.set(data);

                logger.info("2-LocalString的值：{}", LocalContext.get());

                response.onNext("收到了消息: " + data);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onCompleted() {
                logger.info("关闭流");
            }
        };
    }

}
