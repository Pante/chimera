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

import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.Token.Type;
import com.karuslabs.commons.command.aot.ir.*;

import javax.lang.model.util.SimpleElementVisitor9;

import static com.karuslabs.commons.command.aot.Messages.reason;


public class TypeAnalyzer {
    
    protected Environment environment;
    protected Visitor visitor;
    
    
    public TypeAnalyzer(Environment environment) {
        this.environment = environment;
    }


    public void analyze() {
        for (var root : environment.scopes.values()) {
            for (var child : root.children.values()) {
                if (child instanceof Root) {
                    throw new IllegalStateException("Root cannot contain root IRs");

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
            entry.getKey().accept(visitor, entry.getValue());
        }
        
        for (var child : ir.children.values()) {
            analyze(child);
        }
    }
    
    
    static class Visitor extends SimpleElementVisitor9<Void, Token> {
        
    }

}
