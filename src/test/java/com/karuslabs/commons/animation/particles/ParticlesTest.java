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

import com.karuslabs.commons.animation.particles.Particles.Builder;

import org.bukkit.*;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.Test;

import static java.util.Collections.singletonList;
import static org.bukkit.Particle.PORTAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class ParticlesTest extends Base {
    
    Particles particles = new StubBuilder(spy(new StubParticles(null, 0))).particle(PORTAL).amount(10).build();
    
    
    @Test
    void render_Player() {
        particles.render(player);
        
        verify(particles).render(player, location);
    }
    
    
    @Test
    void render_Players() {
        particles.render(singletonList(player), location);
        
        verify(particles).render(player, location);
    }
    
    
    @Test
    void get() {
        assertEquals(PORTAL, particles.getParticle());
        assertEquals(10, particles.getAmount());
    }
    
    
    static class StubParticles extends Particles {

        private StubParticles(Particle particle, int amount) {
            super(particle, amount);
        }

        @Override
        public void render(Player player, Location location) {
            
        }

        @Override
        public void render(Location location) {
            
        }
        
    }
    
    static class StubBuilder extends Builder<StubBuilder, StubParticles> {

        private StubBuilder(StubParticles particles) {
            super(particles);
        }

        @Override
        protected StubBuilder getThis() {
            return this;
        }
        
    }
            
}
