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
package com.karuslabs.commons.animation.particles;

import org.bukkit.*;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class ColouredParticlesTest {
    
    private ColouredParticles particles = spy(Particles.coloured().particle(Particle.BARRIER).colour(Color.YELLOW)).build();
    private World world = mock(World.class);
    private Location location = when(mock(Location.class).getWorld()).thenReturn(world).getMock();
    private Player player = when(mock(Player.class).getLocation()).thenReturn(location).getMock();
    
    
    @Test
    public void render_Player() {
        particles.render(player, location);
        
        verify(player).spawnParticle(Particle.BARRIER, location, 0, 1, 1, 0, 1);
    }
    
    
    @Test
    public void render() {
        particles.render(location);
        
        verify(world).spawnParticle(Particle.BARRIER, location, 0, 1, 1, 0, 1);
    }
    
    
    @Test
    public void getColor() {
        assertEquals(Color.YELLOW, particles.getColour());
    }
    
}
