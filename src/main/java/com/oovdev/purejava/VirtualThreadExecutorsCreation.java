package com.oovdev.purejava;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Slf4j
public class VirtualThreadExecutorsCreation {

    private static final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            log.info("1) run. thread: {}", Thread.currentThread());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("2) run. thread: {}", Thread.currentThread());
        }
    };

    public static void main(String[] args) {
        log.info("1) main. thread: {}", Thread.currentThread());

        ThreadFactory factory = Thread.ofVirtual().name("myVirtual-", 0).factory();

        //Executors.newVirtualThreadPerTaskExecutor()
        try (ExecutorService executorService = Executors.newThreadPerTaskExecutor(factory)) {
            for (int i = 0; i < 100; i++) {
                executorService.submit(runnable);
            }
        }

//        antiPattern1();
//        antiPattern2();

        log.info("2) main. thread: {}", Thread.currentThread());
    }

    private static void antiPattern1() {
        ThreadFactory factory = Thread.ofVirtual().name("myVirtual-", 0).factory();
        try (ExecutorService executorService = Executors.newFixedThreadPool(1, factory)) {
            for (int i = 0; i < 10; i++) {
                executorService.submit(runnable);
            }
        }

        /**log
         * 08:31:43.931 [myVirtual-0] INFO com.oovdev.purejava.VirtualThreadExecutorsCreation -- 2) run. thread: VirtualThread[#31,myVirtual-0]/runnable@ForkJoinPool-1-worker-1
         * 08:31:43.931 [myVirtual-0] INFO com.oovdev.purejava.VirtualThreadExecutorsCreation -- 1) run. thread: VirtualThread[#31,myVirtual-0]/runnable@ForkJoinPool-1-worker-1
         * 08:31:44.932 [myVirtual-0] INFO com.oovdev.purejava.VirtualThreadExecutorsCreation -- 2) run. thread: VirtualThread[#31,myVirtual-0]/runnable@ForkJoinPool-1-worker-1
         * 08:31:44.932 [myVirtual-0] INFO com.oovdev.purejava.VirtualThreadExecutorsCreation -- 1) run. thread: VirtualThread[#31,myVirtual-0]/runnable@ForkJoinPool-1-worker-1
         * 08:31:45.944 [myVirtual-0] INFO com.oovdev.purejava.VirtualThreadExecutorsCreation -- 2) run. thread: VirtualThread[#31,myVirtual-0]/runnable@ForkJoinPool-1-worker-1
         * 08:31:45.944 [myVirtual-0] INFO com.oovdev.purejava.VirtualThreadExecutorsCreation -- 1) run. thread: VirtualThread[#31,myVirtual-0]/runnable@ForkJoinPool-1-worker-1
         * 08:31:46.946 [myVirtual-0] INFO com.oovdev.purejava.VirtualThreadExecutorsCreation -- 2) run. thread: VirtualThread[#31,myVirtual-0]/runnable@ForkJoinPool-1-worker-1
         * 08:31:46.946 [myVirtual-0] INFO com.oovdev.purejava.VirtualThreadExecutorsCreation -- 1) run. thread: VirtualThread[#31,myVirtual-0]/runnable@ForkJoinPool-1-worker-1
         * */
    }

    private static void antiPattern2() {
        ThreadFactory factory = Thread.ofVirtual().name("myVirtual-", 0).factory();
        try (ExecutorService executorService = Executors.newSingleThreadExecutor(factory)) {
            for (int i = 0; i < 10; i++) {
                executorService.submit(runnable);
            }
        }

        /**log
         * 08:32:42.566 [myVirtual-0] INFO com.oovdev.purejava.VirtualThreadExecutorsCreation -- 2) run. thread: VirtualThread[#31,myVirtual-0]/runnable@ForkJoinPool-1-worker-1
         * 08:32:42.566 [myVirtual-0] INFO com.oovdev.purejava.VirtualThreadExecutorsCreation -- 1) run. thread: VirtualThread[#31,myVirtual-0]/runnable@ForkJoinPool-1-worker-1
         * 08:32:43.581 [myVirtual-0] INFO com.oovdev.purejava.VirtualThreadExecutorsCreation -- 2) run. thread: VirtualThread[#31,myVirtual-0]/runnable@ForkJoinPool-1-worker-1
         * 08:32:43.581 [myVirtual-0] INFO com.oovdev.purejava.VirtualThreadExecutorsCreation -- 1) run. thread: VirtualThread[#31,myVirtual-0]/runnable@ForkJoinPool-1-worker-1
         * 08:32:44.596 [myVirtual-0] INFO com.oovdev.purejava.VirtualThreadExecutorsCreation -- 2) run. thread: VirtualThread[#31,myVirtual-0]/runnable@ForkJoinPool-1-worker-1
         * */
    }
}
