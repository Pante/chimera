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

import static org.mockito.Mockito.*;


class DragonTest extends EffectBase {
    
    Dragon dragon = spy(new Dragon(PARTICLES));
    
    
    @Test
    void render() {
        doNothing().when(dragon).renderArcs(context, origin, offset, 0);
        
        dragon.render(context, origin, target, offset);
        
        verify(dragon).populate(RANDOM);
        verify(dragon).renderArcs(context, origin, offset, 0);
        verify(dragon).renderArcs(context, origin, offset, 1);
    }
    
    
    @Test
    void renderArcs() {
        dragon.renderArcs(context, origin, offset, 1);
        
        verify(context, times(20)).render(PARTICLES, origin, offset);
        assertVector(from(0.05619543332824477, 0.10088888827899783, 0.06666667549644664), context.offset);
    }
    
}
