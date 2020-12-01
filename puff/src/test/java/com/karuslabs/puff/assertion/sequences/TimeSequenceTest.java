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
import com.karuslabs.puff.assertion.times.Times;

import java.util.*;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.junit.jupiter.api.*;

import static com.karuslabs.puff.assertion.Assertions.*;
import static javax.lang.model.element.Modifier.PRIVATE;
import javax.lang.model.type.TypeKind;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TimeSequenceTest {

    Sequence<TypeMirror> sequence = contains(exactly(1, is(int.class)), exactly(3, is(boolean.class)));
    TypeMirrors types = mock(TypeMirrors.class);
    TypeMirror type = when(mock(TypeMirror.class).accept(any(TypePrinter.class), any(StringBuilder.class))).then(call -> call.getArgument(1, StringBuilder.class).append("int")).getMock();
    
    @Test
    void describe() {
        when(type.getKind()).thenReturn(TypeKind.INT);
        assertEquals("[2 ints], [0 booleans]", sequence.describe(types, List.of(type, type)));
    }
    
    @Test
    void reset() {
        Times<TypeMirror> first = mock(Times.class);
        Times<TypeMirror> second = mock(Times.class);
        
        ((TimeSequence) contains(first, second)).reset();
        
        verify(first).reset();
        verify(second).reset();
    }
    
}

class ContainsSequenceTest {
    
    Sequence<VariableElement> sequence = contains(min(1, variable().modifiers(PRIVATE)), exactly(2, variable()));
    TypeMirrors types = mock(TypeMirrors.class);
    VariableElement element = when(mock(VariableElement.class).getModifiers()).thenReturn(Set.of(PRIVATE)).getMock();
    
    @Test
    void test() {
        VariableElement other = when(mock(VariableElement.class).getModifiers()).thenReturn(Set.of()).getMock();
        assertTrue(sequence.test(types, List.of(element, other)));
    }
    
    @Test
    void test_times_fails() {
        assertFalse(sequence.test(types, List.of(element, element, element)));
    }
    
    @Test
    void condition() {
        assertEquals("contains [1 or more private types], [2 types]", sequence.condition());
    }
    
}
