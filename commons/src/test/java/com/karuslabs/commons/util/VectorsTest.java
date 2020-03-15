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

import org.bukkit.util.Vector;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class VectorsTest {
    
    Vector vector = new Vector(1, 2, 3);
    Point position = new Point(null, 3, 2, 1);
    Point pivot = new Point(null, 0, 0, 0, 90, -180); 
    
    
    @Test
    void rotate_vector_radians() {
        assertEquals(new Vector(1.6947429641668934, 2.186059878281388, 2.519719923716756), Vectors.rotate(vector, 3, 3, 3));
    }
    
    
    @Test
    void rotateAroundXAxis() {
        assertEquals(new Vector(1.0, -2.4033450173804924, -2.687737473681602), Vectors.rotateAroundXAxis(vector, 3));
    }
    
    
    @Test
    void rotateAroundYAxis() {
        assertEquals(new Vector(-0.5666324724208438, 2.0, -3.111097497861204), Vectors.rotateAroundYAxis(vector, 3));
    }
    
    
    @Test
    void rotateAroundZAxis() {
        assertEquals(new Vector(-1.27223251272018, -1.8388649851410237, 3.0), Vectors.rotateAroundZAxis(vector, 3));
    }
    
    
    @Test
    void rotate_vector_pivot() {
        assertEquals(new Vector(1, -2, -3), Vectors.rotate(vector, pivot));
    }
    
    
    @Test
    void rotate_vector_degrees() {
        assertEquals(new Vector(1, -2, -3), Vectors.rotate(vector, 90, -180));
    }
    
    
    @Test
    void roatate_location_pivot() {
        assertEquals(new Point(null, 3, -2, -1), Vectors.rotate(position, pivot));
    }
    
    
    @Test
    void roatate_location_degrees() {
        assertEquals(new Point(null, 3, -2, -1), Vectors.rotate(position, 90, -180));
    }
    
    
    @Test
    void rotate_reducer() {
        assertEquals(new Vector(1, -2, -3), Vectors.rotate((v, x, y, z) -> v.setX(x).setY(y).setZ(z), vector, 1, 2, 3, 90, 180));
    }
    
    
    @Test
    void angleToXAxis() {
        assertEquals(0.4636476090008061, Vectors.angleToXAxis(vector), 0.0001);
    }

} 
