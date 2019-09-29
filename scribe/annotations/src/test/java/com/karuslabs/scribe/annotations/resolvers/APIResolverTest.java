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
package com.karuslabs.scribe.annotations.resolvers;

import com.karuslabs.scribe.annotations.API;
import com.karuslabs.scribe.annotations.Version;

import java.util.HashMap;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@API(Version.V1_13)
class APIResolverTest {
    
    Messager messager = mock(Messager.class);
    APIResolver resolver = new APIResolver(messager);
    Element element = mock(Element.class);
    
    
    @Test
    void resolve() {
        when(element.getAnnotation(API.class)).thenReturn(getClass().getAnnotation(API.class));
        var results = new HashMap<String, Object>();
        resolver.resolve(element, results);
        
        assertEquals(1, results.size());
        assertEquals("1.13", results.get("api-version"));
    }

} 
