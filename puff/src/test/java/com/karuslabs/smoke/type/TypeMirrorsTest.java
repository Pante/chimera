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
package com.karuslabs.smoke.type;

import com.karuslabs.puff.type.TypeMirrors;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TypeMirrorsTest {
 
    TypeMirror mirror = mock(TypeMirror.class);
    TypeMirror type = mock(TypeMirror.class);
    TypeElement element = when(mock(TypeElement.class).asType()).thenReturn(type).getMock();
    Elements elements = when(mock(Elements.class).getTypeElement(int.class.getName())).thenReturn(element).getMock();
    Types types = mock(Types.class);
    TypeMirrors typing = new TypeMirrors(elements, types);
    
    @Test
    void element_type() {
        when(types.asElement(type)).thenReturn(element);
        
        assertEquals(element, typing.element(type));
    }
    
    @Test
    void element_primitive() {
        when(types.asElement(type)).thenReturn(mock(Element.class));
        
        assertNull(typing.element(type));
    }
    
    @Test
    void box_primitive() {
        var primitive = mock(PrimitiveType.class);
        when(types.boxedClass(primitive)).thenReturn(element);
        
        assertEquals(type, typing.box(primitive));
        verify(types, times(1)).boxedClass(primitive);
    }
    
    @Test
    void box_non_primitive() {
        assertSame(type, typing.box(type));
        verify(types, never()).boxedClass(any());
    }
    
    @Test
    void type() {
        assertEquals(type, typing.type(int.class));
        verifyNoInteractions(types);
    }
    
    @Test
    void erasure() {
        when(types.erasure(any(TypeMirror.class))).thenReturn(mirror);
        
        assertEquals(mirror, typing.erasure(int.class));
        verify(types).erasure(type);
    }
    
    @Test
    void specialize_classes() {
        var declared = mock(DeclaredType.class);
        when(types.getDeclaredType(element, mirror)).thenReturn(declared);
        
        TypeElement string = when(mock(TypeElement.class).asType()).thenReturn(mirror).getMock();
        when(elements.getTypeElement(String.class.getName())).thenReturn(string);
        
        assertEquals(declared, typing.specialize(int.class, String.class));
    }
    
}
