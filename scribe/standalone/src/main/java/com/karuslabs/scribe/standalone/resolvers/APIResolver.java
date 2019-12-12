/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.scribe.standalone.resolvers;

import com.karuslabs.scribe.annotations.API;
import com.karuslabs.scribe.standalone.Resolver;

import java.util.Map;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;


/**
 * A resolver that transforms an {@link API} annotation into a {@code api-version} 
 * key-value pair.
 */
public class APIResolver extends Resolver {
    
    
    /**
     * Creates an {@code APIResolver} with the given messager.
     * 
     * @param messager the messager
     */
    public APIResolver(Messager messager) {
        super(messager);
    }
    
    /**
     * Processes and adds the element to the given results.
     * 
     * @param element the element
     * @param results the results
     */
    @Override
    protected void resolve(Element element, Map<String, Object> results) {
        var api = element.getAnnotation(API.class);
        results.put("api-version", api.value().version);
    }
    
}