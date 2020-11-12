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
package com.karuslabs.puff.old;

import com.karuslabs.puff.type.TypeMirrors;

import java.lang.annotation.Annotation;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

import org.checkerframework.checker.nullness.qual.Nullable;

import static com.karuslabs.puff.Format.prefix;
import static com.karuslabs.puff.old.Matches.*;

public class Variable implements Match<VariableElement> {
    
    private final Match<Modifier> modifiers;
    private final Match<TypeMirror> type;
    private final Match<Class<? extends Annotation>> annotations;
    private final String singular;
    private final String plural;
    
    public Variable(Match<Modifier> modifiers, Match<TypeMirror> type, Match<Class<? extends Annotation>> annotations) {
        this.modifiers = modifiers;
        this.type = type;
        this.annotations = annotations;
        singular = prefix(modifiers.condition(), type.singular()) + " annotated with " + annotations.condition();
        plural = prefix(modifiers.condition(), type.plural()) + " annotated with " + annotations.condition();
    }
    
    @Override
    public @Nullable String match(Element element, TypeMirrors types) {
        if (!(element instanceof VariableElement)) {
            return element.getKind().toString().toLowerCase().replace('_', ' ');
            
        } else if (modifiers.match(element, types) != null || type.match(element, types) != null || annotations.match(element, types) != null) {
             return actual(element);
            
        } else {
            return null;
        }
    }
    
    @Override
    public String actual(Element element) {
        return modifiers.actual(element) + " " + type.actual(element) + " annotated with " + annotations.actual(element);
    }

    @Override
    public String condition() {
        return singular;
    }

    @Override
    public String singular() {
        return singular;
    }

    @Override
    public String plural() {
        return plural;
    }
    
    public static class Builder {
        
        private Match<Modifier> modifiers = ANY_MODIFIER;
        private Match<TypeMirror> type = ANY_TYPE;
        private Match<Class<? extends Annotation>> annotations = ANY_ANNOTATION;
        
        public Builder modifiers(Match<Modifier> modifiers) {
            this.modifiers = modifiers;
            return this;
        }
        
        public Builder type(Match<TypeMirror> type) {
            this.type = type;
            return this;
        }
        
        public Builder type(Class<?> type) {
            this.type = exactly(type);
        }
        
        public Builder annotations(Match<Class<? extends Annotation>> annotations) {
            this.annotations = annotations;
            return this;
        }
        
        public Variable build() {
            return new Variable(modifiers, type, annotations);
        }
        
    }

}
