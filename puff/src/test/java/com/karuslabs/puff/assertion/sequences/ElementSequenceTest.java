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
package com.karuslabs.puff.assertion.sequences;

import com.karuslabs.puff.type.*;

import java.util.*;
import javax.lang.model.element.*;
import javax.lang.model.type.*;

import org.junit.jupiter.api.*;

import static com.karuslabs.puff.assertion.Assertions.*;
import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnyParameterTest {

    Sequence<VariableElement> sequence = ANY_PARAMETERS;
    TypeMirrors types = mock(TypeMirrors.class);
    VariableElement element = mock(VariableElement.class);
    DeclaredType annotation = when(mock(DeclaredType.class).accept(any(), any())).then(invocation -> invocation.getArgument(1, StringBuilder.class).append("A")).getMock();
    AnnotationMirror mirror = when(mock(AnnotationMirror.class).getAnnotationType()).thenReturn(annotation).getMock();
    
    @Test
    void test() {
        assertTrue(sequence.test(types, List.of(element)));
    }
    
    @Test
    void describe() {
        TypeMirror type = when(mock(TypeMirror.class).accept(any(TypePrinter.class), any(StringBuilder.class))).then(call -> call.getArgument(1, StringBuilder.class).append("Type")).getMock();
        when(element.asType()).thenReturn(type);
        when(element.getModifiers()).thenReturn(Set.of(PRIVATE));
        doReturn(List.of(mirror)).when(element).getAnnotationMirrors();
        
        assertEquals("private Type annotated with @A, private Type annotated with @A", sequence.describe(types, List.of(element, element)));
    }
    
    @Test
    void condition() {
        assertEquals("", sequence.condition());
    }
    
}

class AnyTypeTest {
    
    Sequence<TypeMirror> sequence = ANY_TYPES;
    TypeMirrors types = mock(TypeMirrors.class);
    TypeMirror type = when(mock(TypeMirror.class).accept(any(TypePrinter.class), any(StringBuilder.class))).then(call -> call.getArgument(1, StringBuilder.class).append("Type")).getMock();
    
    @Test
    void test() {
        assertTrue(sequence.test(types, List.of(type)));
    }
    
    @Test
    void describe() {
        assertEquals("Type", sequence.describe(types, List.of(type)));
    }
    
    @Test
    void condition() {
        assertEquals("", sequence.condition());
    }
    
}
