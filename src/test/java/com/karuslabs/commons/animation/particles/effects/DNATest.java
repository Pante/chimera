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

import org.bukkit.util.Vector;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class DNATest extends Effect {

    private DNA dna = spy(new DNA(PARTICLES, COLOURED, MATERIAL).get());
    
    
    @Test
    public void render() {
        doNothing().when(dna).renderHelix(context, vector, 0);
        doNothing().when(dna).renderBase(any(), any(), any(), anyInt(), anyInt(), anyInt());
        
        context.count = 76;
        
        dna.render(context);
        
        verify(dna).renderHelix(context, vector, 0);
        verify(dna).renderBase(context, COLOURED, vector, 0, 1, 15);
        verify(dna).renderBase(context, MATERIAL, vector, 0, -15, -1);
    }
    
    
    @Test
    public void renderHelix() {
        doNothing().when(dna).render(context, PARTICLES, vector);
        
        dna.renderHelix(context, vector, 1);
        
        verify(dna, times(2)).render(context, PARTICLES, vector);
    }
    
    
    @Test
    public void renderBase() {
         doNothing().when(dna).render(context, SINGLE, vector);
         
         dna.renderBase(context, SINGLE, vector, 1, 0, 1);
         
         verify(dna, times(2)).render(context, SINGLE, vector);
    }
    
    
    @Test
    public void render_Particles() {
        dna.render(context, COLOURED, vector);
        
        verify(context).render(COLOURED, location, vector);
        assertEquals(new Vector(), vector);
    }
    
}
