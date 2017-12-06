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


class TornadoTest extends EffectBase {
    
    Tornado tornado = spy(new Tornado(PARTICLES, COLOURED));
    
    
    @Test
    void render() {
        doNothing().when(tornado).renderCloud(context, origin, offset, RANDOM);
        doNothing().when(tornado).renderTornado(context, origin, offset);
        
        tornado.render(context, origin, target, offset);
        
        verify(tornado).renderCloud(context, origin, offset, RANDOM);
        verify(tornado).renderTornado(context, origin, offset);
    }
    
    
    @Test
    void renderCloud() {
        tornado.renderCloud(context, origin, offset, RANDOM);
        
        verify(context, times(3)).render(COLOURED, origin, offset);
    }
    
    
    @Test
    void renderTornado() {
        doNothing().when(tornado).renderTornadoPortion(any(), any(), any(), anyDouble(), anyDouble());
        
        tornado.renderTornado(context, origin, offset);
        
        verify(tornado, times(14)).renderTornadoPortion(any(), any(), any(), anyDouble(), anyDouble());
    }
    
    
    @Test
    void renderTornadoPortion() {
        tornado.renderTornadoPortion(context, origin, offset, 1, 2);
        
        verify(context).render(PARTICLES, origin, offset);
        assertVector(from(1, 2.2, 0), context.offset);
    }
    
}
