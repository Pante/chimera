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
package com.karuslabs.puff.assertion.matches;

import com.karuslabs.puff.type.*;

import javax.lang.model.element.Element;
import javax.lang.model.type.*;

import org.junit.jupiter.api.*;

import org.mockito.MockedStatic;

import static com.karuslabs.puff.assertion.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TypeMatchTest {

    Match<TypeMirror> match = spy(ANY_TYPE);
    TypeMirrors types = mock(TypeMirrors.class);
    TypeMirror type = mock(TypeMirror.class);
    Element element = when(mock(Element.class).asType()).thenReturn(type).getMock();
    MockedStatic scope;
    
    @BeforeEach
    void before() {
        scope = mockStatic(TypePrinter.class);
        scope.when(() -> TypePrinter.simple(type)).thenReturn("type");
    }
    
    @Test
    void is_type() {
        when(type.getKind()).thenReturn(TypeKind.BOOLEAN);
        assertTrue(is(type) instanceof PrimitiveMatch);
    }
    
    @Test
    void is_type_false() {
        when(type.getKind()).thenReturn(TypeKind.DECLARED);
        assertFalse(is(type).test(types, type));
    }
    
    @Test
    void test_element() {
        assertTrue(match.test(types, element));
        verify(match).test(types, type);
    }
    
    @Test
    void describe_element() {
        assertEquals("type", match.describe(element));
    }
    
    @Test
    void describe_value() {
        assertEquals("type", match.describe(type));
    }
    
    @AfterEach
    void after() {
        scope.close();
    }
    
}

class AnyMatchTest {
    
    Match<TypeMirror> match = ANY_TYPE;
    
    @Test
    void test_type() {
        assertTrue(match.test(mock(TypeMirrors.class), mock(TypeMirror.class)));
    }
    
    @Test
    void condition() {
        assertEquals("type", match.condition());
    }
    
    @Test
    void conditions() {
        assertEquals("types", match.conditions());
    }
    
}

class PrimitiveMatchTest {
    
    Match<TypeMirror> match = spy(is(int.class));
    TypeMirrors types = mock(TypeMirrors.class);
    TypeMirror type = when(mock(TypeMirror.class).getKind()).thenReturn(TypeKind.INT).getMock();
    Element element = when(mock(Element.class).asType()).thenReturn(type).getMock();
    
    @Test
    void test_element() {
        assertTrue(match.test(types, element));
        verify(match).test(types, type);
    }
    
    @Test
    void test_type_boolean() {
        when(type.getKind()).thenReturn(TypeKind.BOOLEAN);
        assertFalse(match.test(types, type));
    }
    
    @Test
    void describe_element() {
        try (var scope = mockStatic(TypePrinter.class)) {
            scope.when(() -> TypePrinter.simple(type)).thenReturn("int");
            match.describe(element);
            verify(match).describe(type);
        }
    }
    
    @Test
    void condition() {
        assertEquals("int", match.condition());
    }
    
    @Test
    void conditions() {
        assertEquals("ints", match.conditions());
    }
    
}

class ClassMatchTest {
    
    Match<TypeMirror> is = is(String.class);
    Match<TypeMirror> subtype = subtype(String.class, Object.class);
    Match<TypeMirror> supertype = supertype(String.class, Object.class);
    
    TypeMirrors types = mock(TypeMirrors.class);
    TypeMirror type = mock(TypeMirror.class);
    TypeMirror string = mock(TypeMirror.class);
    TypeMirror object = mock(TypeMirror.class);
    
    @BeforeEach
    void before() {
        when(types.type(String.class)).thenReturn(string);
        when(types.type(Object.class)).thenReturn(object);
    }
    
    
    @Test
    void is_test_type() {
        when(types.isSameType(type, string)).thenReturn(true);
        assertTrue(is.test(types, type));
    }
    
    @Test
    void subtype_test_type() {
        when(types.isSubtype(type, string)).thenReturn(true);
        when(types.isSubtype(type, object)).thenReturn(true);
        assertTrue(subtype.test(types, type));
    }
    
    @Test
    void supertype_test_type() {
        when(types.isSubtype(string, type)).thenReturn(true);
        when(types.isSubtype(object, type)).thenReturn(true);
        assertTrue(supertype.test(types, type));
    }
    
    
    @Test
    void is_condition() {
        assertEquals("String", is.condition());
    }
    
    @Test
    void is_conditions() {
        assertEquals("String", is.conditions());
    }
    
    
    @Test
    void subtype_condition() {
        assertEquals("subtype of String and Object", subtype.condition());
    }
    
    @Test
    void subtype_conditions() {
        assertEquals("subtypes of String and Object", subtype.conditions());
    }
    
    
    @Test
    void supertype_condition() {
        assertEquals("supertype of String and Object", supertype.condition());
    }
    
    @Test
    void supertypes_condition() {
        assertEquals("supertypes of String and Object", supertype.conditions());
    }
    
}

class TypeMirrorMatchTest {
    
    TypeMirrors types = mock(TypeMirrors.class);
    TypeMirror type = mock(TypeMirror.class);
    TypeMirror string = when(mock(TypeMirror.class).getKind()).thenReturn(TypeKind.DECLARED).getMock();
    TypeMirror object = mock(TypeMirror.class);
    
    Match<TypeMirror> is;
    Match<TypeMirror> subtype;
    Match<TypeMirror> supertype;
    
    @BeforeEach
    void before() {
        when(string.accept(any(TypePrinter.class), any(StringBuilder.class))).then(invocation -> invocation.getArgument(1, StringBuilder.class).append("String"));
        when(object.accept(any(TypePrinter.class), any(StringBuilder.class))).then(invocation -> invocation.getArgument(1, StringBuilder.class).append("Object"));
        
        is = is(string);
        subtype = subtype(string, object);
        supertype = supertype(string, object);
    }
    
    @Test
    void is_test_type() {
        when(types.isSameType(type, string)).thenReturn(true);
        assertTrue(is.test(types, type));
    }
    
    @Test
    void subtype_test_type() {
        when(types.isSubtype(type, string)).thenReturn(true);
        when(types.isSubtype(type, object)).thenReturn(true);
        assertTrue(subtype.test(types, type));
    }
    
    @Test
    void supertype_test_type() {
        when(types.isSubtype(string, type)).thenReturn(true);
        when(types.isSubtype(object, type)).thenReturn(true);
        assertTrue(supertype.test(types, type));
    }
    
    
    @Test
    void is_condition() {
        assertEquals("String", is.condition());
    }
    
    @Test
    void is_conditions() {
        assertEquals("String", is.conditions());
    }
    
    
    @Test
    void subtype_condition() {
        assertEquals("subtype of String and Object", subtype.condition());
    }
    
    @Test
    void subtype_conditions() {
        assertEquals("subtypes of String and Object", subtype.conditions());
    }
    
    
    @Test
    void supertype_condition() {
        assertEquals("supertype of String and Object", supertype.condition());
    }
    
    @Test
    void supertypes_condition() {
        assertEquals("supertypes of String and Object", supertype.conditions());
    }
    
}
