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

import com.karuslabs.puff.assertion.matches.Match;
import com.karuslabs.puff.type.TypeMirrors;

import java.util.*;
import javax.lang.model.element.Modifier;

import org.junit.jupiter.api.*;

import static com.karuslabs.puff.assertion.Assertions.*;
import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchSequenceTest {

    Sequence<Set<Modifier>> sequence = match(() -> match(PUBLIC, STATIC), () -> match(PRIVATE), () -> match(PROTECTED));
    Sequence<Set<Modifier>> empty = match(new Match[0]);
    TypeMirrors types = mock(TypeMirrors.class);
    
    @Test
    void test() {
        assertTrue(sequence.test(types, List.of(Set.of(PUBLIC, STATIC), Set.of(PRIVATE), Set.of(PROTECTED))));
    }
    
    @Test
    void test_different_length() {
        assertFalse(sequence.test(types, List.of(Set.of(PUBLIC, STATIC))));
    }
    
    @Test
    void test_different_values() {
        assertFalse(sequence.test(types, List.of(Set.of(PUBLIC, STATIC), Set.of(PROTECTED), Set.of(PRIVATE))));
    }
    
    @Test
    void describe() {
        var actual = sequence.describe(types, List.of(Set.of(PRIVATE, FINAL), Set.of(PROTECTED)));
        assertEquals("private final, protected", actual);
    }
    
    @Test
    void describe_empty_values() {
        assertEquals("", sequence.describe(types, List.of()));
    }
    
    @Test
    void describe_no_matches_single_value() {
        assertEquals("1 value", empty.describe(types, List.of(Set.of(PUBLIC))));
    }
    
    @Test
    void describe_no_matches_multiple_values() {
        assertEquals("2 values", empty.describe(types, List.of(Set.of(PUBLIC), Set.of(PRIVATE))));
    }
    
    @Test
    void condition() {
        assertEquals("match [public static], [private], [protected]", sequence.condition());
    }
    
}

class EachSequenceTest {
    
    Sequence<Set<Modifier>> sequence = each(() -> match(PUBLIC));
    TypeMirrors types = mock(TypeMirrors.class);
    
    @Test
    void test() {
        assertTrue(sequence.test(types, List.of(Set.of(PUBLIC), Set.of(PUBLIC))));
    }
    
    @Test
    void test_fails() {
        assertFalse(sequence.test(types, List.of(Set.of(PUBLIC), Set.of(PRIVATE), Set.of(PUBLIC))));
    }
    
    @Test
    void describe() {
        assertEquals("private static final, native", sequence.describe(types, List.of(Set.of(PRIVATE, STATIC, FINAL), Set.of(NATIVE))));
    }
    
    @Test
    void condition() {
        assertEquals("each [public]",  sequence.condition());
    }
    
}
