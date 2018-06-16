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

import static java.lang.Math.PI;
import static org.mockito.Mockito.*;


class AtomTest extends EffectBase {

    Atom atom = spy(new Atom(PARTICLES, COLOURED, 1, 1, 1, 3, 0.2F, 1, 0, PI / 80));
    
    
    @Test
    void render() {
        doNothing().when(atom).renderNucleus(context, origin, offset);
        doNothing().when(atom).renderOrbitals(context, origin, offset);
        
        atom.render(context, origin, target, offset);
        
        verify(atom).renderNucleus(context, origin, offset);
        verify(atom).renderOrbitals(context, origin, offset);
    }
    
    
    @Test
    void renderNucleus() {
        atom.renderNucleus(context, origin, offset);
        
        verify(context).render(PARTICLES, origin, offset);
    }
    
    
    @Test
    void renderOrbitals() {
        atom.renderOrbitals(context, origin, offset);
        
        verify(context).render(COLOURED, origin, offset);
        assertVector(from(3, 0, 0), context.offset);
    }
    
}
