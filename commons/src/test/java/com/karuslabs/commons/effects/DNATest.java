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

import org.bukkit.util.Vector;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class DNATest extends EffectBase {

    DNA dna = spy(new DNA(PARTICLES, COLOURED, MATERIAL));
    
    
    @Test
    void render() {
        doNothing().when(dna).renderHelix(context, origin, offset, pitch, yaw, 0);
        doNothing().when(dna).renderBase(any(), any(), any(), any(), anyFloat(), anyFloat(), anyInt(), anyInt(), anyInt());
        
        context.steps = 76;
        
        dna.render(context, origin, target, offset);
        
        verify(dna).renderHelix(context, origin, offset, pitch, yaw, 0);
        verify(dna).renderBase(context, COLOURED, origin, offset, pitch, yaw, 0, 1, 15);
        verify(dna).renderBase(context, MATERIAL, origin, offset, pitch, yaw, 0, -15, -1);
    }
    
    
    @Test
    void renderHelix() {
        doNothing().when(dna).render(context, PARTICLES, origin, offset, pitch, yaw);
        
        dna.renderHelix(context, origin, offset, pitch, yaw, 1);
        
        verify(dna, times(2)).render(context, PARTICLES, origin, offset, pitch, yaw);
    }
    
    
    @Test
    void renderBase() {
         doNothing().when(dna).render(context, SINGLE, origin, offset, pitch, yaw);
         
         dna.renderBase(context, SINGLE, origin, offset, pitch, yaw, 1, 0, 1);
         
         verify(dna, times(2)).render(context, SINGLE, origin, offset, pitch, yaw);
    }
    
    
    @Test
    void render_Particles() {
        dna.render(context, COLOURED, origin, offset, pitch, yaw);
        
        verify(context).render(COLOURED, origin, offset);
        assertEquals(new Vector(), offset);
    }
    
}
