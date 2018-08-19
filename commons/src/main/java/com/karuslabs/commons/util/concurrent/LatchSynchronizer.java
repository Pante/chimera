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

import com.karuslabs.annotations.Ignored;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;


public class LatchSynchronizer extends AbstractQueuedSynchronizer {

    public LatchSynchronizer(int count) {
        setState(count);
    }
    
    
    public int count() {
        return getState();
    }

    
    @Override
    protected boolean tryAcquire(@Ignored int acquires) {
        return getState() == 0;
    }
    
    @Override
    protected int tryAcquireShared(@Ignored int acquires) {
        return getState() == 0 ? 1 : -1;
    }
    
    
    @Override
    protected boolean tryRelease(int releases) {
        return tryReleaseShared(releases);
    }
    
    @Override
    protected boolean tryReleaseShared(int releases) {
        while (true) {
            int current = getState();
            if (current == 0) {
                return false;
            }

            int unchecked = current - releases;
            int updated = unchecked >= 0 ? unchecked : 0;
            if (compareAndSetState(current, updated)) {
                return updated == 0;
            }
        }
    }

}
