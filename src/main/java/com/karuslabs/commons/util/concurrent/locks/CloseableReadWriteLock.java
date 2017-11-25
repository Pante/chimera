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
package com.karuslabs.commons.util.concurrent.locks;

import com.karuslabs.commons.annotation.Shared;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nonnull;


/**
 * A subclass of {@link java.util.concurrent.locks.ReentrantReadWriteLock} that contains additional methods which automatically
 * unlock when exiting a {@code try}-with-resources block.
 */
public class CloseableReadWriteLock extends ReentrantReadWriteLock {
    
    private final transient Janitor readJanitor;
    private final transient Janitor writeJanitor;
    
    
    /**
     * Constructs a non-fair {@code CloseableReadWriteLock}.
     */
    public CloseableReadWriteLock() {
        this(false);
    }
    
    /**
     * Constructs a {@code CloseableReadWriteLock} with the specified fairness policy.
     * 
     * @param fair true if this lock should use a fair ordering policy
     */
    public CloseableReadWriteLock(boolean fair) {
        super(fair);
        readJanitor = readLock()::unlock;
        writeJanitor = writeLock()::unlock;
    }
    
    
    /**
     * Acquires the {@link java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock}.
     * This method automatically unlocks when exiting a {@code try}-with-resources block.
     * 
     * @return the Janitor used by the try-with-resources block to unlock this lock
     */
    public @Shared @Nonnull Janitor acquireReadLock() {
        readLock().lock();
        return readJanitor;
    }
    
    /**
     * Acquires the {@link java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock} unless the current thread is interrupted.
     * This method automatically unlocks when exiting a {@code try}-with-resources block.
     * 
     * @return the Janitor used by the try-with-resources block to unlock this lock
     * @throws InterruptedException if the current thread is interrupted
     */
    public @Shared @Nonnull Janitor acquireReadLockInterruptibly() throws InterruptedException {
        readLock().lockInterruptibly();
        return readJanitor;
    }
    
    
    /**
     * Acquires the {@link java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock}.
     * This method automatically unlocks when exiting a {@code try}-with-resources block.
     * 
     * @return the Janitor used by the try-with-resources block to unlock this lock
     */
    public @Shared @Nonnull Janitor acquireWriteLock() {
        writeLock().lock();
        return writeJanitor;
    }
    
    /**
     * Acquires the {@link java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock} unless the current thread is interrupted.
     * This method automatically unlocks when exiting a {@code try}-with-resources block.
     * 
     * @return the Janitor used by the try-with-resources block to unlock this lock
     * @throws InterruptedException if the current thread is interrupted
     */
    public @Shared @Nonnull Janitor acquireWriteLockInterruptibly() throws InterruptedException {
        writeLock().lockInterruptibly();
        return writeJanitor;
    }
    
}
