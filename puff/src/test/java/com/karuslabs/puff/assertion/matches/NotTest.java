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
import javax.lang.model.type.*;

import org.junit.jupiter.api.*;

import static com.karuslabs.puff.assertion.Assertions.*;
import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotTest {

    Match<Set<Modifier>> match = not(contains(PUBLIC, STATIC));
    TypeMirrors types = mock(TypeMirrors.class);
    Set<Modifier> modifiers = Set.of(PROTECTED, FINAL);
    Element element = when(mock(Element.class).getModifiers()).thenReturn(modifiers).getMock();
    
    
    @Test
    void timeable_test() {
        TypeMirror type = when(mock(TypeMirror.class).getKind()).thenReturn(TypeKind.INT).getMock();
        assertFalse(not(() -> is(int.class)).test(types, type));
    }
    
    
    @Test
    void test_element() {
        assertTrue(match.test(types, element));
    }
    
    @Test
    void test_value() {
        assertTrue(match.test(types, modifiers));
    }
    
    
    @Test
    void describe_element() {
        assertEquals("protected final", match.describe(element));
    }
    
    @Test
    void describe_value() {
        assertEquals("protected final", match.describe(modifiers));
    }
    
    
    @Test
    void condition() {
        assertEquals("not public static", match.condition());
    }
    
    @Test
    void conditions() {
        assertEquals("not public static", match.conditions());
    }
    
}
