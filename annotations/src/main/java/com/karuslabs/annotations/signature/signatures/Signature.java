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

import com.karuslabs.annotations.signature.Modifiers;
import com.sun.source.tree.Tree;
    
import java.util.*;
import java.util.function.Predicate;
import javax.lang.model.element.Modifier;


public abstract class Signature<T extends Tree> implements Predicate<T> {
    
    protected Set<Modifier> modifiers;
    protected Modifiers condition;
    
    
    public Signature(Set<Modifier> modifiers, Modifiers condition) {
        this.modifiers = modifiers;
        this.condition = condition;
}
    
    
    public static abstract class Builder<GenericBuilder extends Builder, GenericSignature extends Signature<?>> {
        
        protected GenericSignature signature;
        
        
        public Builder(GenericSignature signature) {
            this.signature = signature;
        }     
        
        
        public GenericBuilder modifiers(Modifiers condition, Modifier first, Modifier... rest) {
            signature.condition = condition;
            signature.modifiers = EnumSet.of(first, rest);
            return getThis();
        }
        
        public GenericBuilder modifiers(Modifier first, Modifier... rest) {
            signature.modifiers = EnumSet.of(first, rest);
            return getThis();
        }
        
        public GenericBuilder contains(Modifiers condition) {
            signature.condition = condition;
            return getThis();
        }
        
        public GenericSignature build() {
            return signature;
        }
        
        
        protected abstract GenericBuilder getThis(); 
        
    }
    
}
