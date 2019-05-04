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

import com.karuslabs.annotations.filters.ClassFilter;
import com.karuslabs.commons.command.annotations.*;
import com.karuslabs.commons.command.annotations.processors.BindingProcessor.Visitor;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.lang.annotation.Annotation;

import java.util.*;
import java.util.stream.Stream;
import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.*;
import javax.tools.Diagnostic;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.Spy;
import org.mockito.junit.jupiter.*;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BindingProcessorTest {
    
    BindingProcessor processor = new BindingProcessor();
    Visitor visitor = processor.new Visitor();
    ProcessingEnvironment environment = mock(ProcessingEnvironment.class);
    Elements elements = mock(Elements.class);
    Messager messager = mock(Messager.class);
    Types types = mock(Types.class);
    TypeMirror argument = mock(TypeMirror.class);
    TypeMirror provider = mock(TypeMirror.class);
    ExecutableElement method = mock(ExecutableElement.class);
    TypeMirror type = mock(TypeMirror.class);
    Element element = when(mock(Element.class).asType()).thenReturn(type).getMock();
    
    
    @BeforeEach
    void before() {
        when(environment.getElementUtils()).thenReturn(elements);
        when(environment.getMessager()).thenReturn(messager);
        when(environment.getTypeUtils()).thenReturn(types);
        
        TypeElement argumentElement = when(mock(TypeElement.class).asType()).thenReturn(argument).getMock();
        when(elements.getTypeElement(ArgumentType.class.getName())).thenReturn(argumentElement);
        when(types.erasure(argument)).thenReturn(argument);
        
        TypeElement providerElement = when(mock(TypeElement.class).asType()).thenReturn(provider).getMock();
        when(elements.getTypeElement(SuggestionProvider.class.getName())).thenReturn(providerElement);
        when(types.erasure(provider)).thenReturn(provider);
        
        processor.init(environment);
    }
    
    
    @Test
    void init() {
        assertNotNull(processor.argument);
        assertNotNull(processor.suggestions);
    }
    
    
    @Test
    void process() {
        RoundEnvironment environment = mock(RoundEnvironment.class);
        
        Element bind = when(mock(Element.class).asType()).thenReturn(type).getMock();
        when(bind.accept(ClassFilter.FILTER, null)).thenReturn(bind);
        
        Element argument = when(mock(Element.class).asType()).thenReturn(type).getMock();
        when(argument.accept(ClassFilter.FILTER, null)).thenReturn(argument);
        
        doReturn(Set.of(bind)).when(environment).getElementsAnnotatedWith(Bind.class);
        doReturn(Set.of(argument)).when(environment).getElementsAnnotatedWithAny(BindingProcessor.ARGUMENTS);
        
        when(element.accept(ClassFilter.FILTER, null)).thenReturn(element);
        
        assertFalse(processor.process(Set.of(), environment));
        assertTrue(processor.visitors.isEmpty());
        
        verify(bind).accept(any(Visitor.class), any());
        verify(argument).accept(any(Visitor.class), any());
    }
    
    
    @Test
    void visitor() {
        when(element.accept(ClassFilter.FILTER, null)).thenReturn(element);
        
        var visitor = processor.visitor(element);
        
        assertSame(visitor, processor.visitors.get(type.toString()));
    }
    
    
    @Test
    void visitVariable_invalid_type() {
        VariableElement variable = when(mock(VariableElement.class).asType()).thenReturn(type).getMock();
        Name name = mock(Name.class);
        
        when(variable.getSimpleName()).thenReturn(name);
        
        TypeMirror erasured = mock(TypeMirror.class);
        
        when(types.erasure(type)).thenReturn(erasured);
        when(types.isSubtype(eq(erasured), any())).thenReturn(false);
        
        assertNull(visitor.visitVariable(variable, null));
        verify(messager).printMessage(Diagnostic.Kind.ERROR, "Invalid binded type: " + name + ", field must be either an ArgumentType or SuggestionProvider", variable);
    }
    
    
    @Test
    void visitVariable_duplicate_binding() {
        VariableElement variable = when(mock(VariableElement.class).asType()).thenReturn(type).getMock();
        Name name = mock(Name.class);
        
        when(variable.getSimpleName()).thenReturn(name);
        
        TypeMirror erasured = mock(TypeMirror.class);
        
        when(types.erasure(type)).thenReturn(erasured);
        when(types.isSubtype(eq(erasured), any())).thenReturn(true);
        
        Bind bind = when(mock(Bind.class).value()).thenReturn("name").getMock();
        when(variable.getAnnotation(Bind.class)).thenReturn(bind);
        
        visitor.bindings.put("name", ArgumentType.class, mock(ArgumentType.class));
        
        assertNull(visitor.visitVariable(variable, null));
        verify(messager).printMessage(Diagnostic.Kind.ERROR, "Duplicate binded type: " + name + ", a binding with the same name already exists", variable);
    }
    
    
    @Test
    void visitType() {
        TypeElement element = when(mock(TypeElement.class).getAnnotationsByType(Argument.class)).thenReturn(new Argument[] {}).getMock();
        
        visitor = spy(visitor);
        
        assertNull(visitor.visitType(element, null));
        verify(visitor).visitArgument(element);
    }
    
    
    @Test
    void visitExecutable() {
        ExecutableElement element = when(mock(ExecutableElement.class).getAnnotationsByType(Argument.class)).thenReturn(new Argument[] {}).getMock();
        
        visitor = spy(visitor);
        
        assertNull(visitor.visitExecutable(element, null));
        verify(visitor).visitArgument(element);
    }
    
    
    @Test
    void visitArgument() {
        when(element.getAnnotationsByType(Argument.class)).thenReturn(new Argument[]{new Argument() {
            @Override
            public String[] namespace() {
                return new String[] {"t"};
            }

            @Override
            public String type() {
                return "";
            }

            @Override
            public String suggestions() {
                return "s";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Argument.class;
            }
        }});
        
        visitor.visitArgument(element);
        
        verify(messager).printMessage(Diagnostic.Kind.ERROR, "Unknown type: t, t must be a binded field", element);
        verify(messager).printMessage(Diagnostic.Kind.ERROR, "Unknown suggestions: s, s must be a binded field", element);
    }

} 
