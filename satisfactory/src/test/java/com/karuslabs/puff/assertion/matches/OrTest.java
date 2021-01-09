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

import java.util.Set;
import javax.lang.model.element.*;

import org.junit.jupiter.api.*;

import static com.karuslabs.puff.assertion.Assertions.*;
import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrTest {

    Match<Set<Modifier>> match = match(PUBLIC, FINAL).or(match(PRIVATE, STATIC));
    Set<Modifier> left = Set.of(PUBLIC, FINAL);
    Set<Modifier> right = Set.of(PRIVATE, STATIC);
    Element element = mock(Element.class);
    TypeMirrors types = mock(TypeMirrors.class);
    
    @Test
    void test_element_left_true() {
        when(element.getModifiers()).thenReturn(left);
        assertTrue(match.test(types, element));
    }
    
    @Test
    void test_element_right_true() {
        when(element.getModifiers()).thenReturn(right);
        assertTrue(match.test(types, element));
    }
    
    @Test
    void test_element_false() {
        when(element.getModifiers()).thenReturn(Set.of(PUBLIC, STATIC));
        assertFalse(match.test(types, element));
    }
    
    
    @Test
    void test_value_left_true() {
        assertTrue(match.test(types, left));
    }
    
    @Test
    void test_value_right_true() {
        assertTrue(match.test(types, right));
    }
    
    @Test
    void test_value_false() {
        assertFalse(match.test(types, Set.of(PUBLIC, STATIC)));
    }
    
    
    @Test
    void describe_element() {
        when(element.getModifiers()).thenReturn(right);
        assertEquals("private static", match.describe(element));
    }
    
    @Test
    void describe_value() {
        assertEquals("public final", match.describe(left));
    }
    
    
    @Test
    void condition() {
        assertEquals("public final, or private static", match.condition());
    }
    
    @Test
    void conditions() {
        assertEquals("public final, or private static", match.conditions());
    }
    
}
