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
package com.karuslabs.commons.command.aot;

import java.util.Collection;

import static com.karuslabs.annotations.processor.Messages.format;


/**
 * An analyzer that analyzes the ASTs built by parsers in {@link com.karuslabs.commons.command.aot.parsers}.
 */
public class Analyzer {
    
    private Environment environment;
  
    
    /**
     * Creates an {@code Analyzeer} with the given environment.
     * 
     * @param environment the environment
     */
    public Analyzer(Environment environment) {
        this.environment = environment;
    }

    
    /**
     * Recursively analyzes the AST for each scope in the environment.
     */
    public void analyze() {
        analyze(environment.scopes.values());
    }
    
    void analyze(Collection<Token> tokens) {
        for (var token : tokens) {
            analyze(token.children.values());
            if (token.type == Type.ARGUMENT && !token.bindings.containsKey(Binding.TYPE)) {
                environment.error(token.location, format(token, "is an invalid argument", "an ArgumentType<?> should be bound to it"));
            }
        }
    }
    
}
