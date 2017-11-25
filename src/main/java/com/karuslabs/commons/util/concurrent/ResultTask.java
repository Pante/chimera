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

import com.karuslabs.commons.annotation.Blocking;

import java.util.concurrent.*;

import org.bukkit.scheduler.BukkitRunnable;


/**
 * A skeleton implementation of {@code Result} which represents a cancellable asynchronous computation.
 * <p>
 * This class is a Bukkit/Spigot-specific counterpart for {@link FutureTask} which works in conjuction
 * with the Bukkit/Spigot scheduler.
 * <p>
 * This class provides a base implementation of {@link Result} with methods to start and cancel a computation, 
 * query to see if the computation is complete, and retrieve the result of the computation. 
 * The result can only be retrieved when the computation has completed. Once the computation has completed, 
 * the computation cannot be restarted or cancelled.
 * <p>
 * 
 * @param <T> the result type of the asynchronous computation
 */
public abstract class ResultTask<T> extends BukkitRunnable implements Result<T> {
    
    /**
     * Returns a {@code ResultTask} which will, upon running, execute the specified {@code Runnable} and return the specified {@code result}
     * upon successful completion.
     * 
     * @param <T> the result type of the asynchronous computation
     * @param runnable the runnable
     * @param result the result to return on successful completion
     * @return the ResultTask
     */
    public static <T> ResultTask<T> of(Runnable runnable, T result) {
        return new PromiseRunnable<>(runnable, result);
    }
    
    /**
     * Returns a {@code ResultTask} which will, upon running, execute the specified {@code Callable}.
     * 
     * @param <T> the result type of the asynchronous computation
     * @param callable the callable
     * @return the ResultTask
     */
    public static <T> ResultTask<T> of(Callable<T> callable) {
        return new PromiseCallable<>(callable);
    }
    
    
    static final int NEW = 0;
    static final int RUNNING = 1;
    static final int COMPLETED = 2;
    static final int CANCELLED = 3;
    
    volatile int state;
    volatile Throwable thrown;
    CountDownLatch latch;
    
    
    /**
     * Constructs a {@code ResulTask}.
     */
    public ResultTask() {
        state = NEW;
        thrown = null;
        latch = new CountDownLatch(1);
    }

    /**
     * Sets this {@code ResultTask} to the result of its computation unless it has been cancelled.
     */
    @Override
    public void run() {
        try {
            process();
            
        } catch (Throwable e) {
            thrown = e;
            
        } finally {
            done();
        }
    }
    
    /**
     * Sets this {@code ResultTask} to the result of its computation if successful; else throws an {@code Exception}.
     * Subclasses of {@code ResultTask} should implement this method to customise the computation invoked when {@link #run()} is invoked.
     * 
     * @throws Exception if this ResultTask fails to complete
     */
    protected abstract void process() throws Exception;        
    
    /**
     * Sets the state of this {@code ResultTask} as completed.
     */
    protected void done() {
        finish(COMPLETED);
    }
    
    /**
     * Cancels the asynchronous computation for this {@code ResultTask} and interrupts the computation if running.
     */
    @Override
    public void cancel() {
        cancel(true);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (state < COMPLETED && mayInterruptIfRunning) {
            finish(CANCELLED);
            return true;
            
        } else {
            return false;
        }
    }
    
    void finish(int updated) {
        cancelThis();
        latch.countDown();
        state = updated;
        callback();
    }
    
    /**
     * Runs upon the cancellation or completion of this task.
     */
    protected void callback() {};
    
    void cancelThis() {
        try {
            super.cancel();
        } catch (NullPointerException | IllegalStateException e) {
            // Honestly doesn't really matter if the NPE is ignored as it means Bukkit.getScheduler() returned null
            // in which case the task wouldn't have been scheduled to begin with...
        }
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    @Blocking
    public T get() throws InterruptedException, ExecutionException {
        latch.await();
        return handle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Blocking
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (latch.await(timeout, unit)) {
            return handle();
            
        } else {
            throw new TimeoutException();
        }
    }
    
    /**
     * Handles the result of the computation. Returns the result if this task was successfully completed,
     * throws an {@code ExecutionException} if an exception was thrown during the computation, or throws a
     * {@code CancellationException} if this task was cancelled.
     * 
     * @return the result of this task if successfully completed
     * @throws ExecutionException if an exception was thrown during the computation for this task
     * @throws CancellationException if this task was cancelled
     */
    protected T handle() throws ExecutionException {
        if (thrown != null) {
            throw new ExecutionException(thrown);
            
        } else if (state == CANCELLED) {
            throw new CancellationException();
            
        } else {
            return value();
        }
    }
    
    /**
     * Returns the value to return if this {@code ResulTask} successfully completes.
     * 
     * @return the value to return if this ResultTask successfully completes
     */
    protected abstract T value();
    
    
    /**
     * Returns whether this task was cancelled.
     * 
     * @return true if this task was cancelled; else false
     */
    @Override
    public boolean isCancelled() {
        return state == CANCELLED;
    }
    
    /**
     * Returns whether this task is done.
     * 
     * @return true if this task is done; else false
     */
    @Override
    public boolean isDone() {
        return RUNNING < state;
    }
    
    
    static class PromiseRunnable<T> extends ResultTask<T> {
        
        private final Runnable runnable;
        private final T value;
        
        PromiseRunnable(Runnable runnable, T value) {
            this.runnable = runnable;
            this.value = value;
        }
        
        @Override
        protected void process() {
            runnable.run();
        }

        @Override
        protected T value() {
            return value;
        }
        
    }
    
    static class PromiseCallable<T> extends ResultTask<T> {
        
        private final Callable<T> callable;
        private T value;
        
        PromiseCallable(Callable<T> callable) {
            this.callable = callable;
        }
        
        @Override
        protected void process() throws Exception {
            value = callable.call();
        }

        @Override
        protected T value() {
            return value;
        }
        
    }
    
}
