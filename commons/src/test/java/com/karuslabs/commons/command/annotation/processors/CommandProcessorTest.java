/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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
package com.karuslabs.commons.command.annotation.processors;

import com.karuslabs.commons.command.annotation.processors.CommandProcessor;
import com.karuslabs.commons.command.CommandExecutor;
import com.karuslabs.commons.command.annotation.*;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.util.Collections.singleton;
import static javax.tools.Diagnostic.Kind.ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class CommandProcessorTest {
    
    CommandProcessor checker;
    TypeElement element;
    Elements elements;
    Types types;
    Messager messager;
    ProcessingEnvironment environment;
    
    
    CommandProcessorTest() {
        checker = spy(new CommandProcessor());
        environment = mock(ProcessingEnvironment.class);
        element = mock(TypeElement.class);
        elements = mock(Elements.class);
        types = mock(Types.class);
        messager = mock(Messager.class);
        
        when(element.asType()).thenReturn(mock(TypeMirror.class));
        when(elements.getTypeElement(CommandExecutor.class.getName())).thenReturn(element);
        when(environment.getElementUtils()).thenReturn(elements);
        when(environment.getTypeUtils()).thenReturn(types);
        when(environment.getMessager()).thenReturn(messager);
    }
    
    
    @Test
    void annotations() throws ClassNotFoundException {
        for (String supported: CommandProcessor.class.getAnnotation(SupportedAnnotationTypes.class).value()) {
            assertTrue(Class.forName(supported).isAnnotation());
        }
    }
    
    
    @Test
    void init() {
        checker.init(environment);
        
        assertSame(element.asType(), checker.getExpected());
        assertSame(messager, checker.getMessager());
    }
    
    
    @Test
    void process() {
        Set<TypeElement> set = singleton(element);
        RoundEnvironment environment = new RoundEnvironment() {
            @Override
            public boolean processingOver() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean errorRaised() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Set<? extends Element> getRootElements() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Set<? extends Element> getElementsAnnotatedWith(TypeElement a) {
                return set;
            }

            @Override
            public Set<? extends Element> getElementsAnnotatedWith(Class<? extends Annotation> a) {
                return set;
            }
        };
        doNothing().when(checker).checkAssignability(any());
        doNothing().when(checker).checkNamespace(any());
        
        boolean processed = checker.process(set, environment);
        
        verify(checker).checkAssignability(any());
        verify(checker).checkNamespace(any());
        assertFalse(processed);
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, 0", "false, 1"})
    void checkAssignability(boolean assignable, int times) {
        when(types.isAssignable(any(), any())).thenReturn(assignable);
        
        checker.init(environment);
        checker.checkAssignability(element);
        
        verify(messager, times(times)).printMessage(ERROR, "Invalid annotated type: " + element.asType().toString() + ", type must implement " + CommandExecutor.class.getName() , element);
    }
    
    
    @ParameterizedTest
    @MethodSource("checkNamespace_parameters")
    void checkNamespace(Namespace[] namespaces, int times) {
        when(element.getAnnotationsByType(Namespace.class)).thenReturn(namespaces);
        
        checker.init(environment);
        checker.checkNamespace(element);
        
        verify(messager, times(times)).printMessage(ERROR, "Missing namespace: " + element.asType().toString() + ", command must be declared with a namespace", element);
    }
    
    static Stream<Arguments> checkNamespace_parameters() {
        Namespace namespace = new Namespace() {
            @Override
            public String[] value() {
                return null;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Namespace.class;
            }
        };
        
        return Stream.of(of(new Namespace[] {namespace}, 0), of(new Namespace[] {}, 1));
    }
    
}
