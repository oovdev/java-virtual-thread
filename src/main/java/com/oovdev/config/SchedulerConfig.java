package com.oovdev.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
@Configuration
public class SchedulerConfig {

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() { // Classic Platform thread
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.setThreadNamePrefix("mySch-");
        return threadPoolTaskScheduler;
    }

    @Primary
    @Bean
    public TaskScheduler asyncTaskScheduler() {
        // Creates virtual thread executor for scheduling
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(
                1,  // Single thread sufficient for scheduling tasks
                Thread.ofVirtual()
                        .name("myAsyncSch-", 0)
                        .factory()
        );

        // Converts to Spring taskScheduler
        return new ConcurrentTaskScheduler(executorService);
    }
}
