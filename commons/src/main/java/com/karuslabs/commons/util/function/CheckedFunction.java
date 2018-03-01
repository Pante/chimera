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

import java.util.function.Function;


/**
 * Represents a {@link java.util.function.Function} which throws an exception.
 * 
 * @param <T> the type of the argument
 * @param <R> the type of the result of the function
 * @param <E> the type of the exception thrown
 */
@FunctionalInterface
public interface CheckedFunction<T, R, E extends Exception> {
    
    /**
     * Applies this function to the given argument.
     * 
     * @param t the argument
     * @return the function result
     * @throws E if the operation fails
     */
    public R apply(T t) throws E;
    
    
    /**
     * Returns a {@code CheckedFunction} wrapped with a {@code Function} which throws an
     * {@code UncheckedFunctionException} if the specified {@code CheckedFunction} threw an
     * exception.
     * 
     * @param <T> the type of the argument
     * @param <R> the type of the result of the function
     * @param <E> the type of the exception thrown
     * @param function the CheckedFunction to be wrapped
     * @return a Function which wraps the specified CheckedFunction
     */
    public static <T, R, E extends Exception> Function<T, R> uncheck(CheckedFunction<T, R, E> function) {
        return t -> {
            try {
                return function.apply(t);
                
            } catch (Exception e) {
                throw new UncheckedFunctionException(e);
            }
        };
    }
    
}
