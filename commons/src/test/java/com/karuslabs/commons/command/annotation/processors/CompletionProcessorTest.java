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

import com.karuslabs.commons.command.annotation.*;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;
import javax.annotation.processing.*;
import javax.lang.model.element.TypeElement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static javax.tools.Diagnostic.Kind.ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class CompletionProcessorTest {
    
    CompletionProcessor processor = spy(new CompletionProcessor());
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
        doNothing().when(processor).checkLiterals(any());
        doNothing().when(processor).checkRegistrations(any());
        processor.indexes.add(10);
        
        processor.process(element);
        
        verify(processor).checkLiterals(element);
        verify(processor).checkRegistrations(element);
        
        assertTrue(processor.indexes.isEmpty());
    } 
    
    
    @ParameterizedTest
    @MethodSource("checkLiterals_parameters")
    void checkLiterals(Literal[] literals, int times) {
        doNothing().when(processor).check(any(), anyInt());
        when(element.getAnnotationsByType(Literal.class)).thenReturn(literals);
        
        processor.checkLiterals(element);
        
        verify(processor, times(times)).check(any(), anyInt());
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
        doNothing().when(processor).check(any(), anyInt());
        when(element.getAnnotationsByType(Registered.class)).thenReturn(registrations);
        
        processor.checkRegistrations(element);
        
        verify(processor, times(times)).check(any(), anyInt());
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
        processor.init(environment);
        processor.indexes.add(1);
        
        processor.check(element, index);
        
        verify(messager, times(boundTimes)).printMessage(ERROR, "Index out of bounds: " + index + ", index must be equal to or greater than 0", element);
        verify(messager, times(duplicateTimes)).printMessage(ERROR, "Conflicting indexes: " + index + ", indexes must be unique", element);
    }
    
}
