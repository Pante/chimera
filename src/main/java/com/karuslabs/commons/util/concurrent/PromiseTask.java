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
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.scheduler.BukkitRunnable;


public abstract class PromiseTask<T> extends BukkitRunnable implements Promise<T> {
    
    final CountDownLatch latch;
    final AtomicBoolean cancelled;
    final AtomicBoolean done;
    long total;
    long current;
    
    
    public PromiseTask(long iterations) {
        this(new CountDownLatch(1), iterations);
    }
    
    protected PromiseTask(CountDownLatch latch, long iterations) {
        this.latch = latch;
        cancelled = new AtomicBoolean(false);
        done = new AtomicBoolean(false);
        total = iterations;
        current = 0;
    }
    
    
    @Override
    public void run() {
        if (!done.get() && current < total) {
            process();
            current++;
            
        } else {
            end();
            callback();
        }
    }
    
    protected abstract void process();
    
    protected void end() {
        cancelThis();
        done.set(true);
        latch.countDown();
    } 
        
    void cancelThis() {
        try {
            super.cancel();
        } catch (IllegalStateException e) {
        }
    }
    
    protected void callback() {}
    
    
    @Override
    public void cancel() {
        cancel(true);
    }

    @Override
    public boolean cancel(boolean interrupt) {
        if (!done.get() && interrupt) {
            end();
            cancelled.set(true);
            return true;
            
        } else {
            return false;
        }
    }
    
    
    @Override
    public T get() throws InterruptedException, ExecutionException {
        latch.await();
        return value();
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (latch.await(timeout, unit)) {
            return value();
            
        } else {
            throw new TimeoutException();
        }
    }
    
    protected abstract T value();
    
        
    @Override
    public boolean isDone() {
        return done.get();
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled.get();
    }

    public long getTotal() {
        return total;
    }

    public long getCurrent() {
        return current;
    }
    
}
