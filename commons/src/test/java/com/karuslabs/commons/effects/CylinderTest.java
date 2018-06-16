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
package com.karuslabs.commons.effects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.mockito.Mockito.*;


class CylinderTest extends EffectBase {
    
    Cylinder cylinder = spy(new Cylinder(PARTICLES));
    
    
    @ParameterizedTest
    @CsvSource({"true", "false"})
    void render(boolean rotate) {
        doNothing().when(cylinder).renderSurface(any(), any(), any(), any());
        cylinder.rotate = rotate;
        
        cylinder.render(context, origin, target, offset);
        
        verify(cylinder).renderSurface(context, origin, offset, RANDOM);
    }
    
    
    @Test
    void renderSurface() {
        cylinder.renderSurface(context, origin, offset, RANDOM);
        
        verify(context).render(PARTICLES, origin, offset);
        verify(context).render(PARTICLES, origin);
    }
    
    
    @ParameterizedTest
    @CsvSource({"0.1, 2, -1.2, 2", "0.49, 0.49, -3, 0.49", "0.5, 0.5, -3, 0.5"})
    void calculate(float value, double x, double y, double z) {
        when(mockRandom.nextFloat()).thenReturn(value);
        when(mockRandom.nextDouble(-1, 1)).thenReturn(-0.8);
        
        offset.setX(1).setY(1).setZ(1);
        
        cylinder.calculate(offset, mockRandom, 2);

        assertVector(from(x, y, z), offset, LOW_PRECISION);
    }
    
}
