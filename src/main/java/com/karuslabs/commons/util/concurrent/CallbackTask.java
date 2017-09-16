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


public abstract class CallbackTask<T> extends Task implements UncheckedFuture<T> {
    
    private CountDownLatch latch;
    private AtomicBoolean cancelled;
    private AtomicBoolean running;
    private AtomicBoolean done;
    
    
    public CallbackTask(long iterations) {
        super(iterations);
        latch = new CountDownLatch(1);
        cancelled = new AtomicBoolean(false);
        done = new AtomicBoolean(false);
    }

    
    @Override
    public T get() throws InterruptedException, ExecutionException {
        latch.await();
        return value();
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        latch.await(timeout, unit);
        return value();
    }
    
    protected abstract T value();
    
   
    @Override
    public void cancel() {
        cancel(true);
    }
    
    @Override
    public boolean cancel(boolean interrupt) {
        if (running.get() && interrupt) {
            end();
            cancelled.set(true);
            return true;
            
        } else {
            return false;
        }
    }

    @Override
    protected void end() {
        super.cancel();
        latch.countDown();
        running.set(false);
        done.set(true);
    }
    

    @Override
    public boolean isCancelled() {
        return cancelled.get();
    }

    @Override
    public boolean isDone() {
        return done.get();
    }
    
}
