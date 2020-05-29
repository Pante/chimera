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

import static com.karuslabs.annotations.processor.Messages.format;


/**
 * A parser that resolves the binding of variables and methods to commands.
 */
public class BindParser extends Parser {
    
    Resolver<ExecutableElement> method;
    Resolver<VariableElement> variable;
    
    
    /**
     * Creates a {@code BindParser} with the given parameters.
     * 
     * @param environment the environment
     * @param lexer the lexical analyzer
     * @param method the resolver for methods
     * @param variable the resolver for variables
     */
    public BindParser(Environment environment, Lexer lexer, Resolver<ExecutableElement> method, Resolver<VariableElement> variable) {
        super(environment, lexer);
        this.method = method;
        this.variable = variable;
    }

    
    @Override
    public void parse(Element element) {
        var type = element.accept(Filter.CLASS, null);
        var root = environment.scopes.get(type);
        if (root == null) {
            environment.error(type, "Class should be annotated with @Command");
            return;
        }
        
        var bindings = element.getAnnotation(Bind.class).value();
        if (bindings.length == 0) {
            environment.error(element, "@Bind annotation should not be empty");
            return;
        }
        
        for (var binding : bindings) {
            var tokens = lexer.lex(environment, element, binding);
            if (tokens.size() == 1 && !matchAny(element, root, tokens.get(0))) {
                environment.error(element, format(tokens.get(0), "does not exist"));

            } else if (tokens.size() > 1) {
                match(element, root, tokens);
            }
        }
    }
    
    
    /**
     * Recursively traverses {@code current} and its children. If {@code current}
     * and {@code binding} matches, bind {@code binding} which represents a variable 
     * or method to {@code current}.
     * 
     * @param current the current token that represents a command
     * @param binding the token that represents a variable or method
     * @return {@code true} if any of {@code current} or its children matches
     *         {@code binding}; else {@code false}
     */
    boolean matchAny(Element element, Token current, Token binding) {
        var match = current.lexeme.equals(binding.lexeme) && current.type == binding.type;
        if (match) {
            resolve(element, current, binding.location);
        }
        
        for (var child : current.children.values()) {
            match |= matchAny(element, child, binding);
        }
        
        return match;
    }
    
    
    /**
     * Checks if {@code current} and its children contains a path that matches
     * the sequence of binding tokens. If so, bind the last token in {@code binding}
     * which represents a variable or method to {@code current}.
     * 
     * @param current the current token that represents a command
     * @param bindings the tokens that represent a variable or method
     */
    void match(Element element, Token current, List<Token> bindings) {
        for (var binding : bindings) {
            current = current.children.get(binding.lexeme);
            if (current == null) {
                var command = "";
                for (var part : bindings) {
                    command += part + " ";
                }
                
                environment.error(element, format(command.trim(), "does not exist"));
                return;
            }
        }
        
        resolve(element, current, bindings.get(bindings.size() - 1).location);
    }
    
    
    /**
     * Binds {@code binding} to {@code token} if {@code binding} represents a
     * variable or method.
     * 
     * @param token the token that represents a command
     * @param binding an element that represents a variable or method to bind 
     */
    void resolve(Element element, Token token, Element location) {
        if (element instanceof ExecutableElement) {
            method.resolve((ExecutableElement) element, token, location);
            
        } else if (element instanceof VariableElement) {
            variable.resolve((VariableElement) element, token, location);
            
        } else {
            environment.error(element, "@Bind annotation should not be used on a " + element.getKind().toString());
        }
    }

}
