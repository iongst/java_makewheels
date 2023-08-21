package com.linkcircle.threadpool;

/**
 * 拒绝策略
 */
public enum Policy {
    AbortPolicy,
    CallerRunsPolicy,
    DiscardOldestPolicy,
    DiscardPolicy;
}
