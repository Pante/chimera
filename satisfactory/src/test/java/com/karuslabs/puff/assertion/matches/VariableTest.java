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

@A
class VariableTest {

    TypeMirrors types = mock(TypeMirrors.class);
    VariableElement element = mock(VariableElement.class);
    Set<Modifier> modifiers = Set.of(PUBLIC, STATIC, FINAL);
    TypeMirror type = when(mock(TypeMirror.class).getKind()).thenReturn(TypeKind.INT).getMock();
    DeclaredType annotation = when(mock(DeclaredType.class).accept(any(), any())).then(invocation -> invocation.getArgument(1, StringBuilder.class).append("A")).getMock();
    AnnotationMirror mirror = when(mock(AnnotationMirror.class).getAnnotationType()).thenReturn(annotation).getMock();
    
    @BeforeEach
    void before() {
        when(type.accept(any(), any())).then(call -> call.getArgument(1, StringBuilder.class).append("int"));
        
        when(element.getModifiers()).thenReturn(modifiers);
        when(element.asType()).thenReturn(type);
        when(element.getAnnotationsByType(B.class)).thenReturn(new B[0]);
        when(element.getAnnotationsByType(A.class)).thenReturn(VariableTest.class.getAnnotationsByType(A.class));
        doReturn(List.of(mirror)).when(element).getAnnotationMirrors();
    }
    
    @ParameterizedTest
    @MethodSource("test_parameters")
    void test(boolean expected, Variable.Builder variable) {
        assertEquals(expected, variable.get().test(types, element));
    }
    
    static Stream<Arguments> test_parameters() {
        return Stream.of(
            of(true, variable()),
            of(true, variable().modifiers(contains(PUBLIC)).type(int.class).annotations(contains(A.class))),
            of(false, variable().modifiers(PUBLIC)),
            of(false, variable().type(boolean.class)),
            of(false, variable().annotations(contains(B.class)))
        );
    }
    
    @Test
    void test_other() {
        assertFalse(variable().get().test(types, mock(Element.class)));
    }
    
    
    @Test
    void describe() {
        Element untyped = element;
        assertEquals("public static final int annotated with @A", variable().get().describe(untyped));
    }
    
    @Test
    void describe_no_modifiers() {
        when(element.getModifiers()).thenReturn(Set.of());
        assertEquals("int annotated with @A", variable().get().describe(element));
    }
    
    @Test
    void describe_no_annotations() {
        when(element.getAnnotationMirrors()).thenReturn(List.of());
        assertEquals("public static final int", variable().get().describe(element));
    }
    
    @Test
    void describe_executable() {
        Element element = when(mock(Element.class).getKind()).thenReturn(ElementKind.RESOURCE_VARIABLE).getMock();
        assertEquals("resource variable", variable().get().describe(element));
    }
    
    
    @Test
    void condition() {
        var variable = variable().modifiers(PUBLIC).type(subtype(String.class)).annotations(contains(A.class)).get();
        assertEquals("public subtype of String annotated with @A", variable.condition());
        assertEquals("public subtypes of String annotated with @A", variable.conditions());
    }
    
    @Test
    void condition_no_modifiers() {
        var variable = variable().type(boolean.class).annotations(contains(A.class)).get();
        assertEquals("boolean annotated with @A", variable.condition());
        assertEquals("booleans annotated with @A", variable.conditions());
    }
    
    @Test
    void condition_no_type() {
        var variable = variable().modifiers(PUBLIC).annotations(contains(A.class)).get();
        assertEquals("public type annotated with @A", variable.condition());
        assertEquals("public types annotated with @A", variable.conditions());
    }
    
    @Test
    void condition_no_annotation() {
        var variable = variable().modifiers(PUBLIC).type(boolean.class).get();
        assertEquals("public boolean", variable.condition());
        assertEquals("public booleans", variable.conditions());
    }
    
    @Test
    void condition_empty() {
        var variable = variable().get();
        assertEquals("type", variable.condition());
        assertEquals("types", variable.conditions());
    }
    
    @Test
    void condition_condition() {
        var variable = variable().condition("some string").get();
        assertEquals("some string", variable.condition());
        assertEquals("some string", variable.conditions());
    }
    
}
