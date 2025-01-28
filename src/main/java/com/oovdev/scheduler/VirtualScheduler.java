package com.oovdev.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VirtualScheduler {

    @Scheduled(fixedRate = 5_000)
    public void fixedRate() {
        log.info("fixedRate. thread: {}", Thread.currentThread());
    }

    @Scheduled(fixedRate = 5_000, scheduler = "threadPoolTaskScheduler")
    public void fixedRate2() {
        log.info("fixedRate2. thread: {}", Thread.currentThread());
    }
}
