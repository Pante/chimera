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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class LineTest extends EffectBase {
    
    Line line = spy(new Line(PARTICLES).get());
    
    
    @Test
    void render() {
        line.zigzag = true;
        line.step  = 1000;
        line.render(context);
        
        verify(context).render(PARTICLES, location);
        assertVector(from(1, 0.9, 1), context.location);
        assertEquals(true, line.direction);
        assertEquals(1, line.step);
    }
    
    
    @ParameterizedTest
    @CsvSource({"2, 1.8660254037844384, -0.7320508075688774, 0.5000000000000001", "0, 2, 2, 2"})
    void resolveLink(double length, double x, double y, double z) {
        line.length = length;
        
        line.resolveLink(context, location);
        
        assertVector(from(x, y, z), line.link);
    }
    
}
