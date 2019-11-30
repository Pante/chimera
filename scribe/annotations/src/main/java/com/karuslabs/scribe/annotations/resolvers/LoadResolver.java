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

import com.karuslabs.scribe.annotations.Load;
import com.karuslabs.scribe.annotations.processor.Resolver;

import java.util.Map;
import java.util.regex.Matcher;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;

import static javax.tools.Diagnostic.Kind.ERROR;


/**
 * A resolver that transforms a {@link Load} annotation into plugin load ordering
 * related key-value pairs.
 * <br>
 * <br>
 * The following constraints are enforced:
 * <ul>
 * <li>A plugin name contain only alphanumeric and underscore ({@code _}) characters</li>
 * </ul>
 */
public class LoadResolver extends Resolver {
    
    private Matcher matcher;
    
    
    /**
     * Creates a {@code LoadResolver} with the given messager.
     * 
     * @param messager the messager
     */
    public LoadResolver(Messager messager) {
        super(messager);
        matcher = WORD.matcher("");
    }

    
    /**
     * Processes and adds this element to the given results.
     * 
     * @param element the element
     * @param results the results
     */
    @Override
    protected void resolve(Element element, Map<String, Object> results) {
        var load = element.getAnnotation(Load.class);
        
        results.put("load", load.during().toString());
        
        check(element, load.before());
        results.put("loadbefore", load.before());
        
        check(element, load.optionallyAfter());
        results.put("softdepend", load.optionallyAfter());
        
        check(element, load.after());
        results.put("depend", load.after());
    }
    
    /**
     * Determines if the given plugin names satisfy the constraints.
     * 
     * @param element the element
     * @param names the plugin names
     */
    protected void check(Element element, String[] names) {
        for (var name : names) {
            if (!matcher.reset(name).matches()) {
                messager.printMessage(
                    ERROR, 
                    "Invalid name: '" + name + "', name must contain only alphanumeric characters and '_'", 
                    element
                );
            }
        }
    }
    
}
