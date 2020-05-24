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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProcessorTest {
    
    ProcessingEnvironment environment = mock(ProcessingEnvironment.class);
    Elements elements = mock(Elements.class);
    Types types = mock(Types.class);
    TypeElement element = mock(TypeElement.class);
    Filer filer = mock(Filer.class);
    Messager messager = mock(Messager.class);
    Processor processor = spy(new Processor());
    Environment<Element> resolution;
    
    
    @BeforeEach
    void before() {
        when(environment.getElementUtils()).thenReturn(elements);
        when(environment.getTypeUtils()).thenReturn(types);
        when(environment.getFiler()).thenReturn(filer);
        when(environment.getMessager()).thenReturn(messager);
        
        when(elements.getTypeElement(any())).thenReturn(element);
        
        processor.init(environment);
        resolution = new Environment<Element>();
    }
    
    
    @Test
    void init() {
        assertNotNull(processor.processor);
        assertNotNull(processor.yaml);
    }
    
    
    @Test
    void process() {
        RoundEnvironment environment = when(mock(RoundEnvironment.class).getElementsAnnotatedWithAny(any(Set.class))).thenReturn(Set.of(element)).getMock();
        when(environment.processingOver()).thenReturn(false);
        
        processor.processor = when(mock(StandaloneProcessor.class).run()).thenReturn(resolution).getMock();
        processor.yaml = mock(StandaloneYAML.class);
        resolution.error(element, "error").warning(element, "warning").info(element, "info");
        
        assertFalse(processor.process(Set.of(), environment));
        verify(processor).error(element, "error");
        verify(processor).warn(element, "warning");
        verify(processor).note(element, "info");
    }
    
    
    @Test
    void process_over() {
        RoundEnvironment environment = when(mock(RoundEnvironment.class).getElementsAnnotatedWithAny(any(Set.class))).thenReturn(Set.of(element)).getMock();
        when(environment.processingOver()).thenReturn(true);
        
        processor.processor = when(mock(StandaloneProcessor.class).run()).thenReturn(resolution).getMock();
        processor.yaml = mock(StandaloneYAML.class);
        resolution.error(element, "error").warning(element, "warning").info(element, "info");
        
        assertFalse(processor.process(Set.of(), environment));
        verify(processor, times(0)).error(element, "error");
        verify(processor, times(0)).warn(element, "warning");
        verify(processor, times(0)).note(element, "info");
    }
    
    
    @Test
    void process_empty() {
        RoundEnvironment environment = when(mock(RoundEnvironment.class).getElementsAnnotatedWithAny(any(Set.class))).thenReturn(Set.of()).getMock();
        processor.processor = mock(StandaloneProcessor.class);
        
        assertFalse(processor.process(Set.of(), environment));
        verify(processor.processor, times(0)).initialize(any());
    }

} 
