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

import com.karuslabs.commons.effect.effects.Fountain;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;


class FountainTest extends EffectBase {
    
    Fountain fountain = spy(new Fountain(SPAM).get());
    
    
    @Test
    void render() {
        doNothing().when(fountain).renderStrands(context, location);
        doNothing().when(fountain).renderSpout(context, location, RANDOM);
        
        fountain.render(context);
        
        verify(fountain).renderStrands(context, location);
        verify(fountain).renderSpout(context, location, RANDOM);
    }
    
    
    @Test
    void renderStrands() {
        fountain.renderStrands(context, location);
        
        verify(context, times(10)).render(SPAM, location);
        assertVector(from(1.02357022661029, 1.062827259650071, 1.02357022661029), context.location);
    }
    
    
    @Test
    void renderSpout() {
        fountain.renderSpout(context, location, RANDOM);
        
        verify(context).render(SPAM, location);
    }
    
}
