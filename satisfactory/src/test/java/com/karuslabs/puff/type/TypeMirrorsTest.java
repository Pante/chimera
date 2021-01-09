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
package com.karuslabs.Satisfactory.type;

import java.util.stream.Stream;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;

class TypeMirrorsTest {
 
    TypeMirror mirror = mock(TypeMirror.class);
    TypeMirror type = mock(TypeMirror.class);
    TypeElement element = when(mock(TypeElement.class).asType()).thenReturn(type).getMock();
    Elements elements = when(mock(Elements.class).getTypeElement(int.class.getName())).thenReturn(element).getMock();
    Types delegate = mock(Types.class);
    TypeMirrors types = new TypeMirrors(elements, delegate);
    
    
    @Test
    void is_primitive() {
        var type = mock(PrimitiveType.class);
        assertFalse(TypeMirrors.is(type, int.class));
    }
    
    @Test
    void is_variable() {
        var variable = mock(VariableElement.class);
        TypeMirror type = when(mock(DeclaredType.class).asElement()).thenReturn(variable).getMock();
        assertFalse(TypeMirrors.is(type, int.class));
    }
    
    @Test
    void is_declared_type() {
        Name name = when(mock(Name.class).contentEquals(String.class.getName())).thenReturn(true).getMock();
        TypeElement element = when(mock(TypeElement.class).getQualifiedName()).thenReturn(name).getMock();
        TypeMirror type = when(mock(DeclaredType.class).asElement()).thenReturn(element).getMock();
        assertTrue(TypeMirrors.is(type, String.class));
    }
    
    
    @ParameterizedTest
    @MethodSource("kind_parameters")
    void kind(TypeKind expected, Class<?> type) {
        assertEquals(expected, TypeMirrors.kind(type));
    }
    
    static Stream<Arguments> kind_parameters() {
        return Stream.of(
            of(TypeKind.BOOLEAN, boolean.class),
            of(TypeKind.BYTE, byte.class),
            of(TypeKind.SHORT, short.class),
            of(TypeKind.INT, int.class),
            of(TypeKind.LONG, long.class),
            of(TypeKind.FLOAT, float.class),
            of(TypeKind.DOUBLE, double.class),
            of(TypeKind.CHAR, char.class),
            of(TypeKind.VOID, void.class),
            of(TypeKind.DECLARED, Object.class)
        );
    }
    
    
    @Test
    void element_type() {
        when(delegate.asElement(type)).thenReturn(element);
        
        assertEquals(element, types.element(type));
    }
    
    @Test
    void element_primitive() {
        when(delegate.asElement(type)).thenReturn(mock(Element.class));
        
        assertNull(types.element(type));
    }
    
    @Test
    void box_primitive() {
        var primitive = mock(PrimitiveType.class);
        when(delegate.boxedClass(primitive)).thenReturn(element);
        
        assertEquals(type, types.box(primitive));
        verify(delegate, times(1)).boxedClass(primitive);
    }
    
    @Test
    void box_non_primitive() {
        assertSame(type, types.box(type));
        verify(delegate, never()).boxedClass(any());
    }
    
    @Test
    void type() {
        assertEquals(type, types.type(int.class));
        verifyNoInteractions(delegate);
    }
    
    @Test
    void erasure_class() {
        when(delegate.erasure(any(TypeMirror.class))).thenReturn(mirror);
        
        assertEquals(mirror, types.erasure(int.class));
        verify(delegate).erasure(type);
    }
    
    @Test
    void specialize_classes() {
        var declared = mock(DeclaredType.class);
        when(delegate.getDeclaredType(element, mirror)).thenReturn(declared);
        
        TypeElement string = when(mock(TypeElement.class).asType()).thenReturn(mirror).getMock();
        when(elements.getTypeElement(String.class.getName())).thenReturn(string);
        
        assertEquals(declared, types.specialize(int.class, String.class));
    }
    
    
    @Test
    void asElement() {
        types.asElement(type);
        verify(delegate).asElement(type);
    }
    
    @Test
    void isSameType() {
        types.isSameType(type, mirror);
        verify(delegate).isSameType(type, mirror);
    }
    
    @Test
    void isSubtype() {
        types.isSubtype(type, mirror);
        verify(delegate).isSubtype(type, mirror);
    }
    
    @Test
    void isAssignable() {
        types.isAssignable(type, mirror);
        verify(delegate).isAssignable(type, mirror);
    }
    
    @Test
    void contains() {
        types.contains(type, mirror);
        verify(delegate).contains(type, mirror);
    }
    
    @Test
    void isSubsignature() {
        var first = mock(ExecutableType.class);
        var second = mock(ExecutableType.class);
        types.isSubsignature(first, second);
        verify(delegate).isSubsignature(first, second);
    }
    
    @Test
    void directSupertypes() {
        types.directSupertypes(type);
        verify(delegate).directSupertypes(type);
    }
    
    @Test
    void erasure_typemirror() {
        types.erasure(type);
        verify(delegate).erasure(type);
    }
    
    @Test
    void boxedClass() {
        var type = mock(PrimitiveType.class);
        types.boxedClass(type);
        verify(delegate).boxedClass(type);
    }
    
    @Test
    void unboxedType() {
        types.unboxedType(type);
        verify(delegate).unboxedType(type);
    }
    
    @Test
    void capture() {
        types.capture(type);
        verify(delegate).capture(type);
    }
    
    @Test
    void getPrimitiveType() {
        types.getPrimitiveType(TypeKind.INT);
        verify(delegate).getPrimitiveType(TypeKind.INT);
    }
    
    @Test
    void getNullType() {
        types.getNullType();
        verify(delegate).getNullType();
    }
    
    @Test
    void getNoType() {
        types.getNoType(TypeKind.INT);
        verify(delegate).getNoType(TypeKind.INT);
    }
    
    @Test
    void getArrayType() {
        types.getArrayType(type);
        verify(delegate).getArrayType(type);
    }
    
    @Test
    void getWildcardType() {
        types.getWildcardType(type, mirror);
        verify(delegate).getWildcardType(type, mirror);
    }
    
    @Test
    void getDeclaredType() {
        types.getDeclaredType(element, type, mirror);
        verify(delegate).getDeclaredType(element, type, mirror);
    }
    
    @Test
    void getDeclaredType_nested() {
        var declared = mock(DeclaredType.class);
        types.getDeclaredType(declared, element, type, mirror);
        verify(delegate).getDeclaredType(declared, element, type, mirror);
    }
    
    @Test
    void asMemberOf() {
        var declared = mock(DeclaredType.class);
        types.asMemberOf(declared, element);
        verify(delegate).asMemberOf(declared, element);
    }
    
}
