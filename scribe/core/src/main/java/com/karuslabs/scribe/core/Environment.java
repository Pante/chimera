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
 * An environment which contains the mappings for the generation of a {@code plugin.yml}.
 * 
 * @param <T> the type of the annotated types
 */
public abstract class Environment<T> {
    
    /**
     * The project.
     */
    public final Project project;
    /**
     * A resolver.
     */
    public final Resolver<T> resolver;
    /**
     * The mappings.
     */
    public final Map<String, Object> mappings;
    

    /**
     * Creates an {@code Environment} with the given parameters.
     * 
     * @param project the project
     * @param resolver the resolver
     */
    public Environment(Project project, Resolver<T> resolver) {
        this.project = project;
        this.resolver = resolver;
        mappings = new HashMap<>();
    }
    
    
    /**
     * Emits an error.
     * 
     * @param message the error
     */
    public abstract void error(String message);
    
    /**
     * Emits a error at the given location.
     * 
     * @param location the location
     * @param message the error
     */
    public abstract void error(T location, String message);
    
        
    /**
     * Emits a warning.
     * 
     * @param message the warning
     */
    public abstract void warn(String message);
    
    /**
     * Emits a warning at the given location.
     * 
     * @param location the location
     * @param message the warning
     */
    public abstract void warn(T location, String message);
    
}
