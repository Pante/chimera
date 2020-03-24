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

import com.karuslabs.commons.command.OptionalContext;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.*;
import java.util.stream.Stream;
import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.*;
import javax.tools.Diagnostic.Kind;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.*;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SignatureProcessorTest {
    
    SignatureProcessor processor = new SignatureProcessor();
    ProcessingEnvironment environment = mock(ProcessingEnvironment.class);
    Elements elements = mock(Elements.class);
    Messager messager = mock(Messager.class);
    Types types = mock(Types.class);
    TypeMirror context = mock(TypeMirror.class);
    TypeMirror defaultable = mock(TypeMirror.class);
    TypeMirror exception = mock(TypeMirror.class);
    ExecutableElement method = mock(ExecutableElement.class);
    
    
    @BeforeEach
    void before() {
        when(environment.getElementUtils()).thenReturn(elements);
        when(environment.getMessager()).thenReturn(messager);
        when(environment.getTypeUtils()).thenReturn(types);
        
        TypeElement contextElement = when(mock(TypeElement.class).asType()).thenReturn(context).getMock();
        when(elements.getTypeElement(CommandContext.class.getName())).thenReturn(contextElement);
        when(types.erasure(context)).thenReturn(context);
        
        TypeElement defaultableContext = when(mock(TypeElement.class).asType()).thenReturn(defaultable).getMock();
        when(elements.getTypeElement(OptionalContext.class.getName())).thenReturn(defaultableContext);
        when(types.erasure(defaultable)).thenReturn(defaultable);
        
        TypeElement exceptionElement = when(mock(TypeElement.class).asType()).thenReturn(exception).getMock();
        when(elements.getTypeElement(CommandSyntaxException.class.getName())).thenReturn(exceptionElement);
        
        processor.init(environment);
    }
    
    
    @Test
    void init() {
        assertNotNull(processor.context);
        assertNotNull(processor.defaultable);
        assertNotNull(processor.exception);
    }
    
    
    @Test
    void process() {
        processor.process(method);
        
        verify(method).accept(processor.visitor, null);
    }
    
    
    @ParameterizedTest
    @MethodSource("visitExecutable_modifiers_parameters")
    void visitExecutable_modifiers(Modifier modifier) {
        processor.visitor = spy(processor.visitor);
        doReturn(true).when(processor.visitor).signature(any(), any());
        
        doReturn(mock(Name.class)).when(method).getSimpleName();
        doReturn(List.of(mock(VariableElement.class))).when(method).getParameters();
        
        when(method.getModifiers()).thenReturn(Set.of(modifier));
        
        assertNull(processor.visitor.visitExecutable(method, null));
        verify(messager).printMessage(Kind.ERROR, "Invalid method: " + method.getSimpleName() + ", method cannot be private or static", method);
    }
    
    static Stream<Modifier> visitExecutable_modifiers_parameters() {
        return Stream.of(Modifier.PRIVATE, Modifier.STATIC);
    }
    
    
    @ParameterizedTest
    @MethodSource("visitExecutable_parameters_parameters")
    void visitExecutable_parameters(List<VariableElement> elements, boolean signature, int times) {
        processor.visitor = spy(processor.visitor);
        doReturn(signature).when(processor.visitor).signature(any(), any());
        
        doReturn(Set.of()).when(method).getModifiers();
        doReturn(mock(Name.class)).when(method).getSimpleName();
        doReturn(elements).when(method).getParameters();
        
        assertNull(processor.visitor.visitExecutable(method, null));
        verify(messager, times(times)).printMessage(Kind.ERROR, "Invalid method: " + method.getSimpleName() + ", method signature must match either Command or Executable", method);
    }
    
    static Stream<Arguments> visitExecutable_parameters_parameters() {
        return Stream.of(
            of(List.of(), true, 1),
            of(List.of(mock(VariableElement.class)), false, 1),
            of(List.of(mock(VariableElement.class)), true, 0)
        );
    }

    
    @ParameterizedTest
    @MethodSource("signature_parameters")
    void signature(TypeKind kind, boolean context, boolean defaultable, boolean expected) {
        TypeMirror returnable = when(mock(TypeMirror.class).getKind()).thenReturn(kind).getMock();
        
        TypeMirror parameterType = mock(TypeMirror.class);
        VariableElement variable = when(mock(VariableElement.class).asType()).thenReturn(parameterType).getMock();
        
        TypeMirror type = mock(TypeMirror.class);
        when(types.erasure(parameterType)).thenReturn(type);
        
        when(types.isSameType(processor.context, type)).thenReturn(context);
        when(types.isSameType(processor.defaultable, type)).thenReturn(defaultable);
        
        assertEquals(expected, processor.visitor.signature(returnable, List.of(variable)));
    }
    
    static Stream<Arguments> signature_parameters() {
        return Stream.of(
            of(TypeKind.INT, true, false, true),
            of(TypeKind.VOID, false, true, true),
            of(TypeKind.INT, false, true, false),
            of(TypeKind.VOID, true, false, false)
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("exception_parameters")
    void exceptions(List<TypeMirror> thrown, boolean equals, boolean expected) {
        when(types.isSubtype(any(), eq(exception))).thenReturn(equals);
        
        assertEquals(expected, processor.visitor.exceptions(thrown));
    }
    
    static Stream<Arguments> exception_parameters() {
        var type = mock(TypeMirror.class);
        return Stream.of(
            of(List.of(), true, true),
            of(List.of(type), true, true),
            of(List.of(type), false, false),
            of(List.of(type, type), true, false)
        );
    }

} 
