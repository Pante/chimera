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
import java.util.regex.*;
import javax.annotation.Nullable;
import javax.lang.model.element.*;


public abstract class Typeable extends Signature {
    
    Identifier type;
    Class<?> value;

    
    public Typeable(Modifiers modifiers, Set<Modifier> values, Identifier type, @Nullable Class<?> value, @Nullable Matcher name) {
        super(modifiers, values, name);
        this.type = type;
        this.value = value;
    }
    
    
    public boolean type(Tree tree) {
        return type.visit(tree, value);
    }
    
    
    public static abstract class TypeableBuilder<GenericBuilder extends TypeableBuilder, GenericSignature extends Typeable> extends Builder<GenericBuilder, GenericSignature> {
        
        public TypeableBuilder(GenericSignature signature) {
            super(signature);
        }
        
        
        public TypeableBuilder type(Identifier matcher, Class<?> type) {
            signature.type = matcher;
            signature.value = type;
            return self();
        }
        
    }
    
}
