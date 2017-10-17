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

import static java.lang.Math.PI;
import static org.mockito.Mockito.*;


class StarTest extends EffectBase {
    
    Star star = spy(new Star(PARTICLES).get());
    
    
    @Test
    void render() {
        doNothing().when(star).render(any(), any(), any(), anyDouble());
        
        star.render(context);
        
        verify(star, times(6)).render(eq(context), eq(RANDOM), eq(location), anyDouble());
    }
    
    
    @Test
    void render_rotation() {
        when(random.nextFloat()).thenReturn(0.5F);
        
        star.render(context, random, location, PI / 3);
        
        verify(context, times(2)).render(PARTICLES, location, vector);
        assertVector(from(-0.9485571585149866,-0.12500000000000067, 0.7500000000000001), context.location);
    }
    
}
