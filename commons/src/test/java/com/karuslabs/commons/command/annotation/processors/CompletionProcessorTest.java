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

import com.karuslabs.commons.command.annotation.processors.CompletionProcessor;
import com.karuslabs.commons.command.annotation.*;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.util.Collections.singleton;
import static javax.tools.Diagnostic.Kind.ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class CompletionProcessorTest {
    
    CompletionProcessor checker = spy(new CompletionProcessor());
    TypeElement element = mock(TypeElement.class);
    Messager messager = mock(Messager.class);
    ProcessingEnvironment environment = when(mock(ProcessingEnvironment.class).getMessager()).thenReturn(messager).getMock();
    
    
    @Test
    void annotations() throws ClassNotFoundException {
        for (String supported: CompletionProcessor.class.getAnnotation(SupportedAnnotationTypes.class).value()) {
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
        doNothing().when(checker).checkLiterals(any());
        doNothing().when(checker).checkRegistrations(any());
        checker.indexes.add(10);
        
        boolean process = checker.process(set, environment);
        
        verify(checker).checkLiterals(element);
        verify(checker).checkRegistrations(element);
        
        assertTrue(checker.indexes.isEmpty());
        assertFalse(process);
    } 
    
    
    @ParameterizedTest
    @MethodSource("checkLiterals_parameters")
    void checkLiterals(Literal[] literals, int times) {
        doNothing().when(checker).check(any(), anyInt());
        when(element.getAnnotationsByType(Literal.class)).thenReturn(literals);
        
        checker.checkLiterals(element);
        
        verify(checker, times(times)).check(any(), anyInt());
    }
    
    static Stream<Arguments> checkLiterals_parameters() {
        Literal literal = new Literal() {
            @Override
            public int index() {
                return 0;
            }

            @Override
            public String[] completions() {
                return new String[] {};
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Literal.class;
            }
        };
        
        return Stream.of(of(new Literal[] {literal}, 1), of(new Literal[] {}, 0));
    }
    
    
    @ParameterizedTest
    @MethodSource("checkRegistrations_parameters")
    void checkRegistrations(Registered[] registrations, int times) {
        doNothing().when(checker).check(any(), anyInt());
        when(element.getAnnotationsByType(Registered.class)).thenReturn(registrations);
        
        checker.checkRegistrations(element);
        
        verify(checker, times(times)).check(any(), anyInt());
    }
    
    static Stream<Arguments> checkRegistrations_parameters() {
        Registered registered = new Registered() {
            @Override
            public int index() {
                return 0;
            }

            @Override
            public String completion() {
                return "";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Registered.class;
            }
        };
        
        return Stream.of(of(new Registered[] {registered}, 1), of(new Registered[] {}, 0));
    }
    
    
    @ParameterizedTest
    @CsvSource({"-1, 1, 0", "1, 0, 1", "0, 0, 0"})
    void check(int index, int boundTimes, int duplicateTimes) {
        checker.init(environment);
        checker.indexes.add(1);
        
        checker.check(element, index);
        
        verify(messager, times(boundTimes)).printMessage(ERROR, "Index out of bounds: " + index + ", index must be equal to or greater than 0", element);
        verify(messager, times(duplicateTimes)).printMessage(ERROR, "Conflicting indexes: " + index + ", indexes must be unique", element);
    }
    
}
