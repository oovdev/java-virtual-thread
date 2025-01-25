package com.oovdev.purejava;

import java.util.List;

public class ForkJoinPoolTest {
    public static void main(String[] args) {
        List<Integer> list = List.of(1,2,3,4,5,6,7,8,9,10);

        // .stream(): Single thread
        // .parallelStream(): multi thread
        int op = list.parallelStream()
                .filter(integer -> {
                    System.out.println("i: " + integer +
                            ", thread: " + Thread.currentThread()
                            + ", isDaemon: " + Thread.currentThread().isDaemon());
                    boolean isEven = integer % 2 == 0;
                    return isEven;
                })
                .findAny()
                .get();

        /**
         * ForkJoinPool은 Daemon으로 동작. Java에서 Thread Pool 필요 시 생성하는데 Daemon으로 동작함을 확인 가능
         *
         * i: 10, thread: Thread[#38,ForkJoinPool.commonPool-worker-9,5,main], isDaemon: true
         * i: 1, thread: Thread[#35,ForkJoinPool.commonPool-worker-6,5,main], isDaemon: true
         * i: 4, thread: Thread[#34,ForkJoinPool.commonPool-worker-5,5,main], isDaemon: true
         * i: 8, thread: Thread[#36,ForkJoinPool.commonPool-worker-7,5,main], isDaemon: true
         * i: 5, thread: Thread[#32,ForkJoinPool.commonPool-worker-3,5,main], isDaemon: true
         * i: 3, thread: Thread[#30,ForkJoinPool.commonPool-worker-1,5,main], isDaemon: true
         * i: 7, thread: Thread[#1,main,5,main], isDaemon: false
         * i: 9, thread: Thread[#33,ForkJoinPool.commonPool-worker-4,5,main], isDaemon: true
         * i: 6, thread: Thread[#37,ForkJoinPool.commonPool-worker-8,5,main], isDaemon: true
         * i: 2, thread: Thread[#31,ForkJoinPool.commonPool-worker-2,5,main], isDaemon: true
         * */

        System.out.println(op); // single thread 동작 시 2 고정, parallel thread 시 반드시 2가 나오지 않음
    }
}
