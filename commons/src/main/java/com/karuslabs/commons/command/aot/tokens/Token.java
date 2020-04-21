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
package com.karuslabs.commons.command.aot.tokens;

import com.karuslabs.commons.command.aot.*;

import java.util.*;
import javax.lang.model.element.Element;

import org.checkerframework.checker.nullness.qual.Nullable;


public abstract class Token {
    
    public static interface Visitor<T, R> extends Agent<R> {

        public default @Nullable R argument(Argument argument, T context) {
            return token(argument, context);
        }
        
        public default @Nullable R literal(Literal literal, T context) {
            return token(literal, context);
        }
        
        public default @Nullable R root(Root root, T context) {
            return token(root, context);
        }
        
        
        public default @Nullable R token(Token token, T context) {
            return null;
        }
        
    }
    
    
    public final @Nullable Element site;
    public final String context;
    public final String value;
    public final String raw;
    public final Map<String, Token> children;
    @Nullable Element execution;
    @Nullable Element suggestions;
    
    
    public Token(Element site, String context, String value, String raw) {
        this.site = site;
        this.context = context;
        this.value = value;
        this.raw = raw;
        children = new HashMap<>();
    }
    
    
    public abstract <T, R> R visit(Visitor<T, R> visitor, T context);
    
    
    public @Nullable Token add(Agent<?> agent, Token child) {
        var existing = children.get(child.value);
        if (existing == null) {
            children.put(child.value, child);
            return child;   
        }
        
        return existing.merge(agent, this);
    }
    
    protected abstract @Nullable Token merge(Agent<?> agent, Token other);
    
    
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
    
    
    @Override
    public String toString() {
        return Messages.value(value, context);
    }
    
}
