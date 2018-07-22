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


public abstract class Generic extends Identifier implements And<Identifier, Class<?>> {
    
    static final Identifier[] TYPES = new Identifier[] {};
    static final Class<?>[] CLASSES = new Class<?>[] {};
    static final Identifier RAW = new Raw();
    
    
    public static And<Identifier, Class<?>> of(Identifier... types) {
        return new ExactGeneric(types, CLASSES);
    }
    
    public static And<Identifier, Class<?>> parentOf(Identifier... types) {
        return new ParentGeneric(types, CLASSES);
    }
    
    public static And<Identifier, Class<?>> subclassOf(Identifier... types) {
        return new SubclassGeneric(types, CLASSES);
    }
    
    public static Identifier raw() {
        return RAW;
    }
    
    
    protected Identifier[] types;
    protected Class<?>[] classes;

    
    public Generic(Identifier[] types, Class<?>[] classes) {
        this.types = types;
        this.classes = classes;
    }

        
    @Override
    public Identifier and(Class<?>... classes) {
        if (types.length != classes.length) {
            throw new IllegalArgumentException("Invalid number of classes specified, number of types and classes must be the same");
        }
        
        this.classes = classes;
        return this;
    }
    
    @Override
    public Identifier get() {
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


class ExactGeneric extends Generic {

    ExactGeneric(Identifier[] types, Class<?>[] classes) {
        super(types, classes);
    }

    @Override
    public Boolean visitParameterizedType(ParameterizedTypeTree tree, Class<?> expected) {
        return tree.getType().accept(Identifier.EXACT, expected) && parameters(tree.getTypeArguments());
    }

    @Override
    public Boolean visitTypeParameter(TypeParameterTree tree, Class<?> expected) {
        return tree.getName().contentEquals(expected.getName()) && parameters(tree.getBounds());
    }

}

class ParentGeneric extends Generic {

    ParentGeneric(Identifier[] types, Class<?>[] classes) {
        super(types, classes);
    }

    @Override
    public Boolean visitParameterizedType(ParameterizedTypeTree actual, Class<?> expected) {
        return actual.getType().accept(Identifier.FROM, expected) && parameters(actual.getTypeArguments());
    }

    @Override
    public Boolean visitTypeParameter(TypeParameterTree actual, Class<?> expected) {
        return ParentIdentifier.check(actual.getName(), expected) && parameters(actual.getBounds());
    }

}
    
class SubclassGeneric extends Generic {

    SubclassGeneric(Identifier[] types, Class<?>[] classes) {
        super(types, classes);
    }

    @Override
    public Boolean visitParameterizedType(ParameterizedTypeTree tree, Class<?> expected) {
        return tree.getType().accept(Identifier.TO, expected) && parameters(tree.getTypeArguments());
    }

    @Override
    public Boolean visitTypeParameter(TypeParameterTree tree, Class<?> expected) {
        return SubclassIdentifier.check(tree.getName(), expected) && parameters(tree.getBounds());
    }

}

class Raw extends Generic {

    public Raw() {
        super(TYPES, CLASSES);
    }

    @Override
    public Boolean visitParameterizedType(ParameterizedTypeTree tree, Class<?> expected) {
        return tree.getType().accept(Identifier.EXACT, expected) && tree.getTypeArguments().isEmpty();
    }

    @Override
    public Boolean visitTypeParameter(TypeParameterTree tree, Class<?> expected) {
        return tree.getName().contentEquals(expected.getName()) && tree.getBounds().isEmpty();
    }

}
