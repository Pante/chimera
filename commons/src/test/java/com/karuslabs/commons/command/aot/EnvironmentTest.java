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
package com.karuslabs.commons.command.aot;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;

import org.junit.jupiter.api.Test;

import static javax.tools.Diagnostic.Kind.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class EnvironmentTest {
    
    Environment environment = new Environment(mock(Messager.class), null, null, null);
    Element element = mock(Element.class);
    
    
    @Test
    void scope() {
        var root = environment.scope(element);
        
        assertEquals(Type.ROOT, root.type);
        assertSame(root, environment.scopes.get(element));
    }
    
    
    @Test
    void clear() {
        environment.scopes.put(element, Token.root());
        environment.error = true;
        
        environment.clear();
        
        assertTrue(environment.scopes.isEmpty());
        assertFalse(environment.error());
    }
    
    
    @Test
    void error() {
        environment.error = false;
        
        environment.error(element, "error message");
        
        assertTrue(environment.error());
        verify(environment.messager).printMessage(ERROR, "error message", element);
    }
    
    
    @Test
    void warn() {
        environment.error = false;
        
        environment.warn(element, "warning message");
        
        assertFalse(environment.error());
        verify(environment.messager).printMessage(WARNING, "warning message", element);
    }

} 
