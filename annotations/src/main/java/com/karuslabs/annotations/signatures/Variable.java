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

import com.sun.source.tree.VariableTree;

import java.util.Set;
import java.util.regex.*;
import javax.annotation.Nullable;
import javax.lang.model.element.Modifier;

import static java.util.Collections.EMPTY_SET;
import static java.util.regex.Pattern.LITERAL;
import static javax.lang.model.element.Modifier.*;


public class Variable extends Typeable {
    
    private static final Variable ANY = new Variable(Modifiers.ANY, EMPTY_SET, Identifier.any(), null, null);
     
    
    public static Variable any() {
        return ANY;
    }
    
    
    public static Variable of(Modifiers modifiers, Set<Modifier> values, Identifier type, Class<?> value) {
        return new Variable(modifiers, values, type, value, null);
    }
    
    public static Variable of(Modifiers modifiers, Set<Modifier> values, Identifier type, Class<?> value, String literal) {
        return new Variable(modifiers, values, type, value, Pattern.compile(literal, LITERAL).matcher(""));
    }
    
    
    public static Variable of(Identifier type, Class<?> value) {
        return of(type, value, (Pattern) null);
    }
        
    public static Variable of(Identifier type, Class<?> value, String literal) {
        return of(type, value, Pattern.compile(literal, LITERAL));
    }
    
    public static Variable of(Identifier type, Class<?> value, @Nullable Pattern pattern) {
        return new Variable(Modifiers.ANY, EMPTY_SET, type, value, pattern != null ? pattern.matcher("") : null);
    }
    
    
    public Variable(Modifiers modifiers, Set<Modifier> values, Identifier type, @Nullable Class<?> value, @Nullable Matcher name) {
        super(modifiers, values, type, value, name);
    }
    
    
    @Override
    public Boolean visitVariable(VariableTree tree, Void empty) {
        return modifiers(tree.getModifiers()) && type(tree.getType()) && name(tree.getName());
    }
    
    
    public static VariableBuilder builder() {
        return new VariableBuilder(new Variable(Modifiers.ANY, EMPTY_SET, Identifier.any(), null, null));
    }
    
    
    public static class VariableBuilder extends TypeableBuilder<VariableBuilder, Variable> {

        VariableBuilder(Variable signature) {
            super(signature);
        }

        @Override
        protected VariableBuilder self() {
            return this;
        }
        
    }
    
}
