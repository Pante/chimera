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
package com.karuslabs.puff;

import javax.annotation.processing.*;
import javax.lang.model.element.*;

import org.junit.jupiter.api.*;
import org.mockito.junit.jupiter.*;

import static org.mockito.quality.Strictness.LENIENT;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = LENIENT)
class ElementProcessorTest {
    
    ElementProcessor processor = spy(new ElementProcessor() {
        @Override
        protected void process(Element element) {}
    });
    Messager messager = mock(Messager.class);
    ProcessingEnvironment environment = when(mock(ProcessingEnvironment.class).getMessager()).thenReturn(messager).getMock();
    Element element = mock(Element.class);
    
    
    @BeforeEach
    void before() {
        processor.init(environment);
    }
    
    @Test
    void process() {
        RoundEnvironment round = mock(RoundEnvironment.class);
        doReturn(Set.of(mock(Element.class))).when(round).getElementsAnnotatedWithAny(any(TypeElement[].class));
        
        assertFalse(processor.process(Set.of(), round));
    }

} 
