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
package com.karuslabs.puff.assertion;

import com.karuslabs.puff.type.*;

import java.util.*;
import java.util.stream.Stream;
import javax.lang.model.element.*;
import javax.lang.model.type.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static com.karuslabs.puff.assertion.Assertions.*;
import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;

class MethodTest {

    Method method = method().get();
    TypeMirrors types = mock(TypeMirrors.class);
    ExecutableElement element = mock(ExecutableElement.class);
    Set<Modifier> modifiers = Set.of(PRIVATE);
    TypeMirror type = when(mock(TypeMirror.class).getKind()).thenReturn(TypeKind.INT).getMock();
    DeclaredType annotation = when(mock(DeclaredType.class).accept(any(), any())).then(call -> call.getArgument(1, StringBuilder.class).append("A")).getMock();
    AnnotationMirror mirror = when(mock(AnnotationMirror.class).getAnnotationType()).thenReturn(annotation).getMock();
    VariableElement variable = when(mock(VariableElement.class).getModifiers()).thenReturn(Set.of(PUBLIC)).getMock();
    DeclaredType exception = when(mock(DeclaredType.class).accept(any(), any())).then(call -> call.getArgument(1, StringBuilder.class).append("Exception")).getMock();
    
    @BeforeEach
    void before() {
        when(type.accept(any(TypePrinter.class), any(StringBuilder.class))).then(call -> call.getArgument(1, StringBuilder.class).append("int"));
        when(type.getKind()).thenReturn(TypeKind.INT);
        when(exception.getKind()).thenReturn(TypeKind.DECLARED);
        
        when(variable.asType()).thenReturn(type);
    
        when(element.getModifiers()).thenReturn(modifiers);
        when(element.getReturnType()).thenReturn(type);
        when(element.getAnnotationsByType(B.class)).thenReturn(new B[0]);
        when(element.getAnnotationsByType(A.class)).thenReturn(MethodTest.class.getAnnotationsByType(A.class));
        doReturn(List.of(mirror)).when(element).getAnnotationMirrors();
        doReturn(List.of(variable)).when(element).getParameters();
        doReturn(List.of(exception)).when(element).getThrownTypes();
    }
    
    @ParameterizedTest
    @MethodSource("test_parameters")
    void test(boolean expected, Method.Builder method) {
        assertEquals(expected, method.get().test(types, element));
    }
    
    static Stream<Arguments> test_parameters() {
        return Stream.of(
            of(true, method()),
            of(false, method().annotations(contains(B.class))),
            of(false, method().modifiers(PUBLIC)),
            of(false, method().type(boolean.class)),
            of(false, method().parameters(match(exactly(1, variable().modifiers(PRIVATE))))),
            of(false, method().exceptions(match(exactly(1, is(int.class)))))
        );
    }
    
    @Test
    void test_other() {
        assertFalse(method.test(types, mock(VariableElement.class)));
    }
    
    @Test
    void describe() {
        var expected = "[@A]" + System.lineSeparator()
                     + "private [int] method(public int) throws Exception";
        Element upcast = element;
        assertEquals(expected, method.describe(types, upcast));
    }
    
    @Test
    void describe_element() {
        when(variable.getKind()).thenReturn(ElementKind.RESOURCE_VARIABLE);
        
        assertEquals("resource variable", method.describe(types, variable));
    }
    
    @Test
    void condition() {
        assertEquals("[type] method(...)", method.condition());
    }
    
    @Test
    void condition_custom() {
        assertEquals("some condition", method().condition("some condition").get().condition());
    }
    
}

@interface A {}
@interface B {}
