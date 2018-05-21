package com.knowcup.async.service.impl;

import com.knowcup.async.service.AsyncService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class AsyncServiceImpl implements AsyncService {

    /**
     * 简单的异步
     * @throws InterruptedException
     */
    @Async
    @Override
    public void asyncMethod1() throws InterruptedException {
        System.out.println("simple start =====================");
        System.out.println("=====" + Thread.currentThread().getName() + "=========");
        System.out.println("simple end =====================");
    }

    // 默认的exceutor为：ThreadPoolTaskExecutor(由SpringAsyncConfig配置)
    // 若不采用默认，可用 @Async("executor名称") 指定执行器
    @Async
    @Override
    public void asyncMethod2(String arg) throws InterruptedException {
        System.out.println("arg:" + arg + " start =================");
        System.out.println("=====" + Thread.currentThread().getName() + "=========");
        Thread.sleep(5000);
        System.out.println("arg:" + arg + " end =================");
    }

    /**
     * 有返回值  有参数的异步
     * @param arg
     *          传入参数
     * @return
     *
     * @throws InterruptedException
     */
    @Async
    @Override
    public Future<String> asyncMethod3(String arg) throws InterruptedException {
        System.out.println("arg:" + arg + " have result start ======================");
        System.out.println("=====" + Thread.currentThread().getName() + "=========");
        Thread.sleep(3000);
        System.out.println("arg:" + arg + " have result end ======================");

        return new AsyncResult<>("asyncMethod3 end end == "+ arg);
    }
}
