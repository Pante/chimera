/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.commons.util;

import com.karuslabs.commons.util.Point.Axis;

import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;


@ExtendWith(MockitoExtension.class)
class PointTest {
    
    static final Point POINT = new Point(null, 1, 2, 3, 90, 180);
    
    Point point = new Point();
    Vector vector = new Vector();
    
    Point source = new Point(1, 2, 3);
    Point origin = new Point(null, 1, 2, 3, 90, 180);
    
    
    @Test
    void copy_relative_location() {
        source.relative(Axis.X, true).relative(Axis.Y, true).relative(Axis.Z, true).rotation(true).copy(origin, point);
        assertEquals(new Point(2, -4, -6), point);
    }
    
    
    @Test
    void copy_absolute_location() {
        source.copy(origin, point);
        assertEquals(new Point(1, 2, 3), point);
    }
    
    
    @Test
    void copy_relative_vector() {
        source.relative(Axis.X, true).relative(Axis.Y, true).relative(Axis.Z, true).rotation(true).copy(origin, vector);
        assertEquals(new Vector(2, -4, -6), vector);
    }
    
    
    @Test
    void copy_absolute_vector() {
        source.copy(origin, vector);
        assertEquals(new Vector(1, 2, 3), vector);
    }
    
    
    @Test
    void align() {
        var position = new Point(1, 2, 3).relative(Axis.X, true).relative(Axis.Y, true).relative(Axis.Z, true).rotation(true);
        var origin = new Point(null, 1, 2, 3, 90, 180);
        
        position.align(origin);
        
        assertEquals(new Point(2, -4, -6).relative(Axis.X, true).relative(Axis.Y, true).relative(Axis.Z, true).rotation(true), position);
    }
    
    
    @Test
    void set() {
        assertEquals(4, point.set(Axis.X, 4).getX(), 0.00000001);
        assertEquals(5, point.set(Axis.Y, 5).getY(), 0.00000001);
        assertEquals(6, point.set(Axis.Z, 6).getZ(), 0.00000001);
    }
    
    
    @Test
    void relative() {
        assertTrue(point.relative(Axis.X, true).relative(Axis.X));
    }
    
    
    @Test
    void rotation() {
        assertTrue(point.rotation(true).rotation());
    }
    
    
    @ParameterizedTest
    @MethodSource("equality_parameters")
    void equals(Object other, boolean expected) {
       assertEquals(expected, POINT.equals(other));
    }
    
    @ParameterizedTest
    @MethodSource("equality_parameters")
    void hashCode(Object other, boolean expected) {
        assertEquals(expected, POINT.hashCode() == other.hashCode());
    }
    
    
    static Stream<Arguments> equality_parameters() {
        return Stream.of(of(POINT, true),
            of(new Point(null, 1, 2, 3, 90, 180), true),
            of(new Location(null, 0, 0, 0), false),
            of(new Point(null, 1, 2, 3, 90, 180).relative(Axis.Y, true), false)
        );
    }
    
    
    @Test
    void to_string() {
        assertEquals(
            "Point[rotation: true, world: null, x: [1.0, absolute], y: [2.0, relative], z: [3.0, absolute], yaw: 90.0, pitch: 180.0]",
            new Point(null, 1, 2, 3, 90, 180).rotation(true).relative(Axis.Y, true).toString()
        );
    }

} 
