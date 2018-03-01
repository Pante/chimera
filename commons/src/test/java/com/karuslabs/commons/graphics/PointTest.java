/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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
package com.karuslabs.commons.graphics;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;


class PointTest {
    
    static Point point = new Point(1, 2);
    static Point inverse = new Point(2, 1);
    static Point smaller = new Point();
    static Point larger = new Point(2, 3);
    
    
    @ParameterizedTest
    @MethodSource("compareTo_parameters")
    void compareTo(Point other, int expected) {
        assertEquals(expected, point.compareTo(other));
    }
    
    static Stream<Arguments> compareTo_parameters() {
        return Stream.of(
            of(inverse, 0),
            of(smaller, 1),
            of(larger, -1)
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("parameters")
    void equals(Object other, boolean expected) {
        assertEquals(expected, point.equals((Point) other));
        assertEquals(expected, point.equals(other));
    }
    
    @ParameterizedTest
    @MethodSource("parameters")
    void hashcode(Object other, boolean expected) {
        assertEquals(expected, point.hashCode() == other.hashCode());
    }
    
    static Stream<Arguments> parameters() {
        return Stream.of(
            of(point, true),
            of(new Point(1, 2), true),
            of(inverse, false),
            of(smaller, false),
            of(larger, false)
        );
    }
    
    
    @Test
    void point_toString() {
        assertEquals("(1, 2)", point.toString());
    }
    
}
