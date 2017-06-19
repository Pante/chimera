/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karuslabs.commons.util.concurrent;

import java.util.concurrent.locks.*;


public class CloseableReadWriteLock {
    
    private ReadWriteLock lock;
    private Janitor readJanitor;
    private Janitor writeJanitor;
    
    
    public CloseableReadWriteLock() {
        this(new ReentrantReadWriteLock());
    }
    
    public CloseableReadWriteLock(ReadWriteLock lock) {
        this.lock = lock;
        readJanitor = () -> lock.readLock().unlock();
        writeJanitor = () -> lock.writeLock().unlock();
    }
    
    
    public Janitor readLock() {
        lock.readLock().lock();
        return readJanitor;
    }
    
    public Janitor writeLock() {
        lock.writeLock().lock();
        return writeJanitor;
    }
    
    
    public ReadWriteLock getReadWriteLock() {
        return lock;
    }
    
}
