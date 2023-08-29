package org.excise.dubbo3.api;

import org.apache.dubbo.common.stream.StreamObserver;

import java.util.concurrent.CompletableFuture;

public interface DemoService {
    String sayHello(String name);

    default CompletableFuture<String> sayHelloAsync(String name) {
        return CompletableFuture.completedFuture(sayHello(name));
    }

    void sayHelloServerStream(String name, StreamObserver<String> response);

    StreamObserver<String> sayHelloBiStream(StreamObserver<String> response);
}
