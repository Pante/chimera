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

import java.util.concurrent.Future;


/**
 * A {Code Repetition} that accepts and executes a given runnable each time this
 * repetition is executed.
 * 
 * @param <T> the type of the result
 */
public class RunnableRepetition<T> extends Repetition<T> {
    
    private Runnable runnable;
    
    
    /**
     * Constructs a {@code RunnableRepetition} that will, upon running execute the
     * runnable for the given umber of times.
     * 
     * @param runnable the runnable to be executed each time
     * @param times the number of times this repetition is to be executed
     */
    public RunnableRepetition(Runnable runnable, long times) {
        super(times);
        this.runnable = runnable;
    }

    /**
     * Executes the given runnable, ignoring the context.
     * 
     * @param context the context used to control the execution and result of this
     *                repetition
     */
    @Override
    protected void run(Future<T> context) {
        runnable.run();
    }
    
}
