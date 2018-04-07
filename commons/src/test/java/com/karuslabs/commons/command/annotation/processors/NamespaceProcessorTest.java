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

import com.karuslabs.commons.command.annotation.processors.NamespaceProcessor;
import com.karuslabs.commons.command.annotation.Namespace;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.util.Collections.singleton;
import static javax.tools.Diagnostic.Kind.ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class NamespaceProcessorTest {
    
    NamespaceProcessor checker = spy(new NamespaceProcessor());
    TypeElement element = when(mock(TypeElement.class).asType()).thenReturn(mock(TypeMirror.class)).getMock();
    Messager messager = mock(Messager.class);
    ProcessingEnvironment environment = when(mock(ProcessingEnvironment.class).getMessager()).thenReturn(messager).getMock();
    
    
    @Test
    void annotations() throws ClassNotFoundException {
        for (String supported: NamespaceProcessor.class.getAnnotation(SupportedAnnotationTypes.class).value()) {
            assertTrue(Class.forName(supported).isAnnotation());
        }
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
        
        Namespace namespace = new Namespace() {
            @Override
            public String[] value() {
                return new String[] {};
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Namespace.class;
            }
        };
        
        doNothing().when(checker).check(any(), any());
        when(element.getAnnotationsByType(Namespace.class)).thenReturn(new Namespace[] {namespace});
        
        checker.process(set, environment);
        
        verify(checker).check(any(), any());
    }
    
    
    @Test
    void check_empty() {
        checker.init(environment);
        checker.check(element, new String[] {});
        
        verify(messager).printMessage(ERROR, "Invalid namespace, namespace cannot be empty", element);
    }
    
    
    @ParameterizedTest
    @MethodSource("check_parameters")
    void check(String[] namespace, int times, int size) {
        checker.init(environment);
        checker.namespaces.put("other.element", "other");
        
        checker.check(element, namespace);
        
        verify(messager, times(times)).printMessage(ERROR, "Conflicting namespaces: " + element.asType().toString() + " and other, namespaces must be unique", element);
        assertEquals(size, checker.namespaces.size());
    }
    
    static Stream<Arguments> check_parameters() {
        return Stream.of(of(new String[] {"other", "element"}, 1, 1), of(new String[] {"aye"}, 0, 2));
    }
    
}
