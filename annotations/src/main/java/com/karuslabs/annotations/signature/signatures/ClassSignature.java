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

import com.karuslabs.annotations.signature.*;

import com.sun.source.tree.*;

import java.util.*;
import javax.lang.model.element.Modifier;


public class ClassSignature extends Signature<ClassTree> {
    
    private String name;
    private Type parent;
    private Expectations<Tree> implementations;
    private Expectations<TypeParameterTree> generics;
    private Expectations<VariableTree> fields;
    private Expectations<MethodTree> constructors;
    private Expectations<MethodTree> methods;
    private Expectations<ClassTree> nested;
    
    
    public ClassSignature(Set<Modifier> modifiers, Modifiers condition, String name, Type parent, Expectations<Tree> implementations, Expectations<TypeParameterTree> generics, 
                          Expectations<VariableTree> fields, Expectations<MethodTree> constructors, Expectations<MethodTree> methods, Expectations<ClassTree> nested) {
        super(modifiers, condition);
        this.name = name;
        this.parent = parent;
        this.implementations = implementations;
        this.generics = generics;
        this.fields = fields;
        this.constructors = constructors;
        this.methods = methods;
        this.nested = nested;
    }

    
    @Override
    public boolean test(ClassTree tree) {
        return condition.match(modifiers, tree.getModifiers().getFlags()) && tree.getSimpleName().contentEquals(name) 
            && parent.test(tree.getExtendsClause()) && implementations.check(tree.getImplementsClause())
            && generics.check(tree.getTypeParameters()) && test(tree.getMembers());
    }
    
    protected boolean test(List<? extends Tree> members) {
        List<VariableTree> fields = null;
        List<MethodTree> constructors = null;
        List<MethodTree> methods = null;
        List<ClassTree> nested =  null;
        
        for (Tree member : members) {
            if (member instanceof VariableTree) {
                
            } else if (member instanceof MethodTree) {
                MethodTree method = (MethodTree) member;
                if (method.getName().contentEquals("<init>")) {
                    constructors.add(method);
                    
                } else {
                    methods.add(method);
                }
            } else {
                
            }
        }
    }
    
}
