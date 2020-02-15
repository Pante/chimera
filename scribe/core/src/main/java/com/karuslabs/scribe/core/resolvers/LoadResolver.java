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

import com.karuslabs.scribe.annotations.Load;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.regex.Matcher;


public class LoadResolver<T> extends UniqueResolver<T> {
    
    private Matcher matcher;
    
    
    public LoadResolver(Set<Class<? extends Annotation>> annotations, String name) {
        super(annotations, name);
        matcher = WORD.matcher("Load");
    }

    @Override
    protected void resolve(T type) {
        var load = extractor.single(type, Load.class);
        var mapping = resolution.mapping;
        
        mapping.put("load", load.during().toString());
        
        check(type, load.before());
        mapping.put("loadbefore", load.before());
        
        check(type, load.optionallyAfter());
        mapping.put("softdepend", load.optionallyAfter());
        
        check(type, load.after());
        mapping.put("depend", load.after());
    }
    
    protected void check(T type, String[] names) {
        for (var name : names) {
            if (!matcher.reset(name).matches()) {
                resolution.error(type, "Invalid name: '" + name + "', name must contain only alphanumeric characters and '_'");
            }
        }
    }

}
