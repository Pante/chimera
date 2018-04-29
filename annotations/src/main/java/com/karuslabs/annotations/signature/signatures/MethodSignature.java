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
package com.karuslabs.annotations.signature.signatures;

import com.karuslabs.annotations.signature.*;

import com.sun.source.tree.MethodTree;

import java.util.*;
import javax.lang.model.element.Modifier;


public class MethodSignature extends TypeSignature<MethodTree> {
    
    protected List<TypeParameter> generics;
    protected List<VariableSignature> parameters;
    protected List<Type> exceptions;
    
    
    public MethodSignature(Set<Modifier> modifiers, Modifiers condition, List<TypeParameter> generics, Type type, String name, List<VariableSignature> parameters, List<Type> exceptions) {
        super(modifiers, condition, type, name);
        this.generics = generics;
        this.parameters = parameters;
        this.exceptions = exceptions;
    }

    
    @Override
    public boolean test(MethodTree tree) {
        if (!condition.match(modifiers, tree.getModifiers().getFlags()) || !type.test(tree.getReturnType())) {
            return false;
        }
        
        return true;
    }
    
    
    public static class MethodBuilder extends TypeBuilder<MethodBuilder, MethodSignature> {

        public MethodBuilder(MethodSignature signature) {
            super(signature);
        }
        
        
        public MethodBuilder generics(TypeParameter... parameters) {
            signature.generics = List.of(parameters);
            return this;
        }
        
        public MethodBuilder parameters(VariableSignature... parameters) {
            signature.parameters = List.of(parameters);
            return this;
        }
        
        public MethodBuilder raise(Type... exceptions) {
            signature.exceptions = List.of(exceptions);
            return this;
        }
        

        @Override
        protected MethodBuilder getThis() {
            return this;
        }
        
    }
    
}
