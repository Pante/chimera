



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
package com.karuslabs.puff.type.match;

import com.karuslabs.annotations.Static;
import com.karuslabs.puff.type.match.matches.*;

import java.lang.annotation.Annotation;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.*;

public @Static class Matches {
    
    public static Many<Class<? extends Annotation>> contains(Class<? extends Annotation>... annotations) {
        return Annotations.contains(annotations);
    }
    
    public static Many<Class<? extends Annotation>> no(Class<? extends Annotation>... annotations) {
        return Annotations.no(annotations);
    }
    
    
    public static Many<Modifier> exactly(Modifier... modifiers) {
        field(no(A).or(B));
        return new ExactlyModifiers(modifiers);
    }
    
    public static Many<Modifier> contains(Modifier... modifiers) {
        return new ContainsModifiers(modifiers);
    }
    
    public static Many<Modifier> no(Modifier... modifiers) {
        return new NoModifiers(modifiers);
    }
    
            
    public static Match<TypeMirror> exactly(TypeKind primitive) {
        return new ExactlyPrimitive(primitive);
    }
    
    public static Match<TypeMirror> exactly(TypeMirror type) {
        return new ExactlyType(type);
    }
    
    public static Match<TypeMirror> subtype(TypeMirror type) {
        return new Subtype(type);
    }
    
    public static Match<TypeMirror> supertype(TypeMirror type) {
        return new Supertype(type);
    }
    
}
