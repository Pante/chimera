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
package com.karuslabs.commons.command.arguments;

import com.karuslabs.commons.util.Get;

import java.util.function.*;
import javax.annotation.*;


/**
 * Represents an argument for a {@code Commmand}.
 */
public class Argument {
    
    private String argument;
    
    
    /**
     * Constructs an empty {@code Argument}.
     */
    public Argument() {
        this("");
    }
    
    /**
     * Constructs an {@code Argument} with the specified value.
     * 
     * @param argument the argument
     */
    public Argument(String argument) {
        this.argument = argument;
    }
    
    
    /**
     * Returns {@code true} if this {@code Argument} matches the specified {@code Predicate}; else {@code false}.
     * 
     * @param match the predicate to test this Argument
     * @return true if this Argument matches the specified Predicate; else false
     */
    public boolean match(Predicate<String> match) {
        return match.test(argument);
    }
    
    
    /**
     * Returns the value of this {@code Argument}.
     * 
     * @return the value
     */
    public String asText() {
        return argument;
    }
    
    /**
     * Returns the value produced by applying the specified {@code Function} to the value of this {@code Argument},
     * or {@code null} if the specified {@code Function} is unable to produce a value.
     * 
     * @param <T> the type of the value produced by the specified function
     * @param type the function which produces a value
     * @return the value produced by the specified function if it is able to produce a value; else null
     */
    public @Nullable <T> T as(Function<String, T> type) {
        return type.apply(argument);
    }
    
    /**
     * Returns the value produced by applying the specified {@code Function} to the value of this {@code Argument},
     * or the specified {@code value} if the specified {@code Function} is unable to produce a value.
     * 
     * @param <T> the type of the value produced by the specified function
     * @param type the function which produces a value
     * @param value the default value
     * @return the value produced by the specified function if it is able to produce a value; else the specified value
     */
    public <T> T asOrDefault(Function<String, T> type, T value) {
        return Get.orDefault(type.apply(argument), value);
    }
    
    /**
     * Returns the value produced by applying the specified {@code Function} to the value of this {@code Argument},
     * or throws the exception produced by the specified {@code Supplier} if the specified {@code Function} is 
     * unable to produce a value.
     * 
     * @param <T> the type of the value produced by the specified function
     * @param <E> the type of exception to throw
     * @param type the type of the value produced by the specified function
     * @param exception the specified object if non-null
     * @return the value produced by the specified function if it is able to produce a value
     * @throws E if the specified function fails to produce a value
     */
    public <T, E extends RuntimeException> @Nonnull T asOrThrow(Function<String, T> type, Supplier<E> exception) {
        return Get.orThrow(type.apply(argument), exception);
    }
    
    
    /**
     * Sets the value of this {@code Argument}.
     * 
     * @param argument the value
     */
    public void set(String argument) {
        this.argument = argument;
    }
    
}
