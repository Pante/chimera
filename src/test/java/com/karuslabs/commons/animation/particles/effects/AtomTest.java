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

import com.karuslabs.commons.animation.particles.ColouredParticles;
import com.karuslabs.commons.world.*;

import org.bukkit.Location;

import org.junit.jupiter.api.Test;

import static java.lang.Math.PI;
import static org.bukkit.Color.WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class AtomTest {
    
    private static final double ROUNDING_ERROR = 0.000000000000001;
    
    private ColouredParticles nucleus = new ColouredParticles(null, 1, WHITE);
    private ColouredParticles orbital = new ColouredParticles(null, 1, WHITE);
    private Atom atom = new Atom(nucleus, orbital, 1, 1, 3, 0.2F, 1, 0, PI / 80);
    private StaticLocation origin = new StaticLocation(new Location(null, 1, 1, 1), null, false);
    private StubContext<BoundLocation, BoundLocation> context = spy(new StubContext<>(origin, null, 0, 0));
    
    
    @Test
    public void render() {
        Atom atom = spy(new Atom(nucleus, orbital));
        doNothing().when(atom).renderNucleus(context, origin.getLocation());
        doNothing().when(atom).renderOrbitals(context, origin.getLocation());
        
        atom.render(context);
        
        verify(atom).renderNucleus(context, origin.getLocation());
        verify(atom).renderOrbitals(context, origin.getLocation());
    }
    
    
    @Test
    public void renderNucleus() {
        atom.renderNucleus(context, origin.getLocation());
        
        verify(context).render(nucleus, origin.getLocation());
    }
    
    
    @Test
    public void renderOrbitals() {
        atom.renderOrbitals(context, origin.getLocation());
        
        System.out.println(context.location);
        
        verify(context).render(orbital, origin.getLocation());
        assertEquals(4, context.location.getX(), ROUNDING_ERROR);
        assertEquals(1, context.location.getY(), ROUNDING_ERROR);
        assertEquals(1, context.location.getZ(), ROUNDING_ERROR);
    }
    
}
