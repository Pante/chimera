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
package com.karuslabs.commons.effect.effects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.mockito.Mockito.*;


class CubeTest extends EffectBase {

    Cube cube = spy(new Cube(PARTICLES));
    
    
    @ParameterizedTest
    @CsvSource({"true, 1, 0", "false, 0, 1"})
    void render(boolean solid, int walls, int outlines) {
        doNothing().when(cube).renderWalls(context, origin, offset, 0, 0, 0);
        doNothing().when(cube).renderOutline(context, origin, offset, 0, 0, 0);
        
        cube.solid = solid;
        
        cube.render(context, origin, target, offset);
        
        verify(cube, times(walls)).renderWalls(context, origin, offset, 0, 0, 0);
        verify(cube, times(outlines)).renderOutline(context, origin, offset, 0, 0, 0);
    }
        
    
    @Test
    void renderWalls() {
        cube.perRow = 1;
        
        cube.renderWalls(context, origin, offset, 0, 0, 0);
        
        verify(context).render(PARTICLES, origin, offset);
        assertVector(from(-1.5, -1.5, -1.5), offset);
    }
    
    
    @Test
    void renderOutline() {
        doNothing().when(cube).renderPillarOutline(context, origin, 0, offset, 0, 0, 0);
        
        cube.renderOutline(context, origin, offset, 0, 0, 0);
        
        verify(context, times(11)).render(PARTICLES, origin, offset);
        verify(cube).renderPillarOutline(context, origin, 0, offset, 0, 0, 0);
        assertVector(from(-1.499999982112679, -1.5, 1.5000000178873205), context.offset);
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, 1.317134151772411, 1.905858842747876, -1.17595055072672983", "false, 1.5, -1.5, 1.5"})
    void renderPillarOutlines(boolean rotate, double x, double y, double z) {
        cube.rotate = rotate;
        cube.perRow = 1;
        
        cube.renderPillarOutline(context, origin, 0, offset, 1, 2, 3);
        
        verify(context).render(PARTICLES, origin, offset);
        assertVector(from(x, y, z), context.offset);
    }
    
}
