/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.scribe.core;

import java.util.*;


/**
 * A {@code Resolution} represents the results of annotation resolution.
 * 
 * @param <T> the annotated type
 */
public class Resolution<T> {
    
    /**
     * The mappings that represent a {@code plugin.yml}.
     */
    public final Map<String, Object> mappings;
    /**
     * The messages created during resolution.
     */
    public final List<Message<T>> messages;
    
    /**
     * Creates a {@code Resolution}.
     */
    public Resolution() {
        mappings = new HashMap<>();
        messages = new ArrayList<>();
    }
    
    
    /**
     * Adds an error message with no location.
     * 
     * @param message the error message
     * @return {@code this}
     */
    public Resolution<T> error(String message) {
        return error(null, message);
    }
    
    /**
     * Adds an error message for the given location.
     * 
     * @param location the location
     * @param message the error message
     * @return {@code this}
     */
    public Resolution<T> error(T location, String message) {
        messages.add(Message.error(location, message));
        return this;
    }
    
  
    /**
     * Adds a warning with no location.
     * 
     * @param message the warning
     * @return {@code this}
     */
    public Resolution<T> warning(String message) {
        return warning(null, message);
    }
    
    /**
     * Adds an warning for the given location.
     * 
     * @param location the location
     * @param message the warning
     * @return {@code this}
     */
    public Resolution<T> warning(T location, String message) {
        messages.add(Message.warning(location, message));
        return this;
    }
    
    
    /**
     * Adds an info message with no location.
     * 
     * @param message the info message
     * @return {@code this}
     */
    public Resolution<T> info(String message) {
        return info(null, message);
    }
    
    /**
     * Adds an info message for the given location.
     * 
     * @param location the location
     * @param message the info message
     * @return {@code this}
     */
    public Resolution<T> info(T location, String message) {
        messages.add(Message.info(location, message));
        return this;
    }
    
}
