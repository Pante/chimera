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
package com.karuslabs.puff;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

class TextsTest {
    
    @ParameterizedTest
    @MethodSource("join_parameters")
    void and_list(String expected, String[] values) {
        assertEquals(expected.replace("|", "and"), Texts.and(List.of(values), Texts.STRING));
    }
    
    @ParameterizedTest
    @MethodSource("join_parameters")
    void and_array(String expected, String[] values) {
        assertEquals(expected.replace("|", "and"), Texts.and(values, Texts.STRING));
    }
    
    @ParameterizedTest
    @MethodSource("join_parameters")
    void or_list(String expected, String[] values) {
        assertEquals(expected.replace("|", "or"), Texts.or(List.of(values), Texts.STRING));
    }
    
    @ParameterizedTest
    @MethodSource("join_parameters")
    void or_array(String expected, String[] values) {
        assertEquals(expected.replace("|", "or"), Texts.or(values, Texts.STRING));
    }
    
    
    static Stream<Arguments> join_parameters() {
        return Stream.of(
            of("A", new String[] {"A"}),
            of("A | B", new String[] {"A", "B"}),
            of("A, B | C", new String[] {"A", "B", "C"})
        );
    }
    
    
    @ParameterizedTest
    @CsvSource({"abc, a, c", "a, a, ''", "c, '', c"})
    void join(String expected, String first, String second) {
        assertEquals(expected, Texts.join(first, "b", second));
    }
    
    
    @Test
    void format_reason() {
        assertEquals("\"something\" why", Texts.format("something", "why"));
    }
    
    @Test
    void format_reason_resolution() {
        assertEquals("\"something\" why, how", Texts.format("something", "why", "how"));
    }
    
    
    @Test
    void method() {
        var actual = Texts.method("actual method", "expected method");
        var expected = "Method is: " + System.lineSeparator()
                     + "    actual method" + System.lineSeparator()
                     + System.lineSeparator()
                     + "should be: " + System.lineSeparator()
                     + "    expected method";
        
        assertEquals(expected, actual);
    }
    
    
    @Test
    void indent() {
        var actual = Texts.indent("First line" + System.lineSeparator() + "Second line");
        var expected = "    First line" + System.lineSeparator()
                     + "    Second line";
        assertEquals(expected, actual);
    }
    
    
    @Test
    void quote() {
        assertEquals("\"something\"", Texts.quote("something"));
    }

} 
