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
package com.karuslabs.commons.util.concurrent.locks;

import java.util.concurrent.locks.ReentrantLock;

/**
 * A {@code ReentrantLock} subclass that contains additional methods for automatic 
 * resource management; i.e. automatic releasing of the lock when a {@code try-with-resources}
 * block is exited.
 */
public class AutoLock extends ReentrantLock implements Holdable {
    
    private final Mutex mutex;
    
    /**
     * Creates a non-fair {@code AutoLock}.
     */
    public AutoLock() {
        this(false);
    }
    
    /**
     * Creates a {@code AutoLock} with the specified fairness policy.
     * 
     * @param fair {@code true} if this lock should use a fair ordering policy 
     */
    public AutoLock(boolean fair) {
        super(fair);
        this.mutex = this::unlock;
    }
    
    /**
     * Acquires this lock, automatically releasing this lock when returning from
     * a {@code try-with-resources} block.
     * 
     * @return a {@code Mutex}
     */
    @Override
    public Mutex hold() {
        lock();
        return mutex;
    }

    /**
     * Acquires this lock unless the current thread is interrupted, automatically 
     * releasing this lock when returning from a {@code try-with-resources} block.
     * 
     * @return a {@code Mutex}
     * @throws InterruptedException if the current thread is interrupted while acquiring 
     *                              the lock
     */
    @Override
    public Mutex holdInterruptibly() throws InterruptedException {
        lockInterruptibly();
        return mutex;
    }
    
}