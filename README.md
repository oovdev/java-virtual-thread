# create virtual thread

---

```java
Thread thread = Thread.ofVirtual().name("myVirtual").start(runnable);
```

```java
Thread thread = Thread.ofVirtual().name("myVirtual").unstarted(runnable);
thread.start();
```

# create virutal thread using ExecutorService

---

```java
ThreadFactory factory = Thread.ofVirtual().name("myVirtual-", 0).factory();
try (ExecutorService executorService = Executors.newThreadPerTaskExecutor(factory)) {
    for (int i = 0; i < 10; i++) {
        executorService.submit(runnable);    
    }
}
```

# pinning

---

```java
private final Runnable runnable = new Runnable() {
    @Override
    public void run() {
        log.info("1) run. thread: {}", Thread.currentThread());
        synchronized (this) {   // <--- synchronized block cause pinning
            try {
                Thread.sleep(5_000);
            } catch (InterruptionException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("2) run. thread: {}", Thread.currentThread());
    }
};
```

# detect pinning

---

```
-Djdk.tracePinnedThreads=full or -Djdk.tracePinnedThreads=short
```

# ReentrantLock

---

```java
private final ReentrantLock lock = new ReentrantLock();

private final Runnable runnable = new Runnable() {
    @Override
    public void run() {
        log.info("1) run. thread: {}", Thread.currentThread());
        
        lock.lock();    // <--- use ReentrantLock instead of synchronized block
        try {
            Thread.sleep(5_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally
            lock.unlock();
    }
    log.info("2) run. thread: {}", Thread.currentThread());
};
```

# Thread Dump for Virtual Thread

```bash
jcmd ${PID} Thread.dump_to_file -format=text ${file}
```

# Spring Boot

---

### virtual thread
```yaml
spring:
  threads:
    virtual:
      enabled: true
```

### task execution
```yaml
spring:
  task:
    execution:
      thread-name-prefix: myTask-
```

### task scheduling
```yaml
spring:
  task:
    scheduling:
      thread-name-prefix: mySch-
```

### keep alive although only daemon thread run
```yaml
spring:
  main:
    keep-alive: true
```

# reference
---

https://docs.oracle.com/en/java/javase/21/core/virtual-threads.html

https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.3-Release-Notes

https://d2.naver.com/helloworld/1203723

https://techblog.woowahan.com/15398/

