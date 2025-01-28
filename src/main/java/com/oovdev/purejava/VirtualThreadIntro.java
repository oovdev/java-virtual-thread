package com.oovdev;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VirtualThreadEx {

    private static final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            log.info("1) run. thread: {}, class: {}", Thread.currentThread(), Thread.currentThread().getClass());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("2) run. thread: {}", Thread.currentThread());
        }
    };

    private static Thread createVirtualThreadUnstarted() {
        Thread t1 = Thread.ofVirtual().name("myVirtual1").unstarted(runnable);
        t1.start();
        return t1;
    }

    private static Thread createVirtualThreadStarted() {
        Thread t2 = Thread.ofVirtual().name("myVirtual2").start(runnable);
        return t2;
    }

    public static void main(String[] args) throws InterruptedException {
        log.info("1) main. thread: {}", Thread.currentThread());

        Thread thread = createVirtualThreadStarted();
        //Thread thread2 = createVirtualThreadUnstarted();
        thread.join();

        log.info("2) main. thread: {}", Thread.currentThread());

        /**log
         * 1) main. thread: Thread[#1,main,5,main]
         * 1) run. thread: VirtualThread[#31,myVirtual2]/runnable@ForkJoinPool-1-worker-1, class: class java.lang.VirtualThread
         * 2) run. thread: VirtualThread[#31,myVirtual2]/runnable@ForkJoinPool-1-worker-1
         * 2) main. thread: Thread[#1,main,5,main]
         * */
    }

    public static void _main(String[] args) throws InterruptedException {
//        Thread thread = new Thread(runnable);
//        //thread.setDaemon(true);
//        thread.start();
//        //thread.join();

        /**log
         * 08:12:43.140 [Thread-0] INFO com.oovdev.Live -- 1) run. thread: Thread[#30,Thread-0,5,main], class: class java.lang.Thread
         * 08:12:44.155 [Thread-0] INFO com.oovdev.Live -- 2) run. thread: Thread[#30,Thread-0,5,main]
         * */

        // Virtual thread
        Thread thread = Thread.ofVirtual().name("myVirtual").start(runnable);
        thread.join();  // virtual thread는 기본적으로 daemon thread 사용(ForkJoinPool)

        /**log
         * 08:12:11.825 [myVirtual] INFO com.oovdev.Live -- 1) run. thread: VirtualThread[#31,myVirtual]/runnable@ForkJoinPool-1-worker-1, class: class java.lang.VirtualThread
         * 08:12:12.847 [myVirtual] INFO com.oovdev.Live -- 2) run. thread: VirtualThread[#31,myVirtual]/runnable@ForkJoinPool-1-worker-1
         * */
    }
}
