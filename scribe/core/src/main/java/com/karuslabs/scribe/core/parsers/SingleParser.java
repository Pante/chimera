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
package com.karuslabs.scribe.core.parsers;

import com.karuslabs.scribe.core.Environment;

import java.lang.annotation.Annotation;
import java.util.Set;


/**
 * A resolver that determines if only a <b>single</b> supported annotation is present.
 * 
 * @param <T> the annotated type
 */
public abstract class SingleParser<T> extends Parser<T> {

    /**
     * The name of the annotation supported by this resolver.
     */
    protected String name;
    
    
    /**
     * Creates a {@code SingleParser} with the given annotations and name.
     * 
     * @param environment the environment
     * @param annotations the annotations supported by this resolver
     * @param name the name of the annotation supported by this resolver
     */
    public SingleParser(Environment environment, Set<Class<? extends Annotation>> annotations, String name) {
        super(environment, annotations);
        this.name = name;
    }

    
    /**
     * Determines if only only a <b>single</b> annotated type is present.
     * 
     * @param types the annotated types
     */
    @Override
    protected void check(Set<T> types) {
        if (types.size() > 1) {
            for (var type : types) {
                environment.error(type, "Project contains " + types.size() + " @" + name + " annotations, should contain one @" + name + " annotation");
            }
        }
    }


}
