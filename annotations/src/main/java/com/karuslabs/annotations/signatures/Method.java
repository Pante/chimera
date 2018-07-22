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
package com.karuslabs.annotations.signatures;

import com.karuslabs.annotations.signature.*;

import com.sun.source.tree.MethodTree;

import java.util.Set;
import java.util.regex.Matcher;
import javax.lang.model.element.Modifier;

import static java.util.Collections.EMPTY_SET;


public class Method extends Typeable {
    
    Sequence<Generic> generics;
    Sequence<Variable> parameters;
    Sequence<Identifier> thrown;
    
    
    public Method(Modifiers modifiers, Set<Modifier> values, Sequence<Generic> generics, Identifier type, Class<?> value, Matcher name, Sequence<Variable> parameters, Sequence<Identifier> thrown) {
        super(modifiers, values, type, value, name);
        this.generics = generics;
        this.parameters = parameters;
        this.thrown = thrown;
    }
    
    
    @Override
    public Boolean visitMethod(MethodTree tree, Void empty) {
        return modifiers(tree.getModifiers()) && generics.match(tree.getTypeParameters()) && type(tree.getReturnType()) 
            && name(tree.getName()) && parameters.match(tree.getParameters()) && thrown.match(tree.getThrows());
    }
    
    
    public static MethodBuilder builder() {
        return new MethodBuilder(new Method(Modifiers.ANY, EMPTY_SET, Sequence.any(), Identifier.any(), null, null, Sequence.any(), Sequence.any()));
    }
    
    public static class MethodBuilder extends ExecutableBuilder<MethodBuilder, Method> {

        MethodBuilder(Method signature) {
            super(signature);
        }

        @Override
        protected MethodBuilder self() {
            return this;
        }
        
    }
    
    public static abstract class ExecutableBuilder<Builder extends ExecutableBuilder, Executable extends Method> extends TypeableBuilder<Builder, Executable> {

        public ExecutableBuilder(Executable signature) {
            super(signature);
        }

        
        public Builder generics(Sequence<Generic> generics) {
            signature.generics = generics;
            return self();
        }
        
        public Builder parameters(Sequence<Variable> parameters) {
            signature.parameters = parameters;
            return self();
        }
        
        public Builder thrown(Sequence<Identifier> exceptions) {
            signature.thrown = exceptions;
            return self();
        }
        
    }
    
}
