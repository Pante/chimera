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

import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PositionTest {
    
    static final Position POSITION = new Position(null, 1, 2, 3, 90, 180);
    
    Position position = new Position();
    Vector vector = new Vector();
    
    Position source = new Position(1, 2, 3);
    Position origin = new Position(null, 1, 2, 3, 90, 180);
    
    
    @Test
    void apply_relative_location() {
        source.relative(Position.X, true).relative(Position.Y, true).relative(Position.Z, true).rotate(true).apply(position, origin);
        assertEquals(new Position(2, -4, -6), position);
    }
    
    
    @Test
    void apply_absolute_location() {
        source.apply(position, origin);
        assertEquals(new Position(1, 2, 3), position);
    }
    
    
    @Test
    void apply_relative_vector() {
        source.relative(Position.X, true).relative(Position.Y, true).relative(Position.Z, true).rotate(true).apply(vector, origin);
        assertEquals(new Vector(2, -4, -6), vector);
    }
    
    
    @Test
    void apply_absolute_vector() {
        source.apply(vector, origin);
        assertEquals(new Vector(1, 2, 3), vector);
    }
    
    
    @Test
    void relativize() {
        var position = new Position(1, 2, 3).relative(Position.X, true).relative(Position.Y, true).relative(Position.Z, true).rotate(true);
        var origin = new Position(null, 1, 2, 3, 90, 180);
        
        position.relativize(origin);
        
        assertEquals(new Position(2, -4, -6).relative(Position.X, true).relative(Position.Y, true).relative(Position.Z, true).rotate(true), position);
    }
    
    
    @Test
    void set() {
        assertEquals(4, position.set(Position.X, 4).getX(), 0.00000001);
        assertEquals(5, position.set(Position.Y, 5).getY(), 0.00000001);
        assertEquals(6, position.set(Position.Z, 6).getZ(), 0.00000001);
    }
    
    
    @Test
    void set_throws_exception() {
        assertEquals("Invalid axis: 3", assertThrows(IllegalArgumentException.class, () -> position.set(3, 0)).getMessage());
    }
    
    
    @Test
    void relative() {
        assertTrue(position.relative(Position.X, true).relative(Position.X));
    }
    
    
    @Test
    void rotate() {
        assertTrue(position.rotate(true).rotate());
    }
    
    
    @ParameterizedTest
    @MethodSource("equality_parameters")
    void equals(Object other, boolean expected) {
       assertEquals(expected, POSITION.equals(other));
    }
    
    @ParameterizedTest
    @MethodSource("equality_parameters")
    void hashCode(Object other, boolean expected) {
        assertEquals(expected, POSITION.hashCode() == other.hashCode());
    }
    
    
    static Stream<Arguments> equality_parameters() {
        return Stream.of(
            of(POSITION, true),
            of(new Position(null, 1, 2, 3, 90, 180), true),
            of(new Location(null, 0, 0, 0), false),
            of(new Position(null, 1, 2, 3, 90, 180).relative(Position.Y, true), false)
        );
    }
    
    
    @Test
    void to_string() {
        assertEquals(
            "rotate[true], null[world], 1.0[absolute], 2.0[relative], 3.0[absolute], 90.0[yaw], 180.0[pitch]",
            new Position(null, 1, 2, 3, 90, 180).rotate(true).relative(Position.Y, true).toString()
        );
    }

} 
