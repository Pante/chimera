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
package com.karuslabs.scribe.standalone;

import com.karuslabs.scribe.annotations.Plugin;

import java.util.*;
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
    
    Processor processor = new Processor();
    Messager messager = mock(Messager.class);
    Types types = mock(Types.class);
    Elements elements = mock(Elements.class);
    RoundEnvironment environment = mock(RoundEnvironment.class);
    
    
    @BeforeEach
    void before() {
        when(elements.getTypeElement(any())).thenReturn(mock(TypeElement.class));
    }
    
    
    @Test
    void init() {
        ProcessingEnvironment environment = mock(ProcessingEnvironment.class);
        when(environment.getMessager()).thenReturn(messager);
        when(environment.getTypeUtils()).thenReturn(types);
        when(environment.getElementUtils()).thenReturn(elements);
        
        processor.init(environment);
        
        assertEquals(8, processor.resolvers.size());
        assertNotNull(processor.writer);
    }
    
    
    @Test
    void process() {
        doReturn(Set.of(mock(Element.class))).when(environment).getElementsAnnotatedWithAny(Set.of(Plugin.class));
        when(environment.getElementsAnnotatedWith(Plugin.class)).thenReturn(Set.of());
        when(environment.processingOver()).thenReturn(false);
        
        Resolver resolver = mock(Resolver.class);
        processor.resolvers.put(Plugin.class, resolver);
        processor.writer = mock(YAMLWriter.class);
        
        assertFalse(processor.process(Set.of(), environment));
        
        verify(resolver).resolve(any(Set.class), any(Map.class));
        verify(processor.writer).write(any(Map.class));
    }
    
    
    @Test
    void process_empty() {
        when(environment.getElementsAnnotatedWithAny(Set.of())).thenReturn(Set.of());
        processor.writer = mock(YAMLWriter.class);
        
        assertFalse(processor.process(Set.of(), environment));
        
        verifyZeroInteractions(processor.writer);
    }

} 
