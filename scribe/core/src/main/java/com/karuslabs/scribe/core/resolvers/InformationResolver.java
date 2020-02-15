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

import com.karuslabs.scribe.annotations.Information;

import java.util.Set;


public class InformationResolver<T> extends UniqueResolver<T> {

    public InformationResolver() {
        super(Set.of(Information.class), "Information");
    }
    
    @Override
    protected void resolve(T type) {
        var information = extractor.single(type, Information.class);
        check(information, type);
        resolve(information);
    }
    
    protected void check(Information information, T type) {
        var url = information.url();
        if (!url.isEmpty() && !URL.matcher(url).matches()) {
            resolution.error(type, "Invalid URL: " + url + ", " + url + " is not a valid URL");
        }
    }
    
    protected void resolve(Information information) {
        var mapping = resolution.mapping;
        
        if (information.authors().length > 0) {
            mapping.put("authors", information.authors());
            
        } else if (!project.authors.isEmpty()) {
            mapping.put("authors", project.authors);
        }
        
        if (!information.description().isEmpty()) {
            mapping.put("description", information.description());
            
        } else if (!project.description.isEmpty()) {
            mapping.put("description", project.description);
        }
        
        if (!information.url().isEmpty()) {
            mapping.put("website", information.url());
            
        } else if (!project.url.isEmpty()) {
            mapping.put("description", project.url);
        }
        
        if (!information.prefix().isEmpty()) {
            mapping.put("prefix", information.prefix());
        }
    }

}
