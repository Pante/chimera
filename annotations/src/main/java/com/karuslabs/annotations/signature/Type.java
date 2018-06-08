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

import java.util.function.Predicate;
import javax.lang.model.type.*;

import static javax.lang.model.type.TypeKind.*;


@FunctionalInterface
public interface Type<T extends Tree> extends Predicate<T> {
    
    public static Type<PrimitiveTypeTree> primitive(Class<?> type) {
        TypeKind expected = map(type);
        return tree -> tree.getPrimitiveTypeKind() == expected;
    }
    
    public static Type<IdentifierTree> of(Class<?> type) {
        String name = type.getName();
        return tree -> tree.getName().contentEquals(name);
    }
    
    public static Type<IdentifierTree> from(Class<?> type) {
        return tree -> {
            try {
                Class<?> actual = Class.forName(tree.getName().toString());
                return actual.isAssignableFrom(type);
                
            } catch (ClassNotFoundException e) {
                return false;
            }
        };       
    }
    
    public static Type<IdentifierTree> to(Class<?> type) {
        return tree -> {
            try {
                Class<?> actual = Class.forName(tree.getName().toString());
                return type.isAssignableFrom(actual);
                
            } catch (ClassNotFoundException e) {
                return false;
            }
        };     
    }
    
    
    
    public static TypeKind map(Class<?> primitive) {
        switch (primitive.getName()) {
            case "boolean":
                return BOOLEAN;
            case "byte":
                return BYTE;
            case "short":
                return SHORT;
            case "int":
                return INT;
            case "long":
                return LONG;
            case "char":
                return CHAR;
            case "float":
                return FLOAT;
            case "double":
                return DOUBLE;
            default:
                throw new IllegalArgumentException("Invalid class, class must be a primitive");
        }
    }
    
}
