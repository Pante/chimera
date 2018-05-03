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

import java.util.Set;
import javax.lang.model.element.Modifier;


import static java.util.Collections.EMPTY_SET;


public class ConstructorSignature extends Signature<MethodTree> {
    
    private Expectations<TypeParameterTree> generics;
    private Expectations<VariableTree> parameters;
    private Expectations<Tree> exceptions;
    
    
    public ConstructorSignature(Set<Modifier> modifiers, Modifiers condition, Expectations<TypeParameterTree> generics, Expectations<VariableTree> parameters, Expectations<Tree> exceptions) {
        super(modifiers, condition);
        this.generics = generics;
        this.parameters = parameters;
        this.exceptions = exceptions;
    }

    
    @Override
    public boolean test(MethodTree tree) {
        return condition.match(modifiers, tree.getModifiers().getFlags()) && tree.getReturnType() == null 
            && tree.getName().contentEquals("<init>") && generics.check(tree.getTypeParameters())
            && parameters.check(tree.getParameters()) && exceptions.check(tree.getThrows());
    }
    
    
    public static ConstructorBuilder builder() {
        return new ConstructorBuilder(new ConstructorSignature(EMPTY_SET, Modifiers.ANY, Expectations.ANY, Expectations.ANY, Expectations.ANY));
    }
    
    public static class ConstructorBuilder extends Builder<ConstructorBuilder, ConstructorSignature> {

        private ConstructorBuilder(ConstructorSignature signature) {
            super(signature);
        }
        
        
        public ConstructorBuilder generics(Expectations<TypeParameterTree> generics) {
            signature.generics = generics;
            return this;
        }
        
        public ConstructorBuilder parameters(Expectations<VariableTree> parameters) {
            signature.parameters = parameters;
            return this;
        }
        
        public ConstructorBuilder raise(Expectations<Tree> exceptions) {
            signature.exceptions = exceptions;
            return this;
        }
        
        
        @Override
        protected ConstructorBuilder getThis() {
            return this;
        }
    
    }
    
}
