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
import javax.lang.model.element.*;

import static java.util.regex.Pattern.LITERAL;


public abstract class Typeable extends Signature {
    
    Identifier type;
    Class<?> value;
    Matcher name;

    
    public Typeable(Modifiers modifiers, Set<Modifier> values, Identifier type, Class<?> value, Matcher name) {
        super(modifiers, values);
        this.type = type;
        this.value = value;
        this.name = name;
    }
    
    
    public boolean type(Tree tree) {
        return type.visit(tree, value);
    }
    
    public boolean name(Name name) {
        return this.name.reset(name).matches();
    }
    
    
    public static abstract class TypeableBuilder<GenericBuilder extends TypeableBuilder, GenericSignature extends Typeable> extends Builder<GenericBuilder, GenericSignature> {
        
        public TypeableBuilder(GenericSignature signature) {
            super(signature);
        }
        
        
        public TypeableBuilder type(Identifier matcher, Class<?> type) {
            signature.type = matcher;
            signature.value = type;
            return getThis();
        }
        
        public TypeableBuilder name(String literal) {
            signature.name = Pattern.compile(literal, LITERAL).matcher("");
            return getThis();
        }
        
        public TypeableBuilder name(Pattern name) {
            signature.name = name.matcher("");
            return getThis();
        }
        
        public TypeableBuilder name(Matcher name) {
            signature.name = name;
            return getThis();
        }
        
    }
    
}
