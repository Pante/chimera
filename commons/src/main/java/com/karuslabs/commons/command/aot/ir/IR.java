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
package com.karuslabs.commons.command.aot.ir;

import com.karuslabs.commons.command.aot.Environment;
import com.karuslabs.commons.command.aot.Token;
import java.util.*;
import javax.lang.model.element.Element;

import org.checkerframework.checker.nullness.qual.Nullable;


public class IR {

    public final Element element;
    public final Token declaration;
    public final Map<Element, Token> bindings;
    public final Map<String, IR> children;
    @Nullable Element execution;
    @Nullable Element type;
    @Nullable Element suggestions;
    
    
    public IR(Element element, Token declaration) {
        this.element = element;
        this.declaration = declaration;
        bindings = new HashMap<>();
        children = new HashMap<>();
    }
    
    
    public @Nullable IR add(Environment environment, Element element, Token declaration) {
        var existing = children.get(declaration.lexeme);
        if (existing == null) {
            var ir = new IR(element, declaration);
            children.put(declaration.lexeme, ir);
            return ir;
        }
        
        return existing.declaration.merge(environment, declaration) ? existing : null;
    }
    
    
    public @Nullable Element execution() {
        return execution;
    }
    
    public boolean execution(Element element) {
        if (execution == null) {
            execution = element;
            return true;
            
        } else {
            return false;
        }
    }
    
    
    public @Nullable Element type() {
        return type;
    }
    
    public boolean type(Element element) {
        if (type == null) {
            type = element;
            return true;
            
        } else {
            return false;
        }
    }
    
    
    public @Nullable Element suggestions() {
        return suggestions;
    }
    
    public boolean suggestions(Element element) {
        if (suggestions == null) {
            suggestions = element;
            return true;
            
        } else {
            return false;
        }
    }
    
}
