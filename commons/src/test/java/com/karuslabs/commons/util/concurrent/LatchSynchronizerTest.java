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

import java.util.concurrent.Semaphore;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import static java.lang.Thread.State.*;


@ExtendWith(MockitoExtension.class)
class LatchSynchronizerTest {
    
    LatchSynchronizer synchronizer = new LatchSynchronizer(1);
    Semaphore semaphore = new Semaphore(0);
    
    
    @Test
    void acquire() throws InterruptedException {
        var thread = new Thread(() -> {  
            try {
                semaphore.release();
                synchronizer.acquire(1);
            } finally {
                semaphore.release();
                while (true) {}
            }
        });
        thread.start();
        
        semaphore.acquire();
        Thread.sleep(750);
        assertEquals(WAITING, thread.getState());
        
        synchronizer.release(10);
        semaphore.acquire();
        assertEquals(RUNNABLE, thread.getState());
    }
    
    
    @Test
    void acquireShared() throws InterruptedException {
        var thread = new Thread(() -> {  
            try {
                semaphore.release();
                synchronizer.acquireShared(1);
            } finally {
                semaphore.release();
                while (true) {}
            }
        });
        thread.start();
        
        semaphore.acquire();
        Thread.sleep(750);
        assertEquals(WAITING, thread.getState());
        
        
        synchronizer.release(10);
        semaphore.acquire();
        assertEquals(RUNNABLE, thread.getState());
    }
    
    
    @ParameterizedTest
    @MethodSource("release_provider")
    void release(Consumer<LatchSynchronizer> consumer) {
        consumer.accept(synchronizer);
        consumer.accept(synchronizer);
        
        assertEquals(0, synchronizer.count());
    }
    
    static Stream<Consumer<LatchSynchronizer>> release_provider() {
        return Stream.of(latch -> latch.release(2), latch -> latch.releaseShared(2));
    }
    
}
