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
package com.karuslabs.puff.assertion;

import com.karuslabs.annotations.Static;

import com.karuslabs.puff.assertion.matches.*;
import com.karuslabs.puff.assertion.sequences.Sequence;
import com.karuslabs.puff.assertion.times.Times;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.function.Supplier;
import javax.lang.model.element.*;
import javax.lang.model.type.*;

public @Static class Assertions {

    public static final Sequence<VariableElement> ANY_PARAMETERS = Sequence.ANY_PARAMETERS;
    public static final Sequence<TypeMirror> ANY_TYPES = Sequence.ANY_TYPES;
    
    public static final Match<Class<? extends Annotation>> ANY_ANNOTATION = AnnotationMatch.ANY;
    public static final Match<Set<Modifier>> ANY_MODIFIER = ModifierMatch.ANY;
    public static final Match<TypeMirror> ANY_TYPE = TypeMatch.ANY;
    
    
    public static Method.Builder method() {
        return new Method.Builder();
    }
    
    public static Variable.Builder variable() {
        return new Variable.Builder();
    }
    

    public static <T> Sequence<T> contains(Times<T>... times) {
        return Sequence.contains(times);
    }
    
    
    public static <T> Sequence<T> match(Times<T>... matches) {
        return Sequence.match(matches);
    }
    
    public static <T> Sequence<T> match(Supplier<? extends Match<T>>... matches) {
        var supplied = new Match[matches.length];
        for (int i = 0; i < matches.length; i++) {
            supplied[i] = matches[i].get();
        }
        return match(supplied);
    }
    
    public static <T> Sequence<T> match(Match<T>... matches) {
        return Sequence.match(matches);
    }
    
    
    public static <T> Sequence<T> each(Supplier<? extends Match<T>> match) {
        return each(match.get());
    }
    
    public static <T> Sequence<T> each(Match<T> match) {
        return Sequence.each(match);
    }
    
    
    public static <T> Times<T> exactly(int times, Supplier<? extends Timeable<T>> match) {
        return exactly(times, match.get());
    }
    
    public static <T> Times<T> exactly(int times, Timeable<T> match) {
        return Times.exactly(times, match);
    }
    
    
    public static <T> Times<T> between(int min, int max, Supplier<? extends Timeable<T>> match) {
        return between(min, max, match.get());
    }
    
    public static <T> Times<T> between(int min, int max, Timeable<T> match) {
        return Times.between(min, max, match);
    }
    
    
    public static <T> Times<T> min(int min, Supplier<? extends Timeable<T>> match) {
        return min(min, match.get());
    }
    
    public static <T> Times<T> min(int min, Timeable<T> match) {
        return Times.min(min, match);
    }
    
    
    public static <T> Times<T> max(int max, Supplier<? extends Timeable<T>> match) {
        return max(max, match.get());
    }
    
    public static <T> Times<T> max(int max, Timeable<T> match) {
        return Times.max(max, match);
    }
    
    
    public static <T> Times<T> no(Supplier<? extends Timeable<T>> match) {
        return no(match.get());
    }
    
    public static <T> Times<T> no(Timeable<T> match) {
        return Times.no(match);
    }
    
    
    public static Match<Class<? extends Annotation>> contains(Class<? extends Annotation>... annotations) {
        return AnnotationMatch.contains(annotations);
    }
    
    public static Match<Class<? extends Annotation>> no(Class<? extends Annotation>... annotations) {
        return AnnotationMatch.no(annotations);
    }
    
    
    public static final Match<Set<Modifier>> contains(Modifier... modifiers) {
        return ModifierMatch.contains(modifiers);
    }
    
    public static final Match<Set<Modifier>> match(Modifier... modifiers) {
        return ModifierMatch.match(modifiers);
    }
    
    public static final Match<Set<Modifier>> no(Modifier... modifiers) {
        return ModifierMatch.no(modifiers);
    }
    
    
    public static Timeable<TypeMirror> is(TypeKind primitive) {
        return TypeMatch.is(primitive);
    }
    
    public static Timeable<TypeMirror> is(Class<?> type) {
        return TypeMatch.is(type);
    }
    
    public static Timeable<TypeMirror> is(TypeMirror type) {
        return TypeMatch.is(type);
    }
    
    
    public static Timeable<TypeMirror> subtype(Class<?>... types) {
        return TypeMatch.subtype(types);
    }
    
    public static Timeable<TypeMirror> subtype(TypeMirror... types) {
        return TypeMatch.subtype(types);
    }
    
    
    public static Timeable<TypeMirror> supertype(Class<?>... types) {
        return TypeMatch.supertype(types);
    }
    
    public static Timeable<TypeMirror> supertype(TypeMirror... types) {
        return TypeMatch.supertype(types);
    }
    
}
