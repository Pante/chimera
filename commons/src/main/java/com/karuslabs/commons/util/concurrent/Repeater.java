/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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


/**
 * A {@code ScheduledThreadPoolExecutor} that can additionally schedule repeating
 * tasks that can be executed a given number of times.
 */
public class Repeater extends ScheduledThreadPoolExecutor {
    
    /**
     * Constructs a {@code Repeater} with the given core pool size.
     * 
     * @param corePoolSize the number of threads to keep in the pool, even if they 
     *                     are idle, unless allowCoreThreadTimeOut is set
     * @throws IllegalArgumentException if corePoolSize < 0
     */
    public Repeater(int corePoolSize) {
        super(corePoolSize);
    }
    
    /**
     * Constructs a {@code Repeater} with the given initial parameters.
     * 
     * @param corePoolSize the number of threads to keep in the pool, even
     *        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * @param threadFactory the factory to use when the executor
     *        creates a new thread
     * @throws IllegalArgumentException if {@code corePoolSize < 0}
     * @throws NullPointerException if {@code threadFactory} is null
     */
    public Repeater(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }
    
    /**
     * Constructs a {@code Repeater} with the given initial parameters.
     * 
     * @param corePoolSize the number of threads to keep in the pool, even
     *        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * @param handler the handler to use when execution is blocked
     *        because the thread bounds and queue capacities are reached
     * @throws IllegalArgumentException if {@code corePoolSize < 0}
     * @throws NullPointerException if {@code handler} is null
     */
    public Repeater(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }
    
    /**
     * Constructs a {@code Repeater} with the given initial parameters.
     *  
     * @param corePoolSize the number of threads to keep in the pool, even
     *        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * @param threadFactory the factory to use when the executor
     *        creates a new thread
     * @param handler the handler to use when execution is blocked
     *        because the thread bounds and queue capacities are reached
     * @throws IllegalArgumentException if {@code corePoolSize < 0}
     * @throws NullPointerException if {@code threadFactory} or
     *         {@code handler} is null
     */
    public Repeater(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }
    
    
    /**
     * Periodically execute the runnable for the given number of times after the
     * initial delay.
     * 
     * @param runnable the runnable to be executed
     * @param initial the initial delay
     * @param period the period between which the runnable is executed
     * @param unit the unit of time for both the initial delay and period
     * @param times the number of times the runnable is to be executed
     * @return a ScheduledFuture representing the pending completion of the runnable
     */
    public ScheduledFuture<?> schedule(Runnable runnable, long initial, long period, TimeUnit unit, long times) {
        return schedule(new RunnableRepetition(runnable, times), initial, period, unit);
    }
    
    /**
     * Periodically execute the repetition of times after the initial delay.
     * 
     * @param repetition the repetition to be executed
     * @param initial the initial delay
     * @param period the period between which the repetition is executed
     * @param unit the unit of time for both the initial delay and period
     * @return a ScheduledFuture representing the pending completion of the repetition
     */
    public ScheduledFuture<?> schedule(Repetition<?> repetition, long initial, long period, TimeUnit unit) {
        return scheduleAtFixedRate(repetition, initial, period, unit);
    }
    
    /**
     * Sets the context of the given runnable if it is a {@code Repetition}.
     * 
     * @param <V> the type of the result
     * @param runnable the submitted runnable
     * @param task the task created to execute the given runnable
     * @return a task that execute the given runnable
     */
    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
        if (runnable instanceof Repetition) {
            var repetition = (Repetition<V>) runnable;
            repetition.set(task);
        }
        
        return task;
    }
    
}


