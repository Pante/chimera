/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.commons.command.annotations.processors;

import com.karuslabs.annotations.*;
import com.karuslabs.annotations.processors.AnnotationProcessor;
import com.karuslabs.annotations.filters.ClassFilter;
import com.karuslabs.commons.command.annotations.*;
import com.karuslabs.commons.util.collections.TokenMap;

import com.google.auto.service.AutoService;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.lang.annotation.Annotation;

import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.SimpleElementVisitor9;


@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes({
    "com.karuslabs.commons.command.annotations.Argument", "com.karuslabs.commons.command.annotations.Arguments",
    "com.karuslabs.commons.command.annotations.Bind"
})
public class BindingProcessor extends AnnotationProcessor {
    
    static final Set<Class<? extends Annotation>> ARGUMENTS = Set.of(Argument.class, Arguments.class);
    
    Map<String, Visitor> visitors;
    TypeMirror argument;
    TypeMirror suggestions;
    
    
    public BindingProcessor() {
        visitors = new HashMap<>();
    }
    
    
    @Override
    public void init(ProcessingEnvironment environment) {
        super.init(environment);
        argument = types.erasure(elements.getTypeElement(ArgumentType.class.getName()).asType());
        suggestions = types.erasure(elements.getTypeElement(SuggestionProvider.class.getName()).asType());
    }
    
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment environment) {
        for (var element : environment.getElementsAnnotatedWith(Bind.class)) {
            element.accept(visitor(element), null);
        }
        
        for (var element : environment.getElementsAnnotatedWithAny(ARGUMENTS)) {
            element.accept(visitor(element), null);
        }
        
        visitors.clear();
        
        return false;
    }
    
    protected Visitor visitor(Element element) {
        var type = element.accept(ClassFilter.FILTER, null).asType().toString();
        var visitor = visitors.get(type);
        if (visitor == null) {
            visitor = new Visitor();
            visitors.put(type, visitor);
        }
        
        return visitor;
    }
    
    
    
    public class Visitor extends SimpleElementVisitor9<Void, Void> {
        
        TokenMap<String, Object> bindings;
        
        
        public Visitor() {
            bindings = TokenMap.of();
        }
        
        
        @Override
        public Void visitVariable(VariableElement element, @Ignored Void parameter) {
            var type = types.erasure(element.asType());
            if (!types.isSubtype(type, argument) && !types.isSubtype(type, suggestions)) {
                error(element, "Invalid binded type: " + element.getSimpleName() + ", field must be either an ArgumentType or SuggestionProvider");
                return null;
            }
            
            var value = element.getAnnotation(Bind.class).value();
            var name = value.isEmpty() ? element.getSimpleName().toString() : value;
            var bound = types.isSubtype(type, argument) ? ArgumentType.class : SuggestionProvider.class;
            if (bindings.put(name, bound, null) != null) {
                error(element, "Duplicate binded type: " + element.getSimpleName() + ", a binding with the same name already exists");
            }
            
            return null;
        }
        
        
        @Override
        public Void visitType(TypeElement element, @Ignored Void parameter) {
            visitArgument(element);
            return null;
        }
        
        @Override
        public Void visitExecutable(ExecutableElement element, @Ignored Void parameter) {
            visitArgument(element);
            return null;
        }
        
        protected void visitArgument(Element element) {
            for (var argument : element.getAnnotationsByType(Argument.class)) {
                var namespace = argument.namespace();
                var type = namespace.length > 0 && argument.type().isEmpty() ? namespace[namespace.length - 1] : argument.type();
                
                if (!bindings.containsKey(type, ArgumentType.class)) {
                    error(element, "Unknown type: " + type + ", " + type + " must be a binded field");
                }
                
                if (!argument.suggestions().isEmpty() && !bindings.containsKey(argument.suggestions(), SuggestionProvider.class)) {
                    error(element, "Unknown suggestions: " + argument.suggestions() + ", " + argument.suggestions() + " must be a binded field");
                }
            }
        }
        
    }
    
}
