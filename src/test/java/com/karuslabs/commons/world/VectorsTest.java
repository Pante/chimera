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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.*;


@TestInstance(PER_CLASS)
public class VectorsTest {
    
    private static final double ROUNDING_ERROR = 0.0000000000000001;
    
    
    @Test
    public void random() {
        Vector vector = mock(Vector.class);
        
        Vectors.random(vector);
        
        verify(vector).setX(anyDouble());
        verify(vector).setY(anyDouble());
        verify(vector).setZ(anyDouble());
        verify(vector).normalize();
    }
    
    
    @Test
    public void randomCircle() {
        Vector vector = mock(Vector.class);
        
        Vectors.randomCircle(vector);
        
        verify(vector).setX(anyDouble());
        verify(vector).setY(0);
        verify(vector).setZ(anyDouble());
    }
    
        
    @Test
    public void rotateVector_radians() {
        Vector vector = Vectors.rotate(new Vector(10, 10, 10), 1, 2, 3);
        
        assertEquals(-7.893858690333731, vector.getX(), ROUNDING_ERROR);
        assertEquals(4.167373192795026, vector.getY(), ROUNDING_ERROR);
        assertEquals(-14.843180105658494, vector.getZ(), ROUNDING_ERROR);
    }
    
    
    @Test
    public void rotateVector_Location() {
        Vector vector = Vectors.rotate(new Vector(10, 10, 10), new Location(null, 0, 0, 0, 10, 20));
        
        assertEquals(-12.073748387926274, vector.getX(), ROUNDING_ERROR);
        assertEquals(5.976724774602398, vector.getY(), ROUNDING_ERROR);
        assertEquals(10.885924895648582, vector.getZ(), ROUNDING_ERROR);
    }
    
            
    @Test
    public void rotateAroundXAxis() {
        Vector vector = Vectors.rotateAroundXAxis(new Vector(10, 10, 10), 10);
        
        assertEquals(10.0, vector.getX(), ROUNDING_ERROR);
        assertEquals(-2.9505041818708264, vector.getY(), ROUNDING_ERROR);
        assertEquals(-13.830926399658221, vector.getZ(), ROUNDING_ERROR);
    }
    
    
    @Test
    public void rotateAroundYAxis() {
        Vector vector = Vectors.rotateAroundYAxis(new Vector(10, 10, 10), 10);
        
        assertEquals(-13.830926399658221, vector.getX(), ROUNDING_ERROR);
        assertEquals(10.0, vector.getY(), ROUNDING_ERROR);
        assertEquals(-2.9505041818708264, vector.getZ(), ROUNDING_ERROR);
    }
    
    
    @Test
    public void rotateAroudZAxis() {
        Vector vector = Vectors.rotateAroundZAxis(new Vector(10, 10, 10), 10);
        
        assertEquals(-2.9505041818708264, vector.getX(), ROUNDING_ERROR);
        assertEquals(-13.830926399658221, vector.getY(), ROUNDING_ERROR);
        assertEquals(10.0, vector.getZ(), ROUNDING_ERROR);
    }
    
    
    @Test
    public void angleToXAxis() {
        assertEquals(0.4636476090008061, Vectors.angleToXAxis(new Vector(1, 2, 3)), ROUNDING_ERROR);
    }

}
