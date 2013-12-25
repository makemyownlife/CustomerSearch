package com.diyicai.search;

import org.apache.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 定时生成索引任务
 * User: zhangyong
 * Date: 13-12-18
 * Time: 下午7:33
 * To change this template use File | Settings | File Templates.
 */
public class LuceneTimerTask {

    private final static Logger logger = Logger.getLogger(LuceneTimerTask.class);

    private final static String THREAD_NAME = "CreateLuceneIndexThread";

    private ScheduledExecutorService scheduledExecutorService;

    private AtomicBoolean flag = new AtomicBoolean(false);

    private final static long INITIAL_DELAY = 1;

    private final static long DELAY = 5;

    public void init() {
        if (!flag.get()) {
            this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setName(THREAD_NAME);
                    t.setDaemon(false);
                    return t;
                }
            });
            this.scheduledExecutorService.scheduleWithFixedDelay(
                    new CreateLuceneIndexThread()
                    ,
                    INITIAL_DELAY,
                    DELAY,
                    TimeUnit.MINUTES);
        } else {
            logger.warn("定时任务已经初始化过");
        }
    }

}
