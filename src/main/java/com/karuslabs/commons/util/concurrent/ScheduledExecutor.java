/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.commons.util.concurrent;

import java.util.concurrent.*;
import java.util.function.BiConsumer;


public class ScheduledExecutor extends ScheduledThreadPoolExecutor {

    public ScheduledExecutor(int corePoolSize) {
        super(corePoolSize);
    }
    
    public ScheduledExecutor(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }

    public ScheduledExecutor(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }
    
    public ScheduledExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }

    
    public ScheduledFuture<?> schedule(Runnable runnable, long iterations, long initialDelay, long delay, TimeUnit unit) {
        return schedule(new ScheduledRunnable(runnable, iterations), initialDelay, delay, unit);
    }
    
    public ScheduledFuture<?> schedule(BiConsumer<Long, Long> consumer, long iterations, long initialDelay, long delay, TimeUnit unit) {
        return schedule(new ScheduledRunnable(consumer, iterations), initialDelay, delay, unit);
    }
    
    public ScheduledFuture<?> schedule(ScheduledRunnable runnable, long initialDelay, long delay, TimeUnit unit) {
        return scheduleWithFixedDelay(runnable, initialDelay, delay, unit);
    }
    
    
    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask​(Runnable runnable, RunnableScheduledFuture<V> task) {
        if (runnable instanceof ScheduledRunnable) {
            ((ScheduledRunnable) runnable).future = task;
        }
        
        return task;
    }
    
}
