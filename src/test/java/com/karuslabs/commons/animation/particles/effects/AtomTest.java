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


public class AtomTest extends Effect {

    private Atom atom = new Atom(PARTICLES, COLOURED, 1, 1, 3, 0.2F, 1, 0, PI / 80).get();
    
    
    @Test
    public void render() {
        Atom atom = spy(new Atom(PARTICLES, COLOURED));

        doNothing().when(atom).renderNucleus(context, location, vector);
        doNothing().when(atom).renderOrbitals(context, location, vector, 0);
        
        atom.render(context);
        
        verify(atom).renderNucleus(context, location, vector);
        verify(atom).renderOrbitals(context, location, vector, 0);
    }
    
    
    @Test
    public void renderNucleus() {
        atom.renderNucleus(context, location, vector);
        
        verify(context).render(PARTICLES, location);
    }
    
    
//    @Test
//    public void renderOrbitals() {
//        atom.renderOrbitals(context, location, vector, 0);
//        
//        verify(context).render(COLOURED, location);
//        assertVector(from(4, 1, 1), location);
//    }
    
}
