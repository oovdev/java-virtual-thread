package com.oovdev.purejava;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Pinned Virtual thread Case
 * and
 * Using ReentrantLock
 * */

@Slf4j
public class Pinning {

    private final ReentrantLock lock = new ReentrantLock();

    // -Djdk.tracePinnedThreads=full or -Djdk.tracePinnedThreads=short 통해 detect
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            log.info("1) run. thread: " + Thread.currentThread());
//            synchronized (this) {   // point of pinned-virtual-thread
            lock.lock();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
//            }
            log.info("2) run. thread: " + Thread.currentThread());
        }
    };

    /**
     * [synchronized block - about 10s] : Useless case.
     * 10:15:50.862 [main] INFO com.oovdev.purejava.Pinning -- 2) main. time: 10039, thread: Thread[#1,main,5,main]
     *
     *  versus.
     *
     * [ReentrantLock - about 5s] : Virtual thread having Same performance with Platform Thread.
     * 10:14:08.708 [main] INFO com.oovdev.purejava.Pinning -- 2) main. time: 5029, thread: Thread[#1,main,5,main]
     * */

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        log.info("1) main. thread: {}", Thread.currentThread());

        virtual();
        //platform();

        log.info("2) main. time: {}, thread: {}",  (System.currentTimeMillis() - startTime), Thread.currentThread());
    }

    private static void virtual() {
        ThreadFactory factory = Thread.ofVirtual().name("myVirtual-", 0).factory();
        try (ExecutorService executor = Executors.newThreadPerTaskExecutor(factory)) {
            for (int i = 0; i < 20; i++) {
                Pinning p = new Pinning();
                executor.submit(p.runnable);
            }
        }
    }

    private static void platform() {
        try (ExecutorService executor = Executors.newFixedThreadPool(20)) {
            for (int i = 0; i < 20; i++) {
                Pinning p = new Pinning();
                executor.submit(p.runnable);
            }
        }
    }
}
