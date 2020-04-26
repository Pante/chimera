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
package com.karuslabs.commons.command.aot.analyzer;

import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.Token.Type;

import javax.lang.model.element.*;

import org.checkerframework.checker.nullness.qual.Nullable;

import static com.karuslabs.commons.command.aot.Messages.reason;


public class Analyzer {
    
    private Environment environment;
    private MethodAnalyzer method;
    private VariableAnalyzer variable;
    
    
    
    public Analyzer(Environment environment, MethodAnalyzer method, VariableAnalyzer variable) {
        this.environment = environment;
        this.method = method;
        this.variable = variable;
    }


    public void analyze() {
        for (var root : environment.scopes.values()) {
            for (var child : root.children.values()) {
                if (child.element == null || child.declaration == null) {
                    throw new IllegalStateException("Root contains invalid IRs");

                } else if (child.declaration.type == Type.ARGUMENT) {
                    environment.initialize(child.element);
                    environment.error(reason("Invalid argument position", child.declaration, "commands must start with a literal"));
                }
                
                analyze(child);
            }
        }
    }
    
    void analyze(IR ir) {        
        for (var entry : ir.bindings.entrySet()) {
            var element = entry.getKey();
            
            if (element instanceof ExecutableElement) {
                method.analyze((ExecutableElement) element, ir, entry.getValue());
                
            } else if (element instanceof VariableElement) {
                variable.analyze((VariableElement) element, ir, entry.getValue());
                
            } else {
                throw new IllegalStateException("No analyzer for: " + element.asType());
            }
        }
        
        for (var child : ir.children.values()) {
            analyze(child);
        }
    }
    
    
    

}
