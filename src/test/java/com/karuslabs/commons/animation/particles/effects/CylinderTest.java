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

import com.karuslabs.commons.animation.particles.*;
import com.karuslabs.commons.world.*;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.bukkit.Color.WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class CylinderTest {
    
    private static final double ROUNDING_ERROR = 0.00001;
    
    private Particles particles = new ColouredParticles(null, 100, WHITE);
    private Cylinder cylinder = spy(new Cylinder(particles));
    private Location location = new Location(null, 1, 1, 1);
    private StaticLocation origin = new StaticLocation(location, null, false);
    private StubContext<BoundLocation, BoundLocation> context = spy(new StubContext<>(origin, null, 0, 0));
    
    
    @ParameterizedTest
    @CsvSource({"true, 0.015707963267948967, 0.018479956785822312, 0.02026833970057931", "false, 0.0, 0.0, 0.0"})
    public void render(boolean rotate, double x, double y, double z) {
        doNothing().when(cylinder).renderSurface(any(), any(), any(), anyDouble(), anyDouble(), anyDouble());
        cylinder.rotate = rotate;
        cylinder.step = 1;
        
        cylinder.render(context);
        
        verify(cylinder).renderSurface(context, location, ThreadLocalRandom.current(), x, y, z);
        verify(context).render(particles, location);
    }
    
    
    @Test
    public void renderSurface() {
        cylinder.renderSurface(context, location, ThreadLocalRandom.current(), 1, 2, 3);
        
        verify(context).render(particles, location);
    }
    
    
    @ParameterizedTest
    @CsvSource({"0.1, 2, -1.2, 2", "0.49, 0.49, 3.0, 0.49", "0.5, 0.5, -3, 0.5"})
    public void calculate(float value, double x, double y, double z) {
        ThreadLocalRandom random = when(mock(ThreadLocalRandom.class).nextFloat()).thenReturn(value).getMock();
        when(random.nextDouble(-1, 1)).thenReturn(-0.8);
        
        cylinder.vector.setX(1).setY(1).setZ(1);
        
        cylinder.calculate(random, 2);
        
        Vector vector = cylinder.vector;
        assertEquals(x, vector.getX(), ROUNDING_ERROR);
        assertEquals(y, vector.getY(), ROUNDING_ERROR);
        assertEquals(z, vector.getZ(), ROUNDING_ERROR);
    }
    
}
