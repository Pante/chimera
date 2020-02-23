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
package com.karuslabs.scribe.core.resolvers;

import com.karuslabs.scribe.annotations.*;

import java.util.Set;


/**
 * A resolver that transforms a {@link API} annotation into a {@code api-version}
 * key-value pair. 
 * <br>
 * <br>
 * The following constraints are enforced:
 * <ul>
 * <li>The existence of a <b>single</b> {@code API} annotation</li>
 * </ul>
 * 
 * @param <T> the annotated type
 */
public class APIResolver<T> extends UniqueResolver<T> {
    
    /**
     * Creates a {@code APIResolver}.
     */
    public APIResolver() {
        super(Set.of(API.class), "API");
    }

    
    /**
     * Processes and adds the {@code API} annotation on {@code type} to {@link #resolution}.
     * Infers the value if {@link API#value} is {@link Version#INFERRED} using; 
     * otherwise defaults to {@link Version#V1_13} if the value cannot be inferred
     * 
     * @param type the annotated type
     */
    @Override
    protected void resolve(T type) {
       var api = extractor.single(type, API.class);
       if (api.value() != Version.INFERRED) {
           resolution.mappings.put("api-version", api.value().version);
           return;
       }
       
       for (var version : Version.values()) {
           if (project.api.startsWith(version.version + ".") || project.api.startsWith(version.version + "-")) {
               resolution.mappings.put("api-version", version.version);
               break;
           }
       }
       
       if (!resolution.mappings.containsKey("api-version")) {
           resolution.mappings.put("api-version", Version.INFERRED.version);
           resolution.warning(type, "Unable to infer 'api-version', defaulting to '" + Version.INFERRED.version + "'");
       }
    }

}
