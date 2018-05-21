package com.knowcup.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步的Executor、Exception配置
 * 当【最小线程数】已经被占用满后，新的任务会被放进【等候队列】里面，
 * 当【等候队列】的capacity也被占满之后，pool里面会创建新线程处理这个任务，直到总线程数达到了【最大线程数】
 * 这时系统会根据【拒绝策略】处理新任务（
 *   ABORT（缺省）：抛出TaskRejectedException异常，然后不执行
 *   DISCARD：不执行，也不抛出异常
 *   DISCARD_OLDEST：丢弃queue中最旧的那个任务
 *   CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
 * ）
 */
@Configuration
public class SpringAsyncConfig implements AsyncConfigurer {
    // 最小线程数
    @Value("${spring.task.pool.corePoolSize}")
    private Integer coreSize;

    // 最大线程数
    @Value("${spring.task.pool.maxPoolSize}")
    private Integer maxSize;

    // 等待队列
    @Value("${spring.task.pool.queueCapacity}")
    private Integer queue;

    // 超过core size的那些线程，任务完成后，再经过这个时长（秒）会被结束掉缺省值为：Integer.MAX_VALUE
    @Value("${spring.task.pool.keepAliveSeconds}")
    private Integer aliveSeconds;

    // 线程前缀名称
    @Value("${spring.task.pool.threadNamePrefix}")
    private String threadNamePrefix;

    /**
     * Executor
     * 系统默认为：SimpleAsyncTaskExecutor
     * 此处改为：ThreadPoolTaskExecutor
     * @return
     */
    @Nullable
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(coreSize);
        executor.setMaxPoolSize(maxSize);
        executor.setQueueCapacity(queue);
        executor.setKeepAliveSeconds(aliveSeconds);
        executor.setThreadNamePrefix(threadNamePrefix);

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        //ABORT（缺省）：抛出TaskRejectedException异常，然后不执行
        //DISCARD：不执行，也不抛出异常
        //DISCARD_OLDEST：丢弃queue中最旧的那个任务
        //CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize(); //如果不初始化，导致找到不到执行器
        return executor;
    }

    @Nullable
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncExceptionHandler();
    }
}
