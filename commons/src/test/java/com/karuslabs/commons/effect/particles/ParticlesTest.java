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
package com.karuslabs.commons.effect.particles;

import com.karuslabs.commons.animation.Base;
import com.karuslabs.commons.effect.particles.Particles.Builder;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import org.junit.jupiter.api.Test;

import static java.util.Collections.singleton;
import static org.bukkit.Particle.PORTAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class ParticlesTest extends Base {
    
    static final Vector OFFSET = new Vector(2, 3, 4);
    
    Particles particles = new StubBuilder(spy(new Particles(null, 0) {
        @Override
        protected void render(World world, double x, double y, double z) {
            
        }

        @Override
        protected void render(Player player, double x, double y, double z) {
            
        }
        
    })).particle(PORTAL).amount(10).build();
    
    
    @Test
    void render_Location() {
        particles.render(location);
        
        verify(particles).render(world, 1, 2, 3);
    }
    
    
    @Test
    void render_Location_Offset() {
        particles.render(location, OFFSET);
        
        verify(particles).render(world, 3, 5, 7);
    }
    
    
    @Test
    void render_Player() {
        particles.render(player);
        
        verify(particles).render(player, location);
    }
    
    
    @Test
    void render_Players_Location() {
        particles.render(singleton(player), location);
        
        verify(particles).render(player, location);
    }
    
    
    @Test
    void render_Player_Location() {
        particles.render(player, location);
        
       verify(particles).render(player, 1, 2, 3);
    }
    
    
    @Test
    void render_Player_Offset() {
        particles.render(player, OFFSET);
        
        verify(particles).render(player, location, OFFSET);
    }
    
    
    @Test
    void render_Players_Location_Offset() {
        particles.render(singleton(player), location, OFFSET);
        
        verify(particles).render(player, 3, 5, 7);
    }
    
    
    @Test
    void render_Player_Location_Offset() {
        particles.render(player, location, OFFSET);
        
        verify(particles).render(player, 3, 5, 7);
    }
    
    
    @Test
    void getters() {
        assertEquals(PORTAL, particles.getParticle());
        assertEquals(10, particles.getAmount());
    }
    
    
    static class StubBuilder extends Builder<StubBuilder, Particles> {

        private StubBuilder(Particles particles) {
            super(particles);
        }

        @Override
        protected StubBuilder self() {
            return this;
        }
        
    }
            
}
