/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.Satisfactory.type;

import java.util.*;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;

public class TypeMirrors implements Types {   
    
    public static boolean is(TypeMirror type, Class<?> expected) {
        if (!(type instanceof DeclaredType)) {
            return false;
        }
        
        var element = ((DeclaredType) type).asElement();
        if (!(element instanceof TypeElement)) {
            return false;
        }
        
        return ((TypeElement) element).getQualifiedName().contentEquals(expected.getName());
    }
    
    public static TypeKind kind(Class<?> type) {
        switch (type.getName()) {
            case "boolean":
                return TypeKind.BOOLEAN;
            case "byte":
                return TypeKind.BYTE;
            case "short":
                return TypeKind.SHORT;
            case "int":
                return TypeKind.INT;
            case "long":
                return TypeKind.LONG;
            case "float":
                return TypeKind.FLOAT;
            case "double":
                return TypeKind.DOUBLE;
            case "char":
                return TypeKind.CHAR;
            case "void":
                return TypeKind.VOID;
            default:
                return TypeKind.DECLARED;
        }
    }
    
    
    private final Elements elements;
    private final Types types;
    
    public TypeMirrors(Elements elements, Types types) {
        this.elements = elements;
        this.types = types;
    }
    
    
    public @Nullable TypeElement element(TypeMirror type) {
        var element = types.asElement(type);
        if (element instanceof TypeElement) {
            return (TypeElement) element;
            
        } else {
            return null;
        }
    }
    
    public TypeMirror box(TypeMirror type) {
        if (type instanceof PrimitiveType) {
            return types.boxedClass((PrimitiveType) type).asType();
            
        } else {
            return type;
        }
    }
    
    
    public TypeMirror type(Class<?> type) {
        return elements.getTypeElement(type.getName()).asType();
    }
    
    public TypeMirror erasure(Class<?> type) {
        return types.erasure(elements.getTypeElement(type.getName()).asType());
    }
    
    public TypeMirror specialize(Class<?> type, Class<?>... parameters) {
        var mirrors = new TypeMirror[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            mirrors[i] = type(parameters[i]);
        }
        
        return specialize(type, mirrors);
    }
    
    public TypeMirror specialize(Class<?> type, TypeMirror... parameters) {
        return types.getDeclaredType(elements.getTypeElement(type.getName()), parameters);
    }

    
    @Override
    public Element asElement(TypeMirror t) {
        return types.asElement(t);
    }

    @Override
    public boolean isSameType(TypeMirror t1, TypeMirror t2) {
        return types.isSameType(t1, t2);
    }

    @Override
    public boolean isSubtype(TypeMirror t1, TypeMirror t2) {
        return types.isSubtype(t1, t2);
    }

    @Override
    public boolean isAssignable(TypeMirror t1, TypeMirror t2) {
        return types.isAssignable(t1, t2);
    }

    @Override
    public boolean contains(TypeMirror t1, TypeMirror t2) {
        return types.contains(t1, t2);
    }

    @Override
    public boolean isSubsignature(ExecutableType m1, ExecutableType m2) {
        return types.isSubsignature(m1, m2);
    }

    @Override
    public List<? extends TypeMirror> directSupertypes(TypeMirror t) {
        return types.directSupertypes(t);
    }

    @Override
    public TypeMirror erasure(TypeMirror t) {
        return types.erasure(t);
    }

    @Override
    public TypeElement boxedClass(PrimitiveType p) {
        return types.boxedClass(p);
    }

    @Override
    public PrimitiveType unboxedType(TypeMirror t) {
        return types.unboxedType(t);
    }

    @Override
    public TypeMirror capture(TypeMirror t) {
        return types.capture(t);
    }

    @Override
    public PrimitiveType getPrimitiveType(TypeKind kind) {
        return types.getPrimitiveType(kind);
    }

    @Override
    public NullType getNullType() {
        return types.getNullType();
    }

    @Override
    public NoType getNoType(TypeKind kind) {
        return types.getNoType(kind);
    }

    @Override
    public ArrayType getArrayType(TypeMirror componentType) {
        return types.getArrayType(componentType);
    }

    @Override
    public WildcardType getWildcardType(TypeMirror extendsBound, TypeMirror superBound) {
        return types.getWildcardType(extendsBound, superBound);
    }

    @Override
    public DeclaredType getDeclaredType(TypeElement typeElem, TypeMirror... typeArgs) {
        return types.getDeclaredType(typeElem, typeArgs);
    }

    @Override
    public DeclaredType getDeclaredType(DeclaredType containing, TypeElement typeElem, TypeMirror... typeArgs) {
        return types.getDeclaredType(containing, typeElem, typeArgs);
    }

    @Override
    public TypeMirror asMemberOf(DeclaredType containing, Element element) {
        return types.asMemberOf(containing, element);
    }
    
}
