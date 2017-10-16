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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.mockito.Mockito.*;


class CubeTest extends Base {

    Cube cube = spy(new Cube(PARTICLES));
    
    
    @ParameterizedTest
    @CsvSource({"true, 1, 0", "false, 0, 1"})
    void render(boolean solid, int walls, int outlines) {
        doNothing().when(cube).renderWalls(context, location, vector, 0, 0, 0);
        doNothing().when(cube).renderOutline(context, location, 4, 2, vector, 0, 0, 0);
        
        cube.solid = solid;
        
        cube.render(context);
        
        verify(cube, times(walls)).renderWalls(context, location, vector, 0, 0, 0);
        verify(cube, times(outlines)).renderOutline(context, location, 4, 2, vector, 0, 0, 0);
    }
        
    
    @Test
    void renderWalls() {
        cube.perRow = 1;
        
        cube.renderWalls(context, location, vector, 0, 0, 0);
        
        verify(context).render(PARTICLES, location);
        assertVector(from(-0.5, -0.5, -0.5), context.location);
    }
    
    
    @Test
    void renderOutline() {
        doNothing().when(cube).renderPillarOutlines(context, location, 0, vector, 0, 0, 0);
        
        cube.renderOutline(context, location, 1, 1, vector, 0, 0, 0);
        
        verify(context).render(PARTICLES, location);
        verify(cube).renderPillarOutlines(context, location, 0, vector, 0, 0, 0);
        assertVector(from(2.5, 2.5, -0.5), context.location);
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, 2.317134151772411, 2.905858842747876, -0.17595055072672983", "false, 2.5, -0.5, 2.5"})
    void renderPillarOutlines(boolean rotate, double x, double y, double z) {
        cube.rotate = rotate;
        cube.perRow = 1;
        
        cube.renderPillarOutlines(context, location, 0, vector, 1, 2, 3);
        
        verify(context).render(PARTICLES, location);
        assertVector(from(x, y, z), context.location);
    }
    
}
