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
package com.karuslabs.scribe.core;

import com.karuslabs.scribe.annotations.Command;

import javax.lang.model.element.Element;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Command(name = "a")
@Command(name = "b")
class ExtractorTest {
    
    @Nested
    class ClassTest {
        
        @Test
        void all() {
            assertArrayEquals(ExtractorTest.class.getAnnotationsByType(Command.class), Extractor.CLASS.all(ExtractorTest.class, Command.class));
        }
        
        @Test
        void single() {
            assertEquals(ExtractorTest.class.getAnnotation(Command.class), Extractor.CLASS.single(Extractor.class, Command.class));
        }
        
    }
    
    @Nested
    class ElementTest {
        
        Element element = mock(Element.class);
        
        @Test
        void all() {
            Extractor.ELEMENT.all(element, Command.class);
            
            verify(element).getAnnotationsByType(Command.class);
        }
        
        @Test
        void single() {
            Extractor.ELEMENT.single(element, Command.class);
            
            verify(element).getAnnotation(Command.class);
        }
        
    }

} 
