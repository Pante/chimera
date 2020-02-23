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

/**
 * A resolver that transforms a {@link Information} annotation into key-value pairs. 
 * <br>
 * <br>
 * The following constraints are enforced:
 * <ul>
 * <li>The existence of a <b>single</b> {@code Information} annotation</li>
 * <li>The URL is valid</li>
 * </ul>
 * 
 * @param <T> the annotated type
 */
public class InformationResolver<T> extends UniqueResolver<T> {

    /**
     * Creates a {@code InformationResolver}.
     */
    public InformationResolver() {
        super(Set.of(Information.class), "Information");
    }
    
    /**
     * Validates, processes and adds the {@code @Information} annotation on {@code type}
     * to {@link #resolution}.
     * 
     * @param type the annotated type
     */
    @Override
    protected void resolve(T type) {
        var information = extractor.single(type, Information.class);
        check(information, type);
        resolve(information);
    }
    
    /**
     * Determines if the URL in {@code information} is valid.
     * 
     * @param information the annotation
     * @param type the annotated type
     */
    protected void check(Information information, T type) {
        var url = information.url();
        if (!url.isEmpty() && !URL.matcher(url).matches()) {
            resolution.error(type, "Invalid URL: " + url + ", " + url + " is not a valid URL");
        }
    }
    
    /**
     * Processes and adds the {@code Information} to {@link #resolution}.
     * Infers the values for {@code authors}, {@code description} and {@code website} 
     * from {@link #project} if present.
     * 
     * @param information the annotation
     */
    protected void resolve(Information information) {
        var mappings = resolution.mappings;
        
        if (information.authors().length > 0) {
            mappings.put("authors", information.authors());
            
        } else if (!project.authors.isEmpty()) {
            mappings.put("authors", project.authors);
        }
        
        if (!information.description().isEmpty()) {
            mappings.put("description", information.description());
            
        } else if (!project.description.isEmpty()) {
            mappings.put("description", project.description);
        }
        
        if (!information.url().isEmpty()) {
            mappings.put("website", information.url());
            
        } else if (!project.url.isEmpty()) {
            mappings.put("website", project.url);
        }
        
        if (!information.prefix().isEmpty()) {
            mappings.put("prefix", information.prefix());
        }
    }

}
