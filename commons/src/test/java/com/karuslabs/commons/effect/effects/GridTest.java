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

import static org.mockito.Mockito.*;


class GridTest extends EffectBase {
    
    Grid grid = spy(new Grid(PARTICLES));
    
    
    @Test
    void render() {
        doNothing().when(grid).renderRows(context, origin, offset);
        doNothing().when(grid).renderColumns(context, origin, offset);
        
        grid.render(context, origin, target, offset);
        
        verify(grid).renderRows(context, origin, offset);
        verify(grid).renderColumns(context, origin, offset);
    }
    
    
    @Test
    void renderRows() {
        doNothing().when(grid).renderParticles(context, origin, offset);
        
        grid.renderRows(context, origin, offset);
        
        verify(grid, times(7)).renderParticles(context, origin, offset);
        assertVector(from(0, 6, 0), offset);
    }
    
    
    @Test
    void renderColumns() {
        doNothing().when(grid).renderParticles(context, origin, offset);
        
        grid.renderColumns(context, origin, offset);
        
        verify(grid, times(18)).renderParticles(context, origin, offset);
        assertVector(from(0, 5.666666507720947, 0), offset);
    }
    
    
    @Test
    void renderParticles() {
        grid.renderParticles(context, origin, offset);
        
        verify(context).render(PARTICLES, origin, offset);
        assertVector(from(1, 1, 1), origin);
    }
    
}
