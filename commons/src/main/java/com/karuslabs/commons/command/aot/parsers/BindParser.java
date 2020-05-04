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
package com.karuslabs.commons.command.aot.parsers;

import com.karuslabs.annotations.processor.Filter;

import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.annotations.Bind;
import com.karuslabs.commons.command.aot.lexers.Lexer;
import com.karuslabs.commons.command.aot.resolvers.Resolver;

import java.util.List;
import javax.lang.model.element.*;


public class BindParser extends Parser {
    
    Resolver<ExecutableElement> method;
    Resolver<VariableElement> variable;
    
    
    public BindParser(Environment environment, Lexer lexer, Resolver<ExecutableElement> method, Resolver<VariableElement> variable) {
        super(environment, lexer);
        this.method = method;
        this.variable = variable;
    }

    
    @Override
    public void parse(Element element) {
        var root = environment.scopes.get(element.accept(Filter.CLASS, null));
        if (root == null) {
            environment.error(element, "Enclosing class must be annotated with @Command");
            return;
        }
        
        var bindings = element.getAnnotation(Bind.class).value();
        if (bindings.length == 0) {
            environment.error(element, "@Bind annotation cannot be empty");
            return;
        }
        
        for (var binding : bindings) {
            var tokens = lexer.lex(environment, element, binding);
            if (tokens.size() == 1 && matchAny(root, element, tokens.get(0)) == 0) {
                environment.error(element, "'" + tokens.get(0).literal + "' does not exist");

            } else if (tokens.size() > 1) {
                matchExact(root, element, tokens);
            }
        }
    }
    
    
    int matchAny(Token current, Element element, Token binding) {
        int matches = 0;
        if (current.lexeme.equals(binding.lexeme) && current.type == binding.type) {
            resolve(current, element, binding);
            matches++;
        }

        for (var child : current.children.values()) {
            matches += matchAny(child, element, binding);
        }
        
        return matches;
    }
    
    void matchExact(Token current, Element element, List<Token> bindings) {
        for (var binding : bindings) {
            current = current.children.get(binding.lexeme);
            if (current == null) {
                return;
            }
        }
        
        resolve(current, element, bindings.get(bindings.size() - 1));
    }
    
    
    void resolve(Token token, Element element, Token binding) {
        if (element instanceof ExecutableElement) {
             method.resolve(token, (ExecutableElement) element, binding);
            
        } else if (element instanceof VariableElement) {
            variable.resolve(token, (VariableElement) element, binding);
            
        } else {
            environment.error(element, "@Bind annotation cannot be applied here");
        }
    }

}
