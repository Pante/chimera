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

import com.karuslabs.puff.type.TypeMirrors;

import java.lang.annotation.*;
import java.util.List;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;

import org.junit.jupiter.api.*;

import org.checkerframework.checker.nullness.qual.Nullable;

import static com.karuslabs.puff.assertion.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnnotationMatchTest {

    Match<Class<? extends Annotation>> match = ANY_ANNOTATION;
    Element element = mock(Element.class);
    DeclaredType type = when(mock(DeclaredType.class).accept(any(), any())).then(invocation -> invocation.getArgument(1, StringBuilder.class).append("Type")).getMock();
    AnnotationMirror annotation = when(mock(AnnotationMirror.class).getAnnotationType()).thenReturn(type).getMock();
    
    @Test
    void describe_element_empty() {
        when(element.getAnnotationMirrors()).thenReturn(List.of());
        assertEquals("", match.describe(element));
    }
    
    @Test
    void describe_element() {
        doReturn(List.of(annotation, annotation)).when(element).getAnnotationMirrors();
        assertEquals("@Type @Type", match.describe(element));
    }
    
    
    @Test
    void describe_class() {
        assertEquals("@Nullable", match.describe(Nullable.class));
    }
    
}

class AnyAnnotationTest {
    
    Match<Class<? extends Annotation>> match = ANY_ANNOTATION;
    TypeMirrors types = mock(TypeMirrors.class);
    Element element = mock(Element.class);
    
    @Test
    void test_element() {
        assertTrue(match.test(types, element));
    }
    
    @Test
    void test_value() {
        assertTrue(match.test(types, Nullable.class));
    }
    
    @Test
    void condition() {
        assertEquals("", match.condition());
    }
    
    @Test
    void conditions() {
        assertEquals("", match.conditions());
    }
    
}

@Retention(RetentionPolicy.RUNTIME)
@interface A {}

@Retention(RetentionPolicy.RUNTIME)
@interface B {}

@Retention(RetentionPolicy.RUNTIME)
@interface C {}

@A @B @C
class ContainsAnnotationTest {
    
    Match<Class<? extends Annotation>> match = contains(A.class, B.class);
    TypeMirrors types = mock(TypeMirrors.class);
    Element element = mock(Element.class);
    
    @BeforeEach
    void before() {
        when(element.getAnnotationsByType(A.class)).thenReturn(ContainsAnnotationTest.class.getAnnotationsByType(A.class));
        when(element.getAnnotationsByType(B.class)).thenReturn(ContainsAnnotationTest.class.getAnnotationsByType(B.class));
        when(element.getAnnotationsByType(C.class)).thenReturn(ContainsAnnotationTest.class.getAnnotationsByType(C.class));
    }
    
    @Test
    void test_element() {
        assertTrue(match.test(types, element));
    }
    
    @Test
    void test_element_no_annotaiton() {
        when(element.getAnnotationsByType(B.class)).thenReturn(new B[0]);
        assertFalse(match.test(types, element));
    }
    
    
    @Test
    void test_class() {
        assertTrue(match.test(types, A.class));
    }
    
    @Test
    void test_class_no_annotation() {
        assertFalse(match.test(types, C.class));
    }
    
    @Test
    void condition() {
        assertEquals("@A @B", match.condition());
    }
    
    @Test
    void conditions() {
        assertEquals("@A @B", match.conditions());
    }
    
}

@A @B @C
class NoAnnotationTest {
    
    Match<Class<? extends Annotation>> match = no(A.class, B.class);
    TypeMirrors type = mock(TypeMirrors.class);
    Element element = mock(Element.class);
    
    @BeforeEach
    void before() {
        when(element.getAnnotationsByType(A.class)).thenReturn(ContainsAnnotationTest.class.getAnnotationsByType(A.class));
        when(element.getAnnotationsByType(B.class)).thenReturn(new B[0]);
        when(element.getAnnotationsByType(C.class)).thenReturn(ContainsAnnotationTest.class.getAnnotationsByType(C.class));
    }
    
    @Test
    void test_element() {
        when(element.getAnnotationsByType(A.class)).thenReturn(new A[0]);
        
        assertTrue(match.test(type, element));
    }
    
    @Test
    void test_element_no_annotation() {
        assertFalse(match.test(type, element));
    }
    
    
    @Test
    void test_class() {
        assertTrue(match.test(type, C.class));
    }
    
    @Test
    void test_class_no_annotation() {
        assertFalse(match.test(type, A.class));
    }
    
    
    @Test
    void condition() {
        assertEquals("neither @A nor @B", match.condition());
    }
    
    @Test
    void conditions() {
        assertEquals("neither @A nor @B", match.condition());
    }
    
}
