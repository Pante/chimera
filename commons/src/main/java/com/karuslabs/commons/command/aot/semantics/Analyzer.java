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
package com.karuslabs.commons.command.aot.semantics;

import com.karuslabs.commons.command.aot.Token;
import com.karuslabs.commons.command.aot.Token.Type;
import com.karuslabs.commons.command.aot.lexers.Lexer;

import java.util.*;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;

import static javax.tools.Diagnostic.Kind.ERROR;


public abstract class Analyzer implements Lexer.Visitor {
    
    protected Lexer lexer;
    protected Map<Element, Set<Token>> scopes;
    protected Token root;
    protected Messager messager;
    protected Elements elements;
    protected Types types;
    protected @Nullable Element element;
    protected @Nullable Set<Token> scope;
    protected @Nullable Token current;
    
    
    public Analyzer(Lexer lexer, Map<Element, Set<Token>> scopes, Token root, Messager messager, Elements elements, Types types) {
        this.lexer = lexer;
        this.scopes = scopes;
        this.root = root;
        this.messager = messager;
        this.elements = elements;
        this.types = types;
        this.current = root;
    }
    
    
    public abstract void lint(Element element);
    
    
    @Override
    public void error(String message) {
        messager.printMessage(ERROR, message, element);
    }
    
    
    protected void scope(Element element) {
        var scope = scopes.get(element);
        if (scope == null) {
            scopes.put(element, scope = new HashSet<>());
        }
        
        this.scope = scope;
    }
    
    
    protected @Nullable Token child(String context, String name, Type type) {
        var existing = current.children.get(name);
        
        if (existing == null) {
            var token = new Token(name, type, element);
            current.children.put(name, token);
            return token;
            
        } else if (existing.type == type) {
            return existing;
            
        } else {
            error("Invalid command: '" + name  + "' in '" + context + "', command of a different type already exists in '" + existing.element.asType() + "'");
            return null;
        }
    }
    
}
