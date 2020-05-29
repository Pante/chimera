/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.scribe.standalone;

import com.karuslabs.scribe.core.Environment;

import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.util.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ProcessorTest {
    
    ProcessingEnvironment env = mock(ProcessingEnvironment.class);
    Elements elements = mock(Elements.class);
    Types types = mock(Types.class);
    TypeElement element = mock(TypeElement.class);
    Filer filer = mock(Filer.class);
    Messager messager = mock(Messager.class);
    Processor processor = spy(new Processor());
    Environment<Element> environment;
    
    
    @BeforeEach
    void before() {
        when(env.getElementUtils()).thenReturn(elements);
        when(env.getTypeUtils()).thenReturn(types);
        when(env.getFiler()).thenReturn(filer);
        when(env.getMessager()).thenReturn(messager);
        
        when(elements.getTypeElement(any())).thenReturn(element);
        
        processor.init(env);
        environment = processor.environment;
    }
    
    
    @Test
    void init() {
        assertNotNull(processor.processor);
        assertNotNull(processor.yaml);
    }
    
    
    @Test
    void process() {
        RoundEnvironment round = when(mock(RoundEnvironment.class).getElementsAnnotatedWithAny(any(Set.class))).thenReturn(Set.of(element)).getMock();
        when(round.processingOver()).thenReturn(false);
        
        processor.processor = mock(StandaloneProcessor.class);
        processor.yaml = mock(StandaloneYAML.class);
        
        assertFalse(processor.process(Set.of(), round));
        verify(processor.yaml).write(any());
    }
    
    
    @Test
    void process_over() {
        RoundEnvironment round = when(mock(RoundEnvironment.class).getElementsAnnotatedWithAny(any(Set.class))).thenReturn(Set.of(element)).getMock();
        when(round.processingOver()).thenReturn(true);
        
        processor.processor = mock(StandaloneProcessor.class);
        processor.yaml = mock(StandaloneYAML.class);
        
        assertFalse(processor.process(Set.of(), round));
        verify(processor.yaml, times(0)).write(any());
    }
    
    
    @Test
    void process_empty() {
        RoundEnvironment environment = when(mock(RoundEnvironment.class).getElementsAnnotatedWithAny(any(Set.class))).thenReturn(Set.of()).getMock();
        processor.processor = mock(StandaloneProcessor.class);
        
        assertFalse(processor.process(Set.of(), environment));
        verify(processor.processor, times(0)).initialize(any());
    }

} 
