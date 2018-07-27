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

import com.sun.source.tree.ModifiersTree;
import com.sun.source.util.SimpleTreeVisitor;

import java.util.Set;
import java.util.regex.*;
import javax.lang.model.element.*;

import static java.util.regex.Pattern.LITERAL;


public abstract class Signature extends SimpleTreeVisitor<Boolean, Void> {
    
    Modifiers modifiers;
    Set<Modifier> values;
    Matcher name;
    
    
    public Signature(Modifiers modifiers, Set<Modifier> values, Matcher name) {
        super(false);
        this.modifiers = modifiers;
        this.values = values;
        this.name = name;
    }
    
    
    public boolean modifiers(ModifiersTree tree) {
        return modifiers.match(values, tree.getFlags());
    }
    
    public boolean name(Name name) {
        return this.name == null || this.name.reset(name).matches();
    }
    
    
    public static abstract class Builder<GenericBuilder extends Builder, GenericSignature extends Signature> {
        
        protected GenericSignature signature;
        
        
        public Builder(GenericSignature signature) {
            this.signature = signature;
        }
        
        
        public GenericBuilder modifiers(Modifiers matcher, Set<Modifier> modifiers) {
            signature.modifiers = matcher;
            signature.values = modifiers;
            return self();
        }
        
        public GenericBuilder name(String literal) {
            signature.name = Pattern.compile(literal, LITERAL).matcher("");
            return self();
        }
        
        public GenericBuilder name(Pattern name) {
            signature.name = name.matcher("");
            return self();
        }
        
        public GenericBuilder name(Matcher name) {
            signature.name = name;
            return self();
        }
        
        public GenericSignature build() {
            return signature;
        }
        
        
        protected abstract GenericBuilder self();
        
    }
    
}
