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

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static com.karuslabs.puff.assertion.Assertions.*;
import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;

class ModifierMatchTest {
    
    Match<Set<Modifier>> match = spy(contains(PUBLIC));
    TypeMirrors types = mock(TypeMirrors.class);
    Set<Modifier> modifiers = Set.of(PUBLIC, STATIC, FINAL);
    Element element = when(mock(Element.class).getModifiers()).thenReturn(modifiers).getMock();
    
    @ParameterizedTest
    @MethodSource("sort_parameters")
    void sort_collection(List<Modifier> expected, List<Modifier> modifiers) {
        assertEquals(expected, ModifierMatch.sort(modifiers));
    }
    
    @ParameterizedTest
    @MethodSource("sort_parameters")
    void sort_array(List<Modifier> expected, List<Modifier> modifiers) {
        assertEquals(expected, List.of(ModifierMatch.sort(modifiers.toArray(Modifier[]::new))));
    }
    
    static Stream<Arguments> sort_parameters() {
        return Stream.of(
            of(List.of(PRIVATE, STATIC, FINAL), List.of(FINAL, PRIVATE, STATIC)),
            of(List.of(PUBLIC, FINAL, NATIVE), List.of(FINAL, NATIVE, PUBLIC)),
            of(List.of(PROTECTED, ABSTRACT, STRICTFP), List.of(ABSTRACT, STRICTFP, PROTECTED))
        );
    }
    
    
    @Test
    void test_element() {
        assertTrue(match.test(types, element));
        verify(match).test(types, modifiers);
    }
    
    @Test
    void describe_element() {
        assertEquals("public static final", match.describe(element));
        verify(match).describe(modifiers);
    }
    
    @Test
    void describe_modifiers() {
        assertEquals("public static final", match.describe(modifiers));
    }
    
}

class AnyModifierTest {
    
    Match<Set<Modifier>> match = ANY_MODIFIER;
    TypeMirrors types = mock(TypeMirrors.class);
    
    @Test
    void test_modifiers() {
        assertTrue(match.test(types, Set.of()));
    }
    
    @Test
    void condition() {
        assertEquals("", match.condition());
    }
    
}

class ContainsModifierTest {
    
    Match<Set<Modifier>> match = contains(PUBLIC, FINAL);
    TypeMirrors types = mock(TypeMirrors.class);
    
    @Test
    void test_modifiers() {
        assertTrue(match.test(types, Set.of(PUBLIC, STATIC, FINAL)));
    }
    
    @Test
    void test_modifiers_false() {
        assertFalse(match.test(types, Set.of(PUBLIC, STATIC)));
    }
    
    @Test
    void condition() {
        assertEquals("public final", match.condition());
    }
    
}

class MatchModifierTest {
    
    Match<Set<Modifier>> match = match(PUBLIC, FINAL);
    TypeMirrors types = mock(TypeMirrors.class);
    
    @Test
    void test_modifiers() {
        assertTrue(match.test(types, Set.of(PUBLIC, FINAL)));
    }
    
    @Test
    void test_modifiers_false() {
        assertFalse(match.test(types, Set.of(PUBLIC, STATIC, FINAL)));
    }
    
    @Test
    void condition() {
        assertEquals("public final", match.condition());
    }
    
}