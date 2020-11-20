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
package com.karuslabs.puff.assertion.matches;

import com.karuslabs.puff.type.TypeMirrors;
import com.karuslabs.puff.assertion.SkeletonAssertion;

import java.lang.annotation.Annotation;
import java.util.Set;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Supplier;

import static com.karuslabs.puff.Texts.join;
import static com.karuslabs.puff.assertion.Assertions.*;

public class Variable extends SkeletonAssertion implements Timeable<VariableElement> {

    private final Match<Set<Modifier>> modifiers;
    private final Match<TypeMirror> type;
    private final Match<Class<? extends Annotation>> annotations;
    
    public Variable(Match<Set<Modifier>> modifiers, Match<TypeMirror> type, Match<Class<? extends Annotation>> annotations) {
        this(
            modifiers, 
            type, 
            annotations, 
            join(join(modifiers.condition(), " ", type.condition()), " annotated with ", annotations.condition()),
            join(join(modifiers.condition(), " ", type.conditions()), " annotated with ", annotations.condition())
        );
    }
    
    public Variable(Match<Set<Modifier>> modifiers, Match<TypeMirror> type, Match<Class<? extends Annotation>> annotations, String condition, String conditions) {
        super(condition, conditions);
        this.modifiers = modifiers;
        this.type = type;
        this.annotations = annotations;
    }
    
    @Override
    public boolean test(TypeMirrors types, Element element) {
        return element instanceof VariableElement && test(types, (VariableElement) element);
    }
    
    @Override
    public boolean test(TypeMirrors types, VariableElement element) {
        return modifiers.test(types, element) && type.test(types, element) && annotations.test(types, element);
    }

    
    @Override
    public String describe(Element element) {
        if (element instanceof VariableElement) {
            return describe((VariableElement) element);
            
        } else {
            return element.getKind().toString().toLowerCase().replace('_', ' ');
        }
    }

    @Override
    public String describe(VariableElement element) {
        var description = modifiers.describe(element) + " " + type.describe(element);
        var annotated = annotations.describe(element);
        if (!annotated.isEmpty()) {
            description += " annotated with " + annotated;
        }
        
        return description;
    }
    
    
    public static class Builder implements Supplier<Variable> {
        
        private Match<Set<Modifier>> modifiers = ANY_MODIFIER;
        private Match<TypeMirror> type = ANY_TYPE;
        private Match<Class<? extends Annotation>> annotations = ANY_ANNOTATION;
        private @Nullable String single;
        private @Nullable String plural;
        
        public Builder modifiers(Modifier... modifiers) {
            return modifiers(match(modifiers));
        }
        
        public Builder modifiers(Match<Set<Modifier>> modifiers) {
            this.modifiers = modifiers;
            return this;
        }
        
        public Builder type(Class<?> type) {
            return type(is(type));
        }
        
        public Builder type(Match<TypeMirror> type) {
            this.type = type;
            return this;
        }
        
        public Builder annotations(Match<Class<? extends Annotation>> annotations) {
            this.annotations = annotations;
            return this;
        }
        
        public Builder condition(String single) {
            return condition(single, single);
        }
        
        public Builder condition(String single, String plural) {
            this.single = single;
            this.plural = plural;
            return this;
        }
        
        @Override
        public Variable get() {
            return single == null || plural == null ? new Variable(modifiers, type, annotations) : new Variable(modifiers, type, annotations, single, plural);
        }
        
    }

}
