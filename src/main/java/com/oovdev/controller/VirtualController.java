package com.oovdev.controller;

import com.oovdev.service.VirtualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RequiredArgsConstructor
@RestController
public class VirtualController {

    private final VirtualService virtualService;
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/sleep")
    public void sleep() throws InterruptedException {
        log.info("1) counter: {}, thread: {}", counter.incrementAndGet(), Thread.currentThread());
        Thread.sleep(5_000);
        log.info("2) thread: {}", Thread.currentThread());
    }

    @GetMapping("/async")
    public void async() {
        log.info("1) async. thread: {}", Thread.currentThread());
        virtualService.async();
        log.info("2) async. thread: {}", Thread.currentThread());
    }
}
