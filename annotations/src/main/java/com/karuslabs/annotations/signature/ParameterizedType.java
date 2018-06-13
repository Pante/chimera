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

import com.sun.source.tree.*;

import java.util.List;


public abstract class ParameterizedType extends Type implements Parameterized {
    
    static final Type[] TYPES = new Type[] {};
    static final Class<?>[] CLASSES = new Class<?>[] {};
    
    
    public static Parameterized of(Type... types) {
        return new ExactParameterizedType(types, CLASSES);
    }
    
    public static Parameterized from(Type... types) {
        return new AssignableFromParameterizedType(types, CLASSES);
    }
    
    public static Parameterized to(Type... types) {
        return new AssignableToParameterizedType(types, CLASSES);
    }
    
    
    protected Type[] types;
    protected Class<?>[] classes;

    
    public ParameterizedType(Type[] types, Class<?>[] classes) {
        this.types = types;
        this.classes = classes;
    }

        
    @Override
    public ParameterizedType and(Class<?>... classes) {
        if (types.length != classes.length) {
            throw new IllegalArgumentException("Invalid number of classes specified, number of types and classes must be the same");
        }
        
        this.classes = classes;
        return this;
    }
    
    protected boolean parameters(List<? extends Tree> parameters) {
        if (types.length != parameters.size()) {
            return false;
        }
        
        for (int i = 0; i < types.length; i++) {
            if (parameters.get(i).accept(types[i], classes[i])) {
                return false;
            }
        }

        return true;
    }
    
}


class ExactParameterizedType extends ParameterizedType {

    ExactParameterizedType(Type[] types, Class<?>[] classes) {
        super(types, classes);
    }

    @Override
    public Boolean visitParameterizedType(ParameterizedTypeTree tree, Class<?> expected) {
        return tree.getType().accept(Type.EXACT, expected) && parameters(tree.getTypeArguments());
    }

    @Override
    public Boolean visitTypeParameter(TypeParameterTree tree, Class<?> expected) {
        return tree.getName().contentEquals(expected.getName()) && parameters(tree.getBounds());
    }

}

class AssignableFromParameterizedType extends ParameterizedType {

    AssignableFromParameterizedType(Type[] types, Class<?>[] classes) {
        super(types, classes);
    }

    @Override
    public Boolean visitParameterizedType(ParameterizedTypeTree tree, Class<?> expected) {
        return tree.getType().accept(Type.FROM, expected) && parameters(tree.getTypeArguments());
    }

    @Override
    public Boolean visitTypeParameter(TypeParameterTree tree, Class<?> expected) {
        return AssignableToType.check(tree.getName(), expected) && parameters(tree.getBounds());
    }

}
    
class AssignableToParameterizedType extends ParameterizedType {

    AssignableToParameterizedType(Type[] types, Class<?>[] classes) {
        super(types, classes);
    }

    @Override
    public Boolean visitParameterizedType(ParameterizedTypeTree tree, Class<?> expected) {
        return tree.getType().accept(Type.TO, expected) && parameters(tree.getTypeArguments());
    }

    @Override
    public Boolean visitTypeParameter(TypeParameterTree tree, Class<?> expected) {
        return AssignableToType.check(tree.getName(), expected) && parameters(tree.getBounds());
    }

}
