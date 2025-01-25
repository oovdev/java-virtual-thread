package com.oovdev.purejava;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Performance comparison between Virtual threads and Platform threads.
 * */

@Slf4j
public class PerformanceTest {

    private static final Runnable ioBoundRunnable = new Runnable() {
        @Override
        public void run() {
            log.info("1) run. thread: {}", Thread.currentThread());
            try {
                Thread.sleep(5_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("2) run. thread: {}", Thread.currentThread());
        }
    };

    private static final Runnable cpuBoundRunnable = new Runnable() {
        @Override
        public void run() {
            log.info("1) run. thread: {}", Thread.currentThread());
            long sum = 0;
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sum = sum + i;
            }
            log.info("2) run. sum: {}, thread: {}", sum, Thread.currentThread());
        }
    };

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        log.info("1) main. thread: {}", Thread.currentThread());

        /*
         - IO bound 작업: Virtual thread 성능이 우수
         - sleep이나 blocking 시 unmount 돼서 효율 높임
        */
//        platformThreadWithIoBound(20000); // if count == 20000: 11.5s
//        virtualThreadWithIoBound(20000); // if count == 20000: 6.6s

        /*
         - CPU bound 작업: Virtual thread를 사용하는 장점이 없음. 계속 CPU를 하기에.
         - virtual thread 생성하고 스케줄링 하는데서 overhead 발생
        */
//        platformThreadWithCpuBound(600); // if count == 600: 28.7s
//        virtualThreadWithCpuBound(600); // if count == 600: 27s

        log.info("2) main. time: {}, thread: {}", (System.currentTimeMillis()-startTime) , Thread.currentThread());
    }

    private static void platformThreadWithIoBound(int count) {
        try (ExecutorService executorService = Executors.newFixedThreadPool(10000)) {
            for (int i = 0; i < count; i++) {
                executorService.submit(ioBoundRunnable);
            }
        }
    }

    private static void virtualThreadWithIoBound(int count) {
        ThreadFactory factory = Thread.ofVirtual().name("myVirtual-", 0).factory();
        try (ExecutorService executorService = Executors.newThreadPerTaskExecutor(factory)) {
            for (int i = 0; i < count; i++) {
                executorService.submit(ioBoundRunnable);
            }
        }
    }

    private static void platformThreadWithCpuBound(int count) {
        try (ExecutorService executorService = Executors.newFixedThreadPool(100)) {
            for (int i = 0; i < count; i++) {
                executorService.submit(cpuBoundRunnable);
            }
        }
    }

    private static void virtualThreadWithCpuBound(int count) {
        ThreadFactory factory = Thread.ofVirtual().name("myVirtual-", 0).factory();
        try (ExecutorService executorService = Executors.newThreadPerTaskExecutor(factory)) {
            for (int i = 0; i < count; i++) {
                executorService.submit(cpuBoundRunnable);
            }
        }
    }
}
