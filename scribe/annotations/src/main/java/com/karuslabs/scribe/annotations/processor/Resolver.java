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
package com.karuslabs.scribe.annotations.processor;

import java.util.*;
import java.util.regex.Pattern;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;


/**
 * A resolver that transforms an annotation into parts of a {@code plugin.yml}.
 */
public abstract class Resolver {
    
    /**
     * A pattern that command aliases and names must adhere.
     */
    public static final Pattern COMMAND = Pattern.compile("(.*\\s+.*)");
    /**
     * A pattern that permissions should adhere.
     */
    public static final Pattern PERMISSION = Pattern.compile("\\w+(\\.\\w+)*(.\\*)?");
    /**
     * A pattern corresponding to SemVer 2.0.0 that versions should adhere.
     */
    public static final Pattern VERSIONING = Pattern.compile("(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(-[a-zA-Z\\d][-a-zA-Z.\\d]*)?(\\+[a-zA-Z\\d][-a-zA-Z.\\d]*)?$");
    /**
     * A pattern that represents a word.
     */
    public static final Pattern WORD = Pattern.compile("\\w+");
    
    /**
     * A messager used to emit warnings and errors.
     */
    protected Messager messager;
    
    
    /**
     * Creates a {@code Resolver} with the given {@code messager}.
     * 
     * @param messager the messager
     */
    public Resolver(Messager messager) {
        this.messager = messager;
    }
    
    
    /**
     * Processes and adds the elements to the given results.
     * <br>
     * <br>
     * <b>Default implementation: </b>
     * <br>
     * Delegates validation of elements to {@link #check(Set)}. If all elements are 
     * valid, delegates processing of each element to {@link #resolve(Element, Map)}.
     * After which, delegates cleaning up to {@link #clear()}.
     * 
     * @param elements the elements to be resolved
     * @param results the results of this resolution
     */
    public void resolve(Set<? extends Element> elements, Map<String, Object> results) {
        if (!check(elements)) {
            return;
        }
        
        for (var element : elements) {
            resolve(element, results);
        }
        
        clear();
    }
    
    /**
     * Validates the given elements.
     * <br>
     * <br>
     * <b>Default implementation: </b>
     * <br>
     * Always returns {@code true}.
     * 
     * @param elements the elements to be validated
     * @return {@code true} if the given elements are valid; else {@code false}
     */
    protected boolean check(Set<? extends Element> elements) {
        return true;
    }
    
    /**
     * Processes and adds the element to the given results.
     * 
     * @param element the element
     * @param results the results
     */
    protected abstract void resolve(Element element, Map<String, Object> results);
    
    /**
     * Releases acquired resources and resets this resolver to its initial state.
     * <br>
     * <br>
     * <b>Default implementation: </b>
     * <br>
     * Does nothing.
     */
    protected void clear() {
        
    }
    
}
