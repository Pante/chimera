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
package com.karuslabs.Satisfactory.assertion;

import com.karuslabs.Satisfactory.assertion.matches.*;
import com.karuslabs.Satisfactory.assertion.sequences.Sequence;
import com.karuslabs.Satisfactory.type.*;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Supplier;
import javax.lang.model.element.*;
import javax.lang.model.type.*;

import org.checkerframework.checker.nullness.qual.Nullable;

import static com.karuslabs.Satisfactory.Texts.*;
import static com.karuslabs.Satisfactory.assertion.Assertions.*;

public class Method extends SkeletonAssertion {

    public static String format(String annotations, String modifiers, String type, String arguments, String exceptions) {
        var description = "";
        
        if (!annotations.isBlank()) {
            description = "[" + annotations + "]" + System.lineSeparator();
        }
        
        description += join(join(modifiers, " ", join("[", type , "]")), " method", join("(", arguments.isEmpty() ? "..." : arguments, ")"));
        
        if (!exceptions.isBlank()) {
            description += " throws " + exceptions;
        }

        return description;
    }
    
    private final Match<Class<? extends Annotation>> annotations;
    private final Match<Set<Modifier>> modifiers;
    private final Match<TypeMirror> type;
    private final Sequence<VariableElement> parameters;
    private final Sequence<TypeMirror> exceptions;
    
    public Method(Match<Class<? extends Annotation>> annotations, Match<Set<Modifier>> modifiers, Match<TypeMirror> type, Sequence<VariableElement> parmaeters, Sequence<TypeMirror> exceptions) {
        this(annotations, modifiers, type, parmaeters, exceptions,
            format(annotations.condition(), modifiers.condition(), type.condition(), parmaeters.condition(), exceptions.condition())
        );
    }
    
    public Method(Match<Class<? extends Annotation>> annotations, Match<Set<Modifier>> modifiers, Match<TypeMirror> type, Sequence<VariableElement> parmaeters, Sequence<TypeMirror> exceptions, String condition) {
        this(annotations, modifiers, type, parmaeters, exceptions, condition, condition);
    }
    
    public Method(Match<Class<? extends Annotation>> annotations, Match<Set<Modifier>> modifiers, Match<TypeMirror> type, Sequence<VariableElement> parameters, Sequence<TypeMirror> exceptions, String condition, String conditions) {
        super(condition, conditions);
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.type = type;
        this.parameters = parameters;
        this.exceptions = exceptions;
    }
    
    public boolean test(TypeMirrors types, Element element) {
        if (!(element instanceof ExecutableElement)) {
            return false;
        }
        
        var method = (ExecutableElement) element;
        var arguments = parameters.test(types, method.getParameters());
        var thrown = exceptions.test(types, method.getThrownTypes());
        
        return modifiers.test(types, method) && annotations.test(types, method) 
            && type.test(types, method.getReturnType()) && arguments && thrown;
    }

    
    public String describe(TypeMirrors types, Element element) {
        if (element instanceof ExecutableElement) {
            return describe(types, (ExecutableElement) element);
            
        } else {
            return element.getKind().toString().toLowerCase().replace('_', ' ');
        }
    }
    
    public String describe(TypeMirrors types, ExecutableElement method) {
        return format(
            annotations.describe(method),
            modifiers.describe(method),
            type.describe(method.getReturnType()),
            parameters.describe(types, method.getParameters()),
            exceptions.describe(types, method.getThrownTypes())
        );
    }
    
    
    public static class Builder implements Supplier<Method> {
        
        private Match<Class<? extends Annotation>> annotations = ANY_ANNOTATION;
        private Match<Set<Modifier>> modifiers = ANY_MODIFIER;
        private Match<TypeMirror> type = ANY_TYPE;
        private Sequence<VariableElement> parameters = ANY_PARAMETERS;
        private Sequence<TypeMirror> exceptions = ANY_TYPES;
        private @Nullable String single;
        private @Nullable String plural;
        
        public Builder annotations(Match<Class<? extends Annotation>> annotations) {
            this.annotations = annotations;
            return this;
        }
        
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
        
        public Builder parameters(Sequence<VariableElement> parameters) {
            this.parameters = parameters;
            return this;
        }
        
        public Builder exceptions(Sequence<TypeMirror> exceptions) {
            this.exceptions = exceptions;
            return this;
        }
        
        public Builder condition(String condition) {
            return condition(condition, condition);
        }
        
        public Builder condition(String single, String plural) {
            this.single = single;
            this.plural = plural;
            return this;
        }
        
        @Override
        public Method get() {
            if (single == null || plural == null) {
                return new Method(annotations, modifiers, type, parameters, exceptions);
                
            } else {
                return new Method(annotations, modifiers, type, parameters, exceptions, single, plural);
            }
        }
        
    }

}

