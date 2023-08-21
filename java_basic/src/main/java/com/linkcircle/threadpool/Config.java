package com.linkcircle.threadpool;

import lombok.Getter;
import lombok.Setter;

import java.util.Queue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author:老薛
 * @version:1.1
 * @date:2023/8/21
 * @description: 配置解析成功之后的对象
 * @vx:laoxue004
 */
@Getter
@Setter
public class Config {
    private Integer corePoolSize;
    private Integer  maxNumPoolSize;
    private Long keepAliveTime;
    private TimeUnit unit;
    private Queue<Thread> workQueue;
    private ThreadFactory threadFactory;
    private Policy handler;
}
