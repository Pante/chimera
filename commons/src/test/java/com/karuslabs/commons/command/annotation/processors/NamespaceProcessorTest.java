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

import com.karuslabs.commons.command.annotation.Namespace;
import java.lang.annotation.Annotation;
import java.util.stream.Stream;
import javax.annotation.processing.*;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static javax.tools.Diagnostic.Kind.ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class NamespaceProcessorTest {
    
    NamespaceProcessor processor = spy(new NamespaceProcessor());
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
        
        doNothing().when(processor).check(any(), any());
        when(element.getAnnotationsByType(Namespace.class)).thenReturn(new Namespace[] {namespace});
        
        processor.process(element);
        
        verify(processor).check(any(), any());
    }
    
    
    @Test
    void check_empty() {
        processor.init(environment);
        processor.check(element, new String[] {});
        
        verify(messager).printMessage(ERROR, "Invalid namespace, namespace cannot be empty", element);
    }
    
    
    @ParameterizedTest
    @MethodSource("check_parameters")
    void check(String[] namespace, int times, int size) {
        processor.init(environment);
        processor.namespaces.put("other.element", "other");
        
        processor.check(element, namespace);
        
        verify(messager, times(times)).printMessage(ERROR, "Conflicting namespaces: " + element.asType().toString() + " and other, namespaces must be unique", element);
        assertEquals(size, processor.namespaces.size());
    }
    
    static Stream<Arguments> check_parameters() {
        return Stream.of(of(new String[] {"other", "element"}, 1, 1), of(new String[] {"aye"}, 0, 2));
    }
    
}
