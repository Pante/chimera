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

import java.util.concurrent.*;


/**
 * A cancellable result of an asynchronous computation with additional retrieval 
 * methods.
 * 
 * @param <T> the type of the result
 */
public class EventualTask<T> extends FutureTask<T> implements Eventual<T> {
    
    /**
     * Constructs a {@code EventualTask} that will, upon running execute the given
     * callable.
     * 
     * @param callable the callable task
     */
    public EventualTask(Callable<T> callable) {
        super(callable);
    }
    
    /**
     * Creates a {@code EventualTask} that will, upon running, execute the given 
     * Runnable, and arrange that get will return the given result on successful 
     * completion.
     * 
     * @param runnable the runnable task
     * @param result the result to return on successful completion.
     */
    public EventualTask(Runnable runnable, T result) {
        super(runnable, result);
    }
    
}
