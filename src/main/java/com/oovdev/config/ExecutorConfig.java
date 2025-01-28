package com.oovdev.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Slf4j
@Configuration
public class ExecutorConfig {

    @Primary
    @Bean
    public AsyncTaskExecutor taskExecutor() {
        ThreadFactory factory = Thread.ofVirtual()
                .name("virtual-", 0)
                .factory();
        return new TaskExecutorAdapter(Executors.newThreadPerTaskExecutor(factory));
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setThreadNamePrefix("myThreadPool-");
        return threadPoolTaskExecutor;
    }
    /**
     * [omcat-handler-0] c.oovdev.controller.VirtualController    : 1) async. thread: VirtualThread[#64,tomcat-handler-0]/runnable@ForkJoinPool-1-worker-1
     * [omcat-handler-0] c.oovdev.controller.VirtualController    : 2) async. thread: VirtualThread[#64,tomcat-handler-0]/runnable@ForkJoinPool-1-worker-1
     * [ myThreadPool-1] com.oovdev.service.VirtualService        : 1) async. thread: Thread[#70,myThreadPool-1,5,VirtualThreads]
     * [ myThreadPool-1] com.oovdev.service.VirtualService        : 2) async. thread: Thread[#70,myThreadPool-1,5,VirtualThreads]
     * */
}
