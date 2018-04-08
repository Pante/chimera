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

import com.karuslabs.commons.command.CommandExecutor;

import java.lang.annotation.Annotation;
import java.util.Set;
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
import static org.mockito.Mockito.*;


class CommandProcessorTest {
    
    CommandProcessor processor;
    TypeElement element;
    Elements elements;
    Types types;
    Messager messager;
    ProcessingEnvironment environment;
    
    
    CommandProcessorTest() {
        processor = spy(new CommandProcessor());
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
        processor.init(environment);
        
        assertSame(element.asType(), processor.getExpected());
        assertSame(messager, processor.getMessager());
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
        doNothing().when(processor).checkAssignability(any());
        
        boolean processed = processor.process(set, environment);
        
        verify(processor).checkAssignability(any());
        assertFalse(processed);
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, 0", "false, 1"})
    void checkAssignability(boolean assignable, int times) {
        when(types.isAssignable(any(), any())).thenReturn(assignable);
        
        processor.init(environment);
        processor.checkAssignability(element);
        
        verify(messager, times(times)).printMessage(ERROR, "Invalid annotated type: " + element.asType().toString() + ", type must implement " + CommandExecutor.class.getName() , element);
    }

}
