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
package com.karuslabs.scribe.annotations.resolvers;

import com.karuslabs.scribe.annotations.Information;
import com.karuslabs.scribe.annotations.processor.Resolver;

import java.util.Map;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;


public class InformationResolver extends Resolver {

    public InformationResolver(Messager messager) {
        super(messager);
    }
    

    @Override
    protected void resolve(Element element, Map<String, Object> results) {
        var information = element.getAnnotation(Information.class);
                
        if (information.authors().length > 0) {
            results.put("authors", information.authors());
        }
        
        if (!information.description().isEmpty()) {
            results.put("description", information.description());
        }
        
        if (!information.url().isEmpty()) {
            results.put("website", information.url());
        }
        
        if (!information.prefix().isEmpty()) {
            results.put("prefix", information.prefix());
        }
    }
    
}
