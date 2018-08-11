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

import com.karuslabs.commons.util.concurrent.locks.CloseableReadWriteLock.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;


public class CloseableReadWriteLock extends ReentrantReadWriteLock {
    
    CloseableReadLock reader;
    CloseableWriteLock writer;
    
    
    public CloseableReadWriteLock() {
        this(false);
    }
    
    public CloseableReadWriteLock(boolean fair) {
        super(fair);
        reader = new AcquisitionReadLock(super.readLock());
        writer = new AcquistionWriteLock(super.writeLock());
    }
    
    
    @Override
    public CloseableReadLock readLock() {
        return reader;
    }
    
    @Override
    public CloseableWriteLock writeLock() {
        return writer;
    }
    
    
    public static abstract class CloseableReadLock extends ReadLock implements Acquirable {
        
        ReadLock lock;

        protected CloseableReadLock(ReadLock lock) {
            super(null);
            this.lock = lock;
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
        public Condition newCondition() {
            return lock.newCondition();
        }
        
        @Override
        public String toString() {
            return lock.toString();
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
        
    }
    
    public static abstract class CloseableWriteLock extends WriteLock implements Acquirable {
        
        WriteLock lock;
        
        protected CloseableWriteLock(WriteLock lock) {
            super(null);
            this.lock = lock;
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
        public Condition newCondition() {
            return lock.newCondition();
        }
        
        @Override
        public String toString() {
            return lock.toString();
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
        
    }
    
}


class AcquisitionReadLock extends CloseableReadLock implements Acquisition {

    AcquisitionReadLock(ReentrantReadWriteLock.ReadLock lock) {
        super(lock);
    }

    @Override
    public Acquisition acquire() {
        lock();
        return this;
    }

    @Override
    public Acquisition acquireInterruptibly() throws InterruptedException {
        lockInterruptibly();
        return this;
    }

    @Override
    public void close() {
        unlock();
    }
    
}


class AcquistionWriteLock extends CloseableWriteLock implements Acquisition {

    AcquistionWriteLock(ReentrantReadWriteLock.WriteLock lock) {
        super(lock);
    }

    @Override
    public Acquisition acquire() {
        lock();
        return this;
    }

    @Override
    public Acquisition acquireInterruptibly() throws InterruptedException {
       lockInterruptibly();
       return this;
    }

    @Override
    public void close() {
        unlock();
    }
    
}