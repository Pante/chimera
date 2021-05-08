/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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

import com.karuslabs.annotations.Lazy;

import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * A {@code ScheduledThreadPoolExecutor} subclass which can schedule repeating tasks 
 * that can be executed an arbitrary number of times.
 */
public class Scheduler extends ScheduledThreadPoolExecutor {
    
    /**
     * Creates a {@code Scheduler} with the given core pool size.
     * 
     * @param corePoolSize the number of threads to keep in the pool, even if they 
     *                     are idle, unless allowCoreThreadTimeOut is set
     * @throws IllegalArgumentException if {@code corePoolSize < 0}
     */
    public Scheduler(int corePoolSize) {
        super(corePoolSize);
    }

    /**
     * Creates a {@code Repeater} with the given initial parameters.
     * 
     * @param corePoolSize the number of threads to keep in the pool, even
     *        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * @param threadFactory the factory to use when the executor
     *        creates a new thread
     * @throws IllegalArgumentException if {@code corePoolSize < 0}
     * @throws NullPointerException if {@code threadFactory} is {@code null}
     */
    public Scheduler(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }
    

    /**
     * Creates a {@code Repeater} with the given initial parameters.
     * 
     * @param corePoolSize the number of threads to keep in the pool, even
     *        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * @param handler the handler to use when execution is blocked
     *        because the thread bounds and queue capacities are reached
     * @throws IllegalArgumentException if {@code corePoolSize < 0}
     * @throws NullPointerException if {@code handler} is {@code null}
     */
    public Scheduler(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }

    /**
     * Creates a {@code Repeater} with the given initial parameters.
     *  
     * @param corePoolSize the number of threads to keep in the pool, even
     *        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * @param threadFactory the factory to use when the executor
     *        creates a new thread
     * @param handler the handler to use when execution is blocked
     *        because the thread bounds and queue capacities are reached
     * @throws IllegalArgumentException if {@code corePoolSize < 0}
     * @throws NullPointerException if {@code threadFactory} or
     *         {@code handler} is {@code null}
     */
    public Scheduler(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }
    
    
    /**
     * Periodically execute the consumer for the given number of {@code times} after 
     * the initial delay.
     * 
     * @param task the task to be executed
     * @param initial the initial delay
     * @param period the period between which the task is executed
     * @param unit the unit of time for both the initial delay and period
     * @return a ScheduledFuture representing the pending completion of the task
     */
    public ScheduledFuture<?> schedule(Consumer<Context> task, long initial, long period, TimeUnit unit) {
        return schedule(task, initial, period, unit, Context.INFINITE);
    }
    
    /**
     * Periodically execute the consumer for the given number of {@code times} after 
     * the initial delay.
     * 
     * @param task the task to be executed
     * @param initial the initial delay
     * @param period the period between which the task is executed
     * @param unit the unit of time for both the initial delay and period
     * @param times the number of times the task is to be executed
     * @return a ScheduledFuture representing the pending completion of the task
     */
    public ScheduledFuture<?> schedule(Consumer<Context> task, long initial, long period, TimeUnit unit, long times) {
        return scheduleAtFixedRate(new RunnableContext(task, times), initial, period, unit);
    }
    
     /**
     * Sets the context of the given runnable if it is a {@code RunnableContext}.
     * 
     * @param <V> the type of the result
     * @param runnable the submitted runnable
     * @param future the future created to execute the given runnable
     * @return a task that execute the given runnable
     */
    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> future) {
        if (runnable instanceof RunnableContext) {
            ((RunnableContext) runnable).future = future;
        }
        
        return future;
    }
    
}

/**
 * A {@code Context} that wraps a {@code Consumer<Context>} for repeated execution.
 */
class RunnableContext implements Context, Runnable {
    
    private final Consumer<Context> task;
    @Lazy Future<?> future;
    volatile long times;
    
    /**
     * Creates a {@code RunnableContext} that wraps and executes the given task.
     * 
     * @param task the task to be executed
     * @param times the number of times the task is to be executed
     */
    RunnableContext(Consumer<Context> task, long times) {
        this.task = task;
        this.times = times;
    }
    
    @Override
    public void run() {
        if (times == INFINITE || times > 0) {
            task.accept(this);
            if (times > 0) {
                times--;
            }
            
        } else {
            cancel();
        }
    }
    
    @Override
    public void cancel() {
        future.cancel(false);
    }

    @Override
    public long times() {
        return times;
    }
    
}


