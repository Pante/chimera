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
package com.karuslabs.commons.util;

import com.karuslabs.commons.annotation.Static;

import java.util.function.*;
import javax.annotation.*;


/**
 * This class consist exclusively of static methods which handles the retrieval of {@code null}able instances.
 */
@Static
public class Get {
    
    /**
     * Returns the specified {@code object}, or the specified {@code value}
     * if the specified {@code object} is null.
     * 
     * @param <T> the type of the instance to return
     * @param object the instance to return if non-null
     * @param value the value to return if the specified object is null
     * @return the specified object if non-null; else the specified value
     */
    public static <T> T orDefault(@Nullable T object, T value) {
        return object != null ? object : value;
    }
    
    /**
     * Returns the specified {@code object}, or the value produced by the specified {@code Supplier} 
     * if the specified {@code object} is null.
     * 
     * @param <T> the type of the instance to return
     * @param object the instance to return if non-null
     * @param supplier the supplying function which produces a value
     * @return the specified object if non-null; else the value produced by the specified Supplier
     */
    public static <T> T orDefault(@Nullable T object, Supplier<T> supplier) {
        return object != null ? object : supplier.get();
    }
    
    /**
     * Returns the specified {@code object}, or throws an exception produced by the specified {@code exception}
     * if the specified {@code object} is null.
     * 
     * @param <T> the type of the instance to return
     * @param <E> the type of the exception to throw
     * @param object the instance to return of non-null
     * @param exception the supplying function which produces an exception to throw
     * @return the specified object if non-null
     * @throws E if the specified object is null
     */
    public static <T, E extends RuntimeException> @Nonnull T orThrow(@Nullable T object, Supplier<E> exception) {
        if (object != null) {
            return object;
            
        } else {
            throw exception.get();
        }
    }
    
}
