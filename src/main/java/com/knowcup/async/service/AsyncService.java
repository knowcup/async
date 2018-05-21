package com.knowcup.async.service;

import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.Future;

public interface AsyncService {

    @Async
    void asyncMethod1() throws InterruptedException;

    @Async
    void asyncMethod2(String arg) throws InterruptedException;

    @Async
    Future<String> asyncMethod3(String arg) throws InterruptedException;
}
