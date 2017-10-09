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

import java.util.function.BiPredicate;


/**
 * Represents a {@link java.util.function.BiPredicate} which throws an exception.
 * 
 * @param <T> the type of the first argument
 * @param <U> the type of the second argument
 * @param <E> the type of the exception thrown
 */
@FunctionalInterface
public interface CheckedBiPredicate<T, U, E extends Exception> {
    
     /**
     * Evaluates this predicate on the given arguments.
     * 
     * @param t the first argument
     * @param u the second argument
     * @return true if the input arguments match the predicate, otherwise false
     * @throws E if the operation fails
     */
    public boolean test(T t, U u) throws E;
    
    
    /**
     * Returns a {@code CheckedBiPredicate} wrapped with a {@code BiPredicate} which throws an
     * {@code UncheckedFunctionException} if the specified {@code CheckedBiPredicate} threw an
     * exception.
     * 
     * @param <T> the type of the first argument
     * @param <U> the type of the second argument
     * @param <E> the type of the exception thrown
     * @param predicate the CheckedBiPredicate to be wrapped
     * @return a BiPredicate which wraps the specified CheckedBiPredicate
     */
    public static<T, U, E extends Exception> BiPredicate<T, U> uncheck(CheckedBiPredicate<T, U, E> predicate) {
        return (t, u) -> {
            try {
               return predicate.test(t, u);
                
            } catch (Exception e) {
                throw new UncheckedFunctionException(e);
            }
        };
    }
    
}
