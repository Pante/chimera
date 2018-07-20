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

import com.sun.source.tree.*;

import java.util.*;
import java.util.regex.Matcher;
import javax.lang.model.element.Modifier;


public class Type extends Signature {
    
    private Matcher name;
    private Sequence<Parameterized> parameters;
    private Identifier parent;
    private Class<?> value;
    private Sequence<Identifier> interfaces;
    
    private Sequence<Variable> fields;
    private Sequence<Constructor> constructors;
    private Sequence<Method> methods;
    
    
    public Type(Modifiers modifiers, Set<Modifier> values, Matcher name, Sequence<Parameterized> parameters, Identifier parent, Class<?> value, Sequence<Identifier> interfaces, Sequence<Variable> fields, Sequence<Constructor> constructors, Sequence<Method> methods) {
        super(modifiers, values);
        this.name = name;
        this.parameters = parameters;
        this.parent = parent;
        this.value = value;
        this.interfaces = interfaces;
        this.fields = fields;
        this.constructors = constructors;
        this.methods = methods;
    }
    
    
    @Override
    public Boolean visitClass(ClassTree tree, Void empty) {
        return modifiers(tree.getModifiers()) && name.reset(tree.getSimpleName()).matches() && parameters.match(tree.getTypeParameters())
            && parent.visit(tree.getExtendsClause(), value) && interfaces.match(tree.getImplementsClause()) && members(tree.getMembers());
    }
    
    public boolean members(List<? extends Tree> members) {
        return fields.match(members) && constructors.match(members) && methods.match(members);
    }
    
}
