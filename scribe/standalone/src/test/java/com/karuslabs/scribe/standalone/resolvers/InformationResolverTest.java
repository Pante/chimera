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
package com.karuslabs.scribe.standalone.resolvers;

import com.karuslabs.scribe.annotations.Information;

import java.util.HashMap;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Information(authors = {"Pante"}, description = "description", url = "url", prefix = "prefix")
class InformationResolverTest {
    
    Element element = mock(Element.class);
    Messager messager = mock(Messager.class);
    InformationResolver resolver = new InformationResolver(messager);
    
    
    @Test
    void resolve() {
        when(element.getAnnotation(Information.class)).thenReturn(InformationResolverTest.class.getAnnotation(Information.class));
        var results = new HashMap<String, Object>();
        
        resolver.resolve(element, results);
        
        assertArrayEquals(new String[] {"Pante"}, (String[]) results.get("authors"));
        assertEquals("description", results.get("description"));
        assertEquals("url", results.get("website"));
        assertEquals("prefix", results.get("prefix"));
    }
    
    
    @Test
    void resolve_empty() {
        when(element.getAnnotation(Information.class)).thenReturn(EmptyInformation.class.getAnnotation(Information.class));
        var results = new HashMap<String, Object>();
        
        resolver.resolve(element, results);
    }
    
    @Information
    static class EmptyInformation {
        
    }

} 
