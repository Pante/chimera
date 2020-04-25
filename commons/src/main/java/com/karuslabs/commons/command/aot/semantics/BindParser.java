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

import com.karuslabs.annotations.processor.Filter;

import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.annotations.Bind;
import com.karuslabs.commons.command.aot.ir.IR;
import com.karuslabs.commons.command.aot.ir.IR;
import com.karuslabs.commons.command.aot.lexers.Lexer;

import java.util.List;
import javax.lang.model.element.Element;

import static com.karuslabs.commons.command.aot.Messages.reason;


public class BindParser extends Parser {
    
    private Lexer lexer;
    
    
    public BindParser(Environment environment, Lexer lexer) {
        super(environment);
        this.lexer = lexer;
    }

    
    @Override
    public void parse(Element element) {
        environment.initialize(element);
        var scope = environment.scopes.get(element.accept(Filter.CLASS, null));
        
        if (scope == null) {
            environment.error("Invalid @Bind annotation, enclosing class must be annotated with @Command");
            return;
        }
        
        var bindings = element.getAnnotation(Bind.class).value();
        if (bindings.length == 0) {
            environment.error("Invalid @Bind annotation, @Bind cannot be empty");
            return;
        }
        
        for (var binding : bindings) {
            var tokens = lexer.lex(environment, binding, binding);
            if (tokens.size() > 1) {
                parse(scope, element, tokens);
                
            } else if (tokens.size() == 1) {
                parse(scope, element, tokens.get(0));
            } 
        }
    }
    
    void parse(IR root, Element element, List<Token> tokens) {
        var current = root;
        Token binding = null;
        
        for (var token : tokens) {
            var ir = current.children.get(token.lexeme);
            if (ir == null) {
                return;
            }
            
            current = ir;
            binding = token;
        }
        
        bind(current, element, binding);
    }
    
    void parse(IR ir, Element element, Token binding) {
        var declaration = ir.declaration;
        if (declaration != null && declaration.lexeme.equals(binding.lexeme) && declaration.type == binding.type) {
            bind(ir, element, binding);
        }
        
        for (var child : ir.children.values()) {
            parse(child, element, binding);
        }
    }
    
    
    void bind(IR ir, Element element, Token binding) {
        var existing = ir.bindings.get(element);
        if (existing == null) {
            ir.bindings.put(element, binding);
            
        } else {
            environment.error(reason("Invalid binding", binding, "binding already exists"));
        }
    }

}
