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
package com.karuslabs.annotations.signature;

import com.sun.source.tree.Tree;

import java.util.Set;
import javax.annotation.Nullable;
import javax.lang.model.element.Modifier;


public abstract class TypeSignature<T extends Tree> extends Signature<T> {
    
    protected Type type;
    protected @Nullable String name;
    
    
    public TypeSignature(Set<Modifier> modifiers, Modifiers condition, Type type, String name) {
        super(modifiers, condition);
        this.type = type;
        this.name = name;
    }
    
    
    public static abstract class TypeBuilder<GenericBuilder extends TypeBuilder, GenericSignature extends TypeSignature<?>> extends Builder<GenericBuilder, GenericSignature> {
        
        public TypeBuilder(GenericSignature signature) {
            super(signature);
        }
        
        
        public GenericBuilder type(Type type) {
            signature.type = type;
            return getThis();
        }
        
        public GenericBuilder name(String name) {
            signature.name = name;
            return getThis();
        }
        
    }
    
}
