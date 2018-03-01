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
package com.karuslabs.commons.util.function;

import java.util.function.BiConsumer;


/**
 * Represents a {@link java.util.function.BiConsumer} which throws an exception.
 * 
 * @param <T> the type of the first argument
 * @param <U> the type of the second argument
 * @param <E> the type of the exception thrown
 */
@FunctionalInterface
public interface CheckedBiConsumer<T, U, E extends Exception> {
    
    /**
     * Performs this operation on the given arguments.
     * 
     * @param t the first argument
     * @param u the second argument
     * @throws E if the operation fails
     */
    public void accept(T t, U u) throws E;
    
    
    /**
     * Returns a {@code CheckedBiConsumer} wrapped with a {@code BiConsumer} which throws an
     * {@code UncheckedFunctionException} if the specified {@code CheckedBiConsumer} threw an
     * exception.
     * 
     * @param <T> the type of the first argument
     * @param <U> the type of the second argument
     * @param <E> the type of the exception thrown
     * @param consumer the CheckedBiConsumer to be wrapped
     * @return a BiConsumer which wraps the specified CheckedBiConsumer
     */
    public static<T, U, E extends Exception> BiConsumer<T, U> uncheck(CheckedBiConsumer<T, U, E> consumer) {
        return (t, u) -> {
            try {
                consumer.accept(t, u);
                
            } catch (Exception e) {
                throw new UncheckedFunctionException(e);
            }
        };
    }
    
}
