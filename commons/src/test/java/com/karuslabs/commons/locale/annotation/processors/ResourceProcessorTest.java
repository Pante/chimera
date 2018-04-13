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
package com.karuslabs.commons.locale.annotation.processors;

import com.karuslabs.commons.locale.annotation.*;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.util.Collections.singleton;
import static javax.tools.Diagnostic.Kind.ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class ResourceProcessorTest {
    
    ResourceProcessor processor = spy(new ResourceProcessor());
    Messager messager = mock(Messager.class);
    ProcessingEnvironment environment = when(mock(ProcessingEnvironment.class).getMessager()).thenReturn(messager).getMock();
    TypeMirror mirror = mock(TypeMirror.class);
    TypeElement element = when(mock(TypeElement.class).asType()).thenReturn(mirror).getMock();
    
    
    @Test
    void annotations() throws ClassNotFoundException {
        for (String supported: ResourceProcessor.class.getAnnotation(SupportedAnnotationTypes.class).value()) {
            assertTrue(Class.forName(supported).isAnnotation());
        }
    }
    
    
    @Test
    void process() {
        when(element.getAnnotation(EmbeddedResources.class)).thenReturn(new EmbeddedResources() {
            @Override
            public String[] value() {
                return new String[] {"a"};
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return EmbeddedResources.class;
            }
        });
        
        when(element.getAnnotation(ExternalResources.class)).thenReturn(new ExternalResources() {
            @Override
            public String[] value() {
                return new String[] {"b"};
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return ExternalResources.class;
            }
        });
        
        processor.init(environment);
        processor.process(element);
        
        verify(messager).printMessage(ERROR, "Missing bundle name for: " + element.asType().toString(), element);
        verify(processor, times(2)).process(eq(element), any());
    }
    
    
    @ParameterizedTest
    @MethodSource("process_parameters")
    void process(String[] values, int times) {
        processor.init(environment);
        
        processor.process(element, values);
        verify(messager, times(times)).printMessage(eq(ERROR), any(String.class), eq(element));
    }
    
    static Stream<Arguments> process_parameters() {
        return Stream.of(
            of(new String[] {}, 1),
            of(new String[] {"hey", "there", "hey"}, 1),
            of(new String[] {"there", "theree"}, 0),
            of(new String[] {"a", "b"}, 0)
        );
    }
    
}
