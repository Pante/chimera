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
package com.karuslabs.commons.world;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import org.junit.jupiter.api.*;

import static java.lang.Math.PI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.*;


@TestInstance(PER_CLASS)
class VectorsTest extends VectorBase {
    
    @Test
    void copy_Vector() {
        Vector source = new Vector(0.1, 0.2, 0.3);
        Vector destination = new Vector();
        
        Vectors.copy(source, destination);
        assertEquals(source, destination);
    }
    
    
    @Test
    void copy_Location() {
        Location source = new Location(null, 0.1, 0.2, 0.3);
        Vector destination = new Vector();
        
        Vectors.copy(source, destination);
        assertEquals(source.toVector(), destination);
    }
    
    
    @Test
    void random() {
        Vector vector = mock(Vector.class);
        
        Vectors.random(vector);
        
        verify(vector).setX(anyDouble());
        verify(vector).setY(anyDouble());
        verify(vector).setZ(anyDouble());
        verify(vector).normalize();
    }
    
    
    @Test
    void randomAngle() {
        assertEquals(0, Vectors.randomAngle(), 2 * PI);
    }
    
    
    @Test
    void randomCircle() {
        Vector vector = mock(Vector.class);
        
        Vectors.randomCircle(vector);
        
        verify(vector).setX(anyDouble());
        verify(vector).setY(0);
        verify(vector).setZ(anyDouble());
    }
    
        
    @Test
    void rotate_radians() {
        Vector vector = Vectors.rotate(new Vector(10, 10, 10), 1, 2, 3);
        
        assertVector(from(-7.893858690333731, 4.167373192795026, -14.843180105658494), vector);
    }
    
    
    @Test
    void rotate_Location() {
        Vector vector = Vectors.rotate(new Vector(10, 10, 10), new Location(null, 0, 0, 0, 10, 20));

        assertVector(from(-12.073748387926274, 5.976724774602398, 10.885924895648582), vector);
    }
    
            
    @Test
    void rotateAroundXAxis() {
        Vector vector = Vectors.rotateAroundXAxis(new Vector(10, 10, 10), 10);
        
        assertVector(from(10.0, -2.9505041818708264, -13.830926399658221), vector);
    }
    
    
    @Test
    void rotateAroundYAxis() {
        Vector vector = Vectors.rotateAroundYAxis(new Vector(10, 10, 10), 10);
        
        assertVector(from(-13.830926399658221, 10.0, -2.9505041818708264), vector);
    }
    
    
    @Test
    void rotateAroudZAxis() {
        Vector vector = Vectors.rotateAroundZAxis(new Vector(10, 10, 10), 10);
        
        assertVector(from(-2.9505041818708264, -13.830926399658221, 10.0), vector);
    }
    
    
    @Test
    void angleToXAxis() {
        assertEquals(0.4636476090008061, Vectors.angleToXAxis(new Vector(1, 2, 3)), PRECISION);
    }

}
