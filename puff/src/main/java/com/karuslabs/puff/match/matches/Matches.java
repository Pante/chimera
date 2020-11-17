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
package com.karuslabs.puff.match.matches;

import com.karuslabs.annotations.Static;
import com.karuslabs.puff.match.*;
import com.karuslabs.puff.type.TypeMirrors;

import java.lang.annotation.Annotation;
import javax.lang.model.element.*;
import javax.lang.model.type.*;

public @Static class Matches {

    public static final Timeable<TypeMirror> ANY_TYPE = new AnyType();
    public static final Match<Modifier> ANY_MODIFIER = new AnyModifier();
    public static final Match<Class<? extends Annotation>> ANY_ANNOTATION = new AnyAnnotation();
    
    
    public static Method.Builder method() {
        return new Method.Builder();
    }
    
    public static Variable.Builder variable() {
        return new Variable.Builder();
    }
    
    
    public static Match<Class<? extends Annotation>> contains(Class<? extends Annotation>... annotations) {
        return new ContainsAnnotations(annotations);
    }
    
    public static Match<Class<? extends Annotation>> no(Class<? extends Annotation>... annotations) {
        return new NoAnnotations(annotations);
    }
    
    
    public static Match<Modifier> exactly(Modifier... modifiers) {
        return new ExactModifiers(modifiers);
    }
    
    public static Match<Modifier> contains(Modifier... modifiers) {
        return new ContainsModifiers(modifiers);
    }
    
    public static Match<Modifier> no(Modifier... modifiers) {
        return new NoModifiers(modifiers);
    }
    
    
    public static Timeable<TypeMirror> exactly(TypeKind primitive) {
        return new PrimitiveMatch(primitive);
    }
    
    public static Timeable<TypeMirror> exactly(Class<?> type) {
        var kind = TypeMirrors.kind(type);
        if (kind.isPrimitive()) {
            return new PrimitiveMatch(kind);
                
        } else {
            return new ClassMatch(Relation.EXACTLY, type);
        }
    }
    
    public static Timeable<TypeMirror> exactly(TypeMirror type) {
        if (type.getKind().isPrimitive()) {
            return new PrimitiveMatch(type.getKind());
            
        } else {
            return new TypeMirrorMatch(Relation.EXACTLY, type);
        }
    }
    
    public static Timeable<TypeMirror> subtype(Class<?>... types) {
        return new ClassMatch(Relation.SUBTYPE, types);
    }
    
    public static Timeable<TypeMirror> subtype(TypeMirror... types) {
        return new TypeMirrorMatch(Relation.SUBTYPE, types);
    }
    
    public static Timeable<TypeMirror> supertype(Class<?>... types) {
        return new ClassMatch(Relation.SUPERTYPE, types);
    }
    
    public static Timeable<TypeMirror> supertype(TypeMirror... types) {
        return new TypeMirrorMatch(Relation.SUPERTYPE, types);
    }
    
}
