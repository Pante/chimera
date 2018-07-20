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


public class Method extends Typeable {
    
    private Sequence<Parameterized> parameterized;
    private Sequence<Variable> parameters;
    private Sequence<Identifier> exceptions;
    
    
    public Method(Modifiers modifiers, Set<Modifier> values, Sequence<Parameterized> parameterized, Identifier type, Class<?> value, Matcher name, Sequence<Variable> parameters, Sequence<Identifier> exceptions) {
        super(modifiers, values, type, value, name);
        this.parameterized = parameterized;
        this.parameters = parameters;
        this.exceptions = exceptions;
    }
    
    
    @Override
    public Boolean visitMethod(MethodTree tree, Void empty) {
        return modifiers(tree.getModifiers()) && parameterized.match(tree.getTypeParameters()) && type(tree.getReturnType()) 
            && name(tree.getName()) && parameters.match(tree.getParameters()) && exceptions.match(tree.getThrows());
    }
    
}
