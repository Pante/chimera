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

import com.karuslabs.commons.animation.particles.ColouredParticles;
import com.karuslabs.commons.animation.particles.Particles;
import com.karuslabs.commons.world.*;

import org.bukkit.Location;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.bukkit.Color.WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class CubeTest {
    
    private static final double ROUNDING_ERROR = 0.00001;
    
    private Particles particles = new ColouredParticles(null, 100, WHITE);
    private Cube cube = spy(new Cube(particles));
    private Location location = new Location(null, 1, 1, 1);
    private StaticLocation origin = new StaticLocation(location, null, false);
    private StubContext<BoundLocation, BoundLocation> context = spy(new StubContext<>(origin, null, 0, 0));
    
    
    @ParameterizedTest
    @CsvSource({"true, 1, 0", "false, 0, 1"})
    public void render(boolean solid, int walls, int outlines) {
        doNothing().when(cube).renderWalls(context, location);
        doNothing().when(cube).renderOutline(context, location, 4, 2);
        
        cube.solid = solid;
        
        cube.render(context);
        
        verify(cube, times(walls)).renderWalls(context, location);
        verify(cube, times(outlines)).renderOutline(context, location, 4, 2);
    }
    
    
    @Test
    public void renderOutline() {
        doNothing().when(cube).renderPillarOutlines(context, location, 0);
        
        cube.renderOutline(context, location, 1, 1);
        
        verify(context).render(particles, location);
        verify(cube).renderPillarOutlines(context, location, 0);
        assertEquals(2.5, context.location.getX(), ROUNDING_ERROR);
        assertEquals(2.5, context.location.getY(), ROUNDING_ERROR);
        assertEquals(-0.5, context.location.getZ(), ROUNDING_ERROR);
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, 2.317145845959713, 2.9058444260611456, -0.17596081742664094", "false, 2.500014999925, -0.5, 2.499984999925"})
    public void renderPillarOutlines(boolean rotate, double x, double y, double z) {
        cube.rotate = rotate;
        cube.perRow = 1;
        cube.rotation.setX(1).setY(2).setZ(3);
        
        cube.renderPillarOutlines(context, location, ROUNDING_ERROR);
        
        verify(context).render(particles, location);
        assertEquals(x, context.location.getX(), ROUNDING_ERROR);
        assertEquals(y, context.location.getY(), ROUNDING_ERROR);
        assertEquals(z, context.location.getZ(), ROUNDING_ERROR);
    }
    
    
    @Test
    public void renderWalls() {
        cube.perRow = 1;
        
        cube.renderWalls(context, location);
        
        verify(context).render(particles, location);
        assertEquals(-0.5, context.location.getX(), ROUNDING_ERROR);
        assertEquals(-0.5, context.location.getY(), ROUNDING_ERROR);
        assertEquals(-0.5, context.location.getZ(), ROUNDING_ERROR);
    }
    
}
