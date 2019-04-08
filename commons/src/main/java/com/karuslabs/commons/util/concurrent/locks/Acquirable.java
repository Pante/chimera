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


/**
 * A resource from which concurrent access to it can be acquired.
 */
public interface Acquirable {
    
    /**
     * Acquires mutually exclusive access to this.
     * <br><br>
     * If the object is not available then the current thread becomes disabled for 
     * thread scheduling purposes and lies dormant until the object has been acquired. 
     * 
     * @return a {@code Mutex}
     */
    public Mutex acquire();
    
    /**
     * Acquires mutually exclusive access to this unless the current thread is interrupted.
     * <br><br>
     * If the object is not available then the current thread becomes disabled for 
     * thread scheduling purposes and lies dormant until one of two things happens:
     * <ul>
     * <li> The object is acquired by the current thread
     * <li> Some other thread interrupts the current thread 
     * </ul>
     * 
     * @return a {@code Mutex}
     * @throws InterruptedException if the current thread is interrupted while acquiring 
     *                              the object
     */
    public Mutex acquireInterruptibly() throws InterruptedException;
    
}
