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
package com.karuslabs.puff.assertion.times;

import com.karuslabs.puff.assertion.matches.Variable;
import com.karuslabs.puff.type.TypeMirrors;

import java.util.Set;
import javax.lang.model.element.*;

import org.junit.jupiter.api.*;

import static com.karuslabs.puff.assertion.Assertions.*;
import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TimesTest {

    Variable variable = variable().get();
    Times<VariableElement> times = exactly(3, variable);
    TypeMirrors types = mock(TypeMirrors.class);
    VariableElement element = when(mock(VariableElement.class).getModifiers()).thenReturn(Set.of(PUBLIC)).getMock();
    
    @Test
    void add_element() {
        assertTrue(times.add(types, element));
        assertEquals(1, times.current);
    }
    
    @Test
    void add_element_fails() {
        assertFalse(times.add(types, mock(ExecutableElement.class)));
        assertEquals(0, times.current);
    }
    
    
    @Test
    void add_value() {
        assertTrue(times.add(types, mock(VariableElement.class)));
        assertEquals(1, times.current);
    }
    
    @Test
    void add_value_fails() {
        assertFalse(exactly(3, variable().modifiers(PRIVATE)).add(types, element));
        assertEquals(0, times.current);
    }
    
    
    @Test
    void reset() {
        times.current = 42;
        times.reset();
        
        assertEquals(0, times.current);
    }    
    
    
    @Test
    void describe_single() {
        times.current = 1;
        assertEquals("1 type", times.describe());
    }
    
    @Test
    void describe_plural() {
        times.current = 0;
        assertEquals("0 types", times.describe());
    }
    
}

class ExactlyTest {
    
    Times<VariableElement> times = no(variable());
    
    @Test
    void test() {
        assertTrue(times.test());
    }
    
    @Test
    void test_fails() {
        times.current = 3;
        assertFalse(times.test());
    }
    
    @Test
    void condition() {
        assertEquals("0 types", times.condition());
    }
    
}

class BetweenTest {
    
    Times<VariableElement> times = between(1, 3, variable());
    
    @Test
    void test_lower_bound() {
        times.current = 1;
        assertTrue(times.test());
    }
    
    @Test
    void test_lower_bound_fails() {
        assertFalse(times.test());
    }
    
    @Test
    void test_upper_bound() {
        times.current = 2;
        assertTrue(times.test());
    }
    
    @Test
    void test_upper_bound_fails() {
        times.current = 3;
        assertFalse(times.test());
    }
    
    @Test
    void condition() {
        assertEquals("1 to 3 types", times.condition());
    }
    
}

class MinTest {
    
    Times<VariableElement> times = min(1, variable());
    
    @Test
    void test() {
        times.current = 1;
        assertTrue(times.test());
    }
    
    @Test
    void test_fails() {
        times.current = 0;
        assertFalse(times.test());
    }
    
    @Test
    void condition() {
        assertEquals("1 or more types", times.condition());
    }
    
}

class MaxTest {
    
    Times<VariableElement> times = max(3, variable());
    
    @Test
    void test() {
        times.current = 3;
        assertTrue(times.test());
    }
    
    @Test
    void test_fails() {
        times.current = 4;
        assertFalse(times.test());
    }
    
}