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


/**
 * An abstract subclass of {@code ResultTask} which represents a cancellable repeating asynchronous computation.
 * <p>
 * This class is a Bukkit/Spigot-specific counterpart for {@link java.util.concurrent.ScheduledFuture} which works in conjuction
 * with the Bukkit/Spigot scheduler.
 * <p>
 * This class is invoked each iteration and tracking its own number of iterations ran, starting from 0.
 * 
 * @param <T> the result type of the repeating asynchronous computation
 */
public abstract class ScheduledResultTask<T> extends ResultTask<T> implements Repeatable {
    
    /**
     * Returns a {@code ScheduledResultTask} which will, upon each iteration, execute the specified {@code Runnable} for 
     * the specified number of iterations and returns the specified {@code result} upon successful completion.
     * 
     * @param <T> the result type of the repeating asynchronous computation
     * @param runnable the runnable
     * @param value the result to return on successful completion
     * @param iterations the number of iterations to execute this task
     * @return the ScheduledResultTask
     */
    public static <T> ScheduledResultTask<T> of(Runnable runnable, T value, long iterations) {
        return new ScheduledPromiseRunnable<>(runnable, value, iterations);
    }
    
    
    /**
     * Signifies that the {@code ScheduledResultTask} should execute for an infinite number of iterations.
     */
    public static final int INFINITE = -1;
    
    long current;
    long total;
    
    
    /**
     * Constructs a {@code ScheduledResultTask} with the specified number of iterations to run.
     * 
     * @param iterations the number of iterations
     */
    public ScheduledResultTask(long iterations) {
        current = 0;
        total = iterations;
    }
    
    
    /**
     * Runs the asynchronous computation for each iteration.
     * 
     */
    @Override
    public void run() {
        if (total == -1 || current < total) {
            try {
                process();
                if (total != -1) {
                    current++;
                }
            } catch (Exception e) {
                thrown = e;
                done();
            }
            
        } else {
            done();
        }
    }
    
    @Override
    void finish(int updated) {
        super.finish(updated);
        callback();
    }
    
    /**
     * Runs upon the completion of this task.
     */
    protected void callback() {};
    
    
    /**
     * Returns the number of iterations ran.
     * 
     * @return the number of iterations
     */
    @Override
    public long getCurrent() {
        return current;
    }
    
    /**
     * Returns the total number of iterations this task is to run.
     * 
     * @return the total number of iterations
     */
    @Override
    public long getIterations() {
        return total;
    }
    
    
    static class ScheduledPromiseRunnable<T> extends ScheduledResultTask<T> {
        
        private final Runnable runnable;
        private final T value;
        
        ScheduledPromiseRunnable(Runnable runnable, T value, long iterations) {
            super(iterations);
            this.runnable = runnable;
            this.value = value;
        }
        
        @Override
        protected void process() throws Exception {
            runnable.run();
        }

        @Override
        protected T value() {
            return value;
        }
        
    }
    
}
