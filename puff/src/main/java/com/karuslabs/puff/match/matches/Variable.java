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

import com.karuslabs.puff.Texts;
import com.karuslabs.puff.match.*;
import com.karuslabs.puff.type.TypeMirrors;

import java.lang.annotation.Annotation;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

import org.checkerframework.checker.nullness.qual.Nullable;

import static com.karuslabs.puff.Texts.join;
import static com.karuslabs.puff.match.matches.Matches.*;

public class Variable extends AbstractDescription implements Timeable<VariableElement> {

    private final Match<Modifier> modifiers;
    private final Match<TypeMirror> type;
    private final Match<Class<? extends Annotation>> annotations;
    
    public Variable(Match<Modifier> modifiers, Match<TypeMirror> type, Match<Class<? extends Annotation>> annotations) {
        this(
            modifiers, 
            type, 
            annotations, 
            join(join(modifiers.expectation(), " ", type.expectation()), " annotated with ", annotations.expectation()),
            join(join(modifiers.expectation(), " ", type.expectations()), " annotated with ", annotations.expectation())
        );
    }
    
    public Variable(Match<Modifier> modifiers, Match<TypeMirror> type, Match<Class<? extends Annotation>> annotations, String expectation, String expectations) {
        super(expectation, expectations);
        this.modifiers = modifiers;
        this.type = type;
        this.annotations = annotations;
    }
    
    @Override
    public boolean match(TypeMirrors types, Element element) {
        if (!(element instanceof VariableElement)) {
            return false;
        }
        
        return modifiers.match(types, element) && type.match(types, element) && annotations.match(types, element);
    }

    @Override
    public String describe(Element element) {
        if (!(element instanceof VariableElement)) {
            return element.getKind().toString().toLowerCase().replace('_', ' ');
        }
        
        var description = modifiers.describe(element) + " " + type.describe(element);
        var annotated = annotations.describe(element);
        if (!annotated.isEmpty()) {
            description += " annotated with " + annotated;
        }
        return description;
    }
    
    
    public static class Builder {
        
        private Match<Modifier> modifiers = ANY_MODIFIER;
        private Match<TypeMirror> type = ANY_TYPE;
        private Match<Class<? extends Annotation>> annotations = ANY_ANNOTATION;
        private @Nullable String single;
        private @Nullable String plural;
        
        public Builder modifiers(Modifier... modifiers) {
            return modifiers(exactly(modifiers));
        }
        
        public Builder modifiers(Match<Modifier> modifiers) {
            this.modifiers = modifiers;
            return this;
        }
        
        public Builder type(Class<?> type) {
            return type(exactly(type));
        }
        
        public Builder type(Match<TypeMirror> type) {
            this.type = type;
            return this;
        }
        
        public Builder annotations(Match<Class<? extends Annotation>> annotations) {
            this.annotations = annotations;
            return this;
        }
        
        public Builder expectation(String single, String plural) {
            this.single = single;
            this.plural = plural;
            return this;
        }
        
        public Variable build() {
            return single == null || plural == null ? new Variable(modifiers, type, annotations) : new Variable(modifiers, type, annotations, single, plural);
        }
        
    }

}
