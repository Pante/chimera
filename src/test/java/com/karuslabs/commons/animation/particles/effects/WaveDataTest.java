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
package com.karuslabs.commons.animation.particles.effects;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.bukkit.util.Vector;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;


class WaveDataTest {
    
    WaveData data = new WaveData(10, 1, new Vector(0.75, 0.5, 0), new Vector(-1.5, 0, 0));
    Set<Vector> waters = new HashSet<>();
    Set<Vector> clouds = new HashSet<>();
    Vector vector = new Vector();
    
    
    @ParameterizedTest
    @MethodSource("parameters")
    void process(Predicate<Integer> range, int watersSize, int cloudsSize) {
        data.process(waters, clouds, range, 1, 0, 0);
        
        assertEquals(watersSize, waters.size());
        assertEquals(cloudsSize, clouds.size());
    }
    
    static Stream<Arguments> parameters() {
        return Stream.of(of(wrap(i -> i == 0 || i == 9), 8, 2), of(wrap(i -> i == 0), 9, 1));
    }
    
    static Object wrap(Predicate<Integer> predicate) {
        return predicate;
    }
    
    
    @Test
    void processRows() {
        data.processRows(waters, vector, 3, 10, 10);
        
        assertEquals(3, waters.size());
    }
    
}
