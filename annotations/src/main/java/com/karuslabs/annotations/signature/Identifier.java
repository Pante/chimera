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
import com.sun.source.util.SimpleTreeVisitor;

import javax.annotation.Nullable;
import javax.lang.model.element.Name;
import javax.lang.model.type.TypeKind;

import static javax.lang.model.type.TypeKind.*;


public abstract class Identifier extends SimpleTreeVisitor<Boolean, Class<?>> {
    
    static final Identifier ANY = new Identifier(true) {};
    static final Identifier NONE = new Identifier(false) {};
    static final Identifier EXACT = new ExactIdentifier();
    static final Identifier FROM = new ParentIdentifier();
    static final Identifier TO = new SubclassIdentifier();
    
    
    public static Identifier any() {
        return ANY;
    }
    
    public static Identifier none() {
        return NONE;
    }
    
    public static Identifier exact() {
        return EXACT;
    }
    
    public static Identifier parentOf() {
        return FROM;
    }
    
    public static Identifier subclassOf() {
        return TO;
    }
    
    
    public Identifier() {
        this(false);
    }
    
    public Identifier(boolean value) {
        super(value);
    }
    
    public @Nullable static TypeKind map(Class<?> primitive) {
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
                return null;
        }
    }

}

class ExactIdentifier extends Identifier {

    @Override
    public Boolean visitIdentifier(IdentifierTree tree, Class<?> expected) {
        return tree.getName().contentEquals(expected.getName());
    }

    @Override
    public Boolean visitPrimitiveType(PrimitiveTypeTree tree, Class<?> expected) {
        return tree.getPrimitiveTypeKind() == map(expected);
    }
}

class ParentIdentifier extends Identifier {

    @Override
    public Boolean visitIdentifier(IdentifierTree tree, Class<?> expected) {
        return check(tree.getName(), expected);
    }
    
    static boolean check(Name actual, Class<?> expected) {
        try {
            return Class.forName(actual.toString()).isAssignableFrom(expected);

        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}

class SubclassIdentifier extends Identifier {

    @Override
    public Boolean visitIdentifier(IdentifierTree tree, Class<?> expected) {
        Generic.of(Identifier.EXACT);
        return check(tree.getName(), expected);
    }

    static boolean check(Name actual, Class<?> expected) {
        try {
            return expected.isAssignableFrom(Class.forName(actual.toString()));

        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}