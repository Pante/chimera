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

import com.karuslabs.commons.annotation.*;

import java.util.function.*;

import static java.util.Arrays.copyOfRange;


/**
 * Represents the arguments for a {@code Command}.
 */
public class Arguments {
    
    private static final String[] EMPTY = new String[] {};
    
    
    private String[] arguments;
    private Matcher matcher;
    private Argument mutable;
    
    
    /**
     * Constructs an {@code Arguments} with the specified arguments.
     * 
     * @param arguments the arguments
     */
    public Arguments(String... arguments) {
        this.arguments = arguments;
        mutable = new Argument("");
    }
    
    
    /**
     * Returns {@code true} if this {@code Arguments} contains the specified index; else {@code false}.
     * 
     * @param index the index
     * @return true if this Arguments contains the specified index; else false
     */
    public boolean contains(int index) {
        return 0 <= index && index < arguments.length;
    }
        
    
    /**
     * Returns a shared {@code Matcher} for this {@code Arguments}.
     * 
     * @return a Matcher
     */
    public @Shared Matcher match() {        
        if (matcher == null) {
            matcher = new Matcher(arguments);
            
        } else {
            matcher.set(arguments);
        }
        
        return matcher;
    }
    
    
    /**
     * Returns a shared {@code Argument} for the argument at the specified index,
     * or an empty shared {@code Argument} if the index is out of bounds.
     * 
     * @param index the index
     * @return a shared Argument for the argument at the specified index if this Arguments contains the index; else an empty shared Argument
     */
    public @Shared Argument get(int index) {
        mutable.set(contains(index) ? arguments[index] : "");
        return mutable;
    }
    
    /**
     * Returns a shared {@code Argument} for the last argument,
     * or an empty shared {@code Argument} if this {@code Arguments} is empty.
     * 
     * @return a shared {@code Argument} for the last argument if this Arguments is not empty; else an empty shared Argument
     */
    public @Shared Argument getLast() {
        return get(arguments.length - 1);
    }
    
    
    /**
     * Creates an {@code Argument} for the argument at the specified index,
     * or an empty {@code Argument} if the index is out of bounds.
     * 
     * @param index the index
     * @return an Argument for the argument at the specified index if this Arguments contains the specified index; else an empty Argument
     */
    public Argument getCopy(int index) {
        return getOr(index, Argument::new, Argument::new);
    }
    
    /**
     * Creates an {@code Argument} for the last argument, or an empty {@code Argument} if this {@code Arguments}
     * is empty.
     * 
     * @return an {@code Argument} for the last argument if this Arguments is not empty; else an empty Argument
     */
    public Argument getLastCopy() {
        return getOr(arguments.length - 1, Argument::new, Argument::new);
    }
    
    
    /**
     * Applies the specified {@code Function} to the argument at the specified index, or returns the {@code Argument}
     * produced by the specified {@code Supplier} if the specified index is out of bounds.
     * 
     * @param index the index
     * @param function the function which produces a value for a given argument
     * @param supplier the supplying function which produces a value
     * @return the Argument produced by the specified Function if this Arguments contains the specified index; else the Argument produced by the specified Supplier
     */
    protected Argument getOr(int index, Function<String, Argument> function, Supplier<Argument> supplier) {
        if (contains(index)) {
            return function.apply(arguments[index]);
            
        } else {
            return supplier.get();
        }
    }
    
    
    /**
     * Returns the number of arguments in this {@code Arguments}.
     * 
     * @return the number of arguments in this Arguments
     */
    public int length() {
        return arguments.length;
    }
    
    
    /**
     * Removes the first argument, or does nothing if this {@code Arguments} is empty.
     */
    public void trim() {
        if (arguments.length <= 1) {
            arguments = EMPTY;
            
        } else {
            arguments = copyOfRange(arguments, 1, arguments.length);
        }
    }
    
    
    /**
     * Returns the arguments.
     * 
     * @return the arguments
     */
    public String[] text() {
        return arguments;
    }
    
    /**
     * Sets the arguments.
     * 
     * @param arguments the arguments
     */
    protected void set(String[] arguments) {
        this.arguments = arguments;
    }
    
}
