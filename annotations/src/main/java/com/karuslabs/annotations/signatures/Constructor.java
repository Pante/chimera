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

import com.sun.source.tree.Tree;

import java.util.Set;
import javax.lang.model.element.*;


public class Constructor extends Method {
    
    public Constructor(Modifiers modifiers, Set<Modifier> values, Sequence<Generic> generics, Sequence<Variable> parameters, Sequence<Identifier> thrown) {
        super(modifiers, values, generics, null, null, null, parameters, thrown);
    }
    
    
    @Override
    public boolean type(Tree tree) {
        return tree == null;
    }
    
    @Override
    public boolean name(Name name) {
        return name.contentEquals("<init>");
    }
    
    
    public static class ConstructorBuilder extends ExecutableBuilder<ConstructorBuilder, Constructor> {

        ConstructorBuilder(Constructor signature) {
            super(signature);
        }

        @Override
        protected ConstructorBuilder self() {
            return this;
        }
        
    }
    
}
