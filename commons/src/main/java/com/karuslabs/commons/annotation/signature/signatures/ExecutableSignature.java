/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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
package com.karuslabs.commons.annotation.signature.signatures;

import com.karuslabs.commons.annotation.signature.*;

import java.util.Set;
import javax.lang.model.element.*;


public abstract class ExecutableSignature<T extends ExecutableSignature> extends TypeSignature<T, ExecutableElement> {
    
    protected VariableSignature[] parameters;
    
    
    public ExecutableSignature(Set<Modifier> modifiers, Type type, Expression expression, VariableSignature[] parameters) {
        super(modifiers, type, expression);
        this.parameters = parameters;
    }

    @Override
    public boolean exact(ExecutableElement element) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public abstract boolean covariant(ExecutableElement element);
    
    
    public VariableSignature[] getParameters() {
        return parameters;
    }
    
}
