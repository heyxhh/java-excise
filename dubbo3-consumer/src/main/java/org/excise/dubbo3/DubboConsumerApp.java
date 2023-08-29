package org.excise.dubbo3;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.rpc.model.FrameworkModel;
import org.excise.dubbo3.api.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Hello world!
 *
 */
public class DubboConsumerApp
{
    private final static Logger logger = LoggerFactory.getLogger(DubboConsumerApp.class);


    public static void main( String[] args )
    {
        runWithBootStrap();
    }

    private static void runWithBootStrap() {
        // ReferenceConfig<DemoService> reference = new ReferenceConfig<>(FrameworkModel.defaultModel().newApplication().newModule());
        ReferenceConfig<DemoService> reference = new ReferenceConfig<>();
        reference.setInterface(DemoService.class);
        reference.setGeneric("true");
        reference.setProtocol(CommonConstants.TRIPLE);

//        HashMap<String, String> params1 = new HashMap<>();
//        params1.put("migration.step", "FORCE_APPLICATION");
//        reference.setParameters(params1);
        reference.setProvidedBy("DemoService-provider");


        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        ApplicationConfig applicationConfig = new ApplicationConfig("DemoService-Consumer");
        applicationConfig.setQosEnable(false);
        applicationConfig.setQosPort(-1);
        applicationConfig.setRegisterConsumer(false);


        RegistryConfig registryConfig = new RegistryConfig("nacos://123.249.38.62:8848");
        HashMap<String, String> params = new HashMap<>();
        params.put("namespace", "8dea8c26-6e9b-4d1b-baf7-6230a654d74e");
        registryConfig.setParameters(params);

        bootstrap.application(applicationConfig)
                .registry(registryConfig)
                .protocol(new ProtocolConfig(CommonConstants.TRIPLE, -1))
                .reference(reference)
                .start();

        DemoService demoService = bootstrap.getCache().get(reference);
//         String message = demoService.sayHello("大王");
//         System.out.println(message);

//        demoService.sayHelloServerStream("小王", new StreamObserver<>() {
//
//            @Override
//            public void onNext(String data) {
//                logger.info("收到消息：" + data);
//                logger.info("值1：{}", localString.get());
//                localString.set("嘻嘻嘻");
//                logger.info("值2：{}", localString.get());
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                throwable.printStackTrace();
//            }
//
//            @Override
//            public void onCompleted() {
//                logger.info("已经关闭");
//            }
//        });

        StreamObserver<String> responseStream = new StreamObserver<>() {

            @Override
            public void onNext(String data) {
                logger.info("收到消息：" + data);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onCompleted() {
                logger.info("已经关闭");
            }
        };

        StreamObserver<String> requestStream = demoService.sayHelloBiStream(responseStream);

        requestStream.onNext("第一次");
        requestStream.onNext("第二次");
        requestStream.onCompleted();



    }
}
