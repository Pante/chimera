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

import static org.mockito.Mockito.*;


class GridTest extends EffectBase {
    
    Grid grid = spy(new Grid(PARTICLES).get());
    
    
    @Test
    void render() {
        doNothing().when(grid).renderRows(context, location);
        doNothing().when(grid).renderColumns(context, location);
        
        grid.render(context);
        
        verify(grid).renderRows(context, location);
        verify(grid).renderColumns(context, location);
    }
    
    
    @Test
    void renderRows() {
        doNothing().when(grid).render(context, location, context.vector);
        
        grid.renderRows(context, location);
        
        verify(grid, times(7)).render(context, location, context.vector);
        assertVector(from(0, 6, 0), vector);
    }
    
    
    @Test
    void renderColumns() {
        doNothing().when(grid).render(context, location, context.vector);
        
        grid.renderColumns(context, location);
        
        verify(grid, times(18)).render(context, location, context.vector);
        assertVector(from(0, 5.666666507720947, 0), vector);
    }
    
    
    @Test
    void render_Location() {
        grid.render(context, location, vector);
        
        verify(context).render(PARTICLES, location, vector);
        assertVector(from(1, 1, 1), location);
    }
    
}
