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

import com.karuslabs.annotations.Delegate;

import com.karuslabs.commons.util.concurrent.locks.AutoReadWriteLock.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;
import java.util.concurrent.locks.ReentrantReadWriteLock.*;


/**
 * A {@code AutoReadWriteLock} subclass that contains additional methods for automatic 
 * resource management; i.e. automatic releasing of the lock when a {@code try-with-resources}
 * block is exited.
 */
public class AutoReadWriteLock extends ReentrantReadWriteLock {
    
    private final AutoReadLock reader;
    private final AutoWriteLock writer;
    
    
    /**
     * Creates a non-fair {@code AutoReadWriteLock}.
     */
    public AutoReadWriteLock() {
        this(false);
    }
    
    /**
     * Creates a {@code AutoReaderWriteLock} with the specified fairness policy.
     * 
     * @param fair {@code true} if this lock should use a fair ordering policy 
     */
    public AutoReadWriteLock(boolean fair) {
        super(fair);
        reader = new AutoReadLock(this, super.readLock());
        writer = new AutoWriteLock(this, super.writeLock());
    }
    
    
    /**
     * Returns the lock used for reading.
     * 
     * @return the lock used for reading
     */
    @Override
    public AutoReadLock readLock() {
        return reader;
    }
    
    /**
     * Returns the lock used for writing
     * 
     * @return the lock used for writing
     */
    @Override
    public AutoWriteLock writeLock() {
        return writer;
    }
    
    
    /**
     * A {@code ReadLock} decorator that contains additional methods for automatic 
     * resource management; i.e. automatic releasing of the lock when a {@code try-with-resources}
     * block is exited. Overridden operations are forwarded to an underlying {@code ReadLock}.
     */
    public static @Delegate class AutoReadLock extends ReadLock implements Holdable {
        
        private final ReadLock lock;
        private final Mutex mutex;
        
        /**
         * Creates a {@code AutoReadLock} with the given owning {@code ReentrantReadWriteLock}
         * and underlying {@code ReadLock}.
         * 
         * @param owner the owning {@code ReentrantReadWriteLock}
         * @param lock the underlying {@code ReadLock}
         */
        protected AutoReadLock(ReentrantReadWriteLock owner, ReadLock lock) {
            super(owner);
            this.lock = lock;
            this.mutex = lock::unlock;
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
         * releasing this lock when returning from a {@code try-with-resources} 
         * block.
         * 
         * @return a {@code Mutex}
         * @throws InterruptedException if the current thread is interrupted while 
         *                              acquiring the object
         */
        @Override
        public Mutex holdInterruptibly() throws InterruptedException {
            lockInterruptibly();
            return mutex;
        }
        
        
        @Override
        public void lock() {
            lock.lock();
        }
        
        @Override
        public void lockInterruptibly() throws InterruptedException {
            lock.lockInterruptibly();
        }
        
        @Override
        public boolean tryLock() {
            return lock.tryLock();
        }
        
        @Override
        public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
            return lock.tryLock(timeout, unit);
        }
        
        @Override
        public void unlock() {
            lock.unlock();
        }
  
        @Override
        public Condition newCondition() {
            return lock.newCondition();
        }
        
        @Override
        public String toString() {
            return lock.toString();
        }
        
    }
    
    /**
     * A {@code WriteLock} decorator that contains additional methods for automatic 
     * resource management; i.e. automatic releasing of the lock when a {@code try-with-resources}
     * block is exited. Overridden operations are forwarded to an underlying {@code WriteLock}.
     */
    public static @Delegate class AutoWriteLock extends WriteLock implements Holdable {
        
        private final WriteLock lock;
        private final Mutex mutex;
        
        /**
         * Creates a {@code AutoWriteLock} with the given owning {@code ReentrantReadWriteLock}
         * and underlying {@code WriteLock}.
         * 
         * @param owner the owning {@code ReentrantReadWriteLock}
         * @param lock the underlying {@code WriteLock}
         */
        protected AutoWriteLock(ReentrantReadWriteLock owner, WriteLock lock) {
            super(owner);
            this.lock = lock;
            this.mutex = lock::unlock;
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
         * @throws InterruptedException if the current thread is interrupted while 
         *                              acquiring the object
         */
        @Override
        public Mutex holdInterruptibly() throws InterruptedException {
            lockInterruptibly();
            return mutex;
        }
        
        
        @Override
        public int getHoldCount() {
            return lock.getHoldCount();
        }
        
        @Override
        public boolean isHeldByCurrentThread() {
            return lock.isHeldByCurrentThread();
        }
        
        @Override
        public void lock() {
            lock.lock();
        }
        
        @Override
        public void lockInterruptibly() throws InterruptedException {
            lock.lockInterruptibly();
        }
        
        @Override
        public boolean tryLock() {
            return lock.tryLock();
        }
        
        @Override
        public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
            return lock.tryLock(timeout, unit);
        }
        
        @Override
        public void unlock() {
            lock.unlock();
        }
                
        @Override
        public Condition newCondition() {
            return lock.newCondition();
        }
                
        @Override
        public String toString() {
            return lock.toString();
        }
        
    }
    
}