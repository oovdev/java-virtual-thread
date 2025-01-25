package com.oovdev.purejava;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Virtual thread dump
 *
 * jstat 활용 시 Virtual thread dump 안됨
 * jcmd 활용해야 함.
 *  1) jps 커맨드로 PID 확인
 *  2) jcmd ${PID} Thread.dump_to_file -format=text ${FILE_PATH | FILE_NAME}
 * */

@Slf4j
public class VirtualThreadDump {

    private static final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            log.info("1) run. thread: {}", Thread.currentThread());

            try {
                Thread.sleep(30_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("2) run. thread: {}", Thread.currentThread());
        }
    };

    public static void main(String[] args) throws InterruptedException {
        log.info("1) main. thread: " + Thread.currentThread());

        taskExecutor();

        log.info("2) main. thread: " + Thread.currentThread());
    }

    private static void taskExecutor() {
        ThreadFactory factory = Thread.ofVirtual().name("myVirtual-", 0).factory();
        try (ExecutorService executorService = Executors.newThreadPerTaskExecutor(factory)) {
            for (int i = 0; i < 33; i++) {
                executorService.submit(runnable);
            }
        }
    }
}
