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
    
    
    protected Type[] types;
    protected Class<?>[] classes;
    
    
    public ParameterizedType(Type[] types, Class<?>[] classes) {
        this.types = types;
        this.classes = classes;
    }
    
    
    protected boolean parameters(List<? extends Tree> parameters) {
        for (int i = 0; i < types.length; i++) {
            if (parameters.get(i).accept(types[i], classes[i])) {
                return false;
            }
        }

        return true;
    }
    
    @Override
    public ParameterizedType and(Class<?>... classes) {
        this.classes = classes;
        return this;
    }
    
    
    class Exact extends ParameterizedType {

        public Exact(Type[] types, Class<?>[] classes) {
            super(types, classes);
        }
        
        
        @Override
        public Boolean visitParameterizedType(ParameterizedTypeTree tree, Class<?> expected) {
            return types.length == tree.getBounds().size()
        }
        
        @Override
        public Boolean visitTypeParameter(TypeParameterTree tree, Class<?> expected) {
            
        }
            if (!tree.getName().contentEquals(expected) || ) {
                    return false;
                }


    }
    
}



