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
package com.karuslabs.puff.type;

import java.util.List;
import javax.lang.model.type.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalkerTest {
    
    Walker<?> walker = Walker.ancestor(mock(TypeMirrors.class));
    
    @Test
    void defaultAction() {
        assertNull(walker.defaultAction(null, null));
    }

}

class AncestorWalkerTest {
    
    TypeMirror object = mock(TypeMirror.class);
    TypeMirror ancestor = mock(TypeMirror.class);
    DeclaredType type = mock(DeclaredType.class);
    DeclaredType parent = mock(DeclaredType.class);
    TypeMirrors types = when(mock(TypeMirrors.class).type(Object.class)).thenReturn(object).getMock();
    Walker<TypeMirror> walker = Walker.ancestor(types);
    
    @Test
    void visitDeclared_same() {
        when(types.isSameType(type, ancestor)).thenReturn(true);
        assertEquals(type, walker.visitDeclared(type, ancestor));
    }
    
    @Test
    void visitDeclared_object() {
        when(types.isSameType(type, ancestor)).thenReturn(false);
        when(types.isSameType(type, object)).thenReturn(true);
        
        assertNull(walker.visitDeclared(type, ancestor));
    }
    
    @Test
    void visitDeclared_found_ancestor() {
        doReturn(List.of(parent)).when(types).directSupertypes(type);
        when(parent.accept(walker, ancestor)).thenReturn(parent);
        
        when(types.isSameType(type, ancestor)).thenReturn(false);
        when(types.isSameType(type, object)).thenReturn(false);
        
        assertEquals(parent, walker.visitDeclared(type, ancestor));
    }
    
    @Test
    void visitDeclared_no_ancestor() {
        doReturn(List.of(parent)).when(types).directSupertypes(type);
        when(parent.accept(walker, ancestor)).thenReturn(null);
        
        when(types.isSameType(type, ancestor)).thenReturn(false);
        when(types.isSameType(type, object)).thenReturn(false);
        
        assertNull(walker.visitDeclared(type, ancestor));
    }
    
}
