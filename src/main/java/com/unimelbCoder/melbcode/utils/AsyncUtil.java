package com.unimelbCoder.melbcode.utils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import cn.hutool.core.thread.ExecutorBuilder;
import com.google.common.util.concurrent.SimpleTimeLimiter;

public class AsyncUtil {

    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
        private final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            Thread thread = this.defaultFactory.newThread(r);
            if (!thread.isDaemon()) {
                thread.setDaemon(true);
            }

            thread.setName("paicoding-" + this.threadNumber.getAndIncrement());
            return thread;
        }
    };
    private static ExecutorService executorService;
    private static SimpleTimeLimiter simpleTimeLimiter;

    static {
        initExecutorService(0, 50);
    }

    public static void initExecutorService(int core, int max) {
        executorService = new ExecutorBuilder().setCorePoolSize(core).setMaxPoolSize(max).setKeepAliveTime(0).setKeepAliveTime(0, TimeUnit.SECONDS).setWorkQueue(new SynchronousQueue<Runnable>()).setHandler(new ThreadPoolExecutor.CallerRunsPolicy()).setThreadFactory(THREAD_FACTORY).buildFinalizable();
        simpleTimeLimiter = SimpleTimeLimiter.create(executorService);
    }

    public static void execute(Runnable call) {
        executorService.execute(call);
    }

}
