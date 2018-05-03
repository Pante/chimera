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
    
import com.sun.source.tree.*;

import java.util.*;
import javax.lang.model.element.Modifier;

import static java.util.Collections.EMPTY_SET;


public class MethodSignature extends TypeSignature<MethodTree> {
    
    private Expectations<TypeParameterTree> generics;
    private Expectations<VariableTree> parameters;
    private Expectations<Tree> exceptions;
    
    
    public MethodSignature(Set<Modifier> modifiers, Modifiers condition, Expectations<TypeParameterTree> generics, Type type, String name, Expectations<VariableTree> parameters, Expectations<Tree> exceptions) {
        super(modifiers, condition, type, name);
        this.generics = generics;
        this.parameters = parameters;
        this.exceptions = exceptions;
    }

    
    @Override
    public boolean test(MethodTree tree) {
        return condition.match(modifiers, tree.getModifiers().getFlags()) && type.test(tree.getReturnType()) 
            && tree.getName().contentEquals(name) && generics.check(tree.getTypeParameters()) 
            && parameters.check(tree.getParameters()) && exceptions.check(tree.getThrows());
    }
    
    
    public static MethodBuilder builder() {
        return new MethodBuilder(new MethodSignature(EMPTY_SET, Modifiers.ANY, Expectations.ANY, Type.any(), "", Expectations.ANY, Expectations.ANY));
    }
    
    
    public static class MethodBuilder extends TypeBuilder<MethodBuilder, MethodSignature> {

        private MethodBuilder(MethodSignature signature) {
            super(signature);
        }
        
        
        public MethodBuilder generics(Expectations<TypeParameterTree> generics) {
            signature.generics = generics;
            return this;
        }
        
        public MethodBuilder parameters(Expectations<VariableTree> parameters) {
            signature.parameters = parameters;
            return this;
        }
        
        public MethodBuilder raise(Expectations<Tree> exceptions) {
            signature.exceptions = exceptions;
            return this;
        }
        

        @Override
        protected MethodBuilder getThis() {
            return this;
        }
        
    }
    
}
