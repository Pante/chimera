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

import com.karuslabs.scribe.annotations.Load;

import java.util.HashMap;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.karuslabs.scribe.annotations.Phase.*;
import static javax.tools.Diagnostic.Kind.ERROR;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Load(during = STARTUP, before = {"a"}, optionallyAfter = {"b"}, after = {"c"})
class LoadResolverTest {
    
    Element element = mock(Element.class);
    Messager messager = mock(Messager.class);
    LoadResolver resolver = new LoadResolver(messager);
    
    
    @Test
    void resolve() {
        when(element.getAnnotation(Load.class)).thenReturn(LoadResolverTest.class.getAnnotation(Load.class));
        var results = new HashMap<String, Object>();
        
        resolver.resolve(element, results);
        
        assertEquals(4, results.size());
        assertEquals("STARTUP", results.get("load"));
        assertArrayEquals(new String[] {"a"}, (String[]) results.get("loadbefore"));
        assertArrayEquals(new String[] {"b"}, (String[]) results.get("softdepend"));
        assertArrayEquals(new String[] {"c"}, (String[]) results.get("depend"));
    }
    
    
    @Test
    void resolve_empty() {
        when(element.getAnnotation(Load.class)).thenReturn(EmptyLoad.class.getAnnotation(Load.class));
        var results = new HashMap<String, Object>();
        
        resolver.resolve(element, results);
        
        assertEquals(4, results.size());
        assertEquals("POSTWORLD", results.get("load"));
        assertArrayEquals(new String[] {}, (String[]) results.get("loadbefore"));
        assertArrayEquals(new String[] {}, (String[]) results.get("softdepend"));
        assertArrayEquals(new String[] {}, (String[]) results.get("depend"));
    }
    
    @Load
    static class EmptyLoad {
        
    }
    
    @Test
    void check_errors() {
        resolver.check(element, new String[] {"a1", "hey!", "sp ace"});
        
        verify(messager, times(2)).printMessage(any(Kind.class), any(String.class), any(Element.class));
        verify(messager).printMessage(ERROR, "Invalid name: 'hey!', name must contain only alphanumeric characters and '_'", element);
        verify(messager).printMessage(ERROR, "Invalid name: 'sp ace', name must contain only alphanumeric characters and '_'", element);
    }

} 
