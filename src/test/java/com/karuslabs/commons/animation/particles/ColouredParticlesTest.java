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

import com.karuslabs.commons.effect.particles.ColouredParticles;
import com.karuslabs.commons.animation.Base;

import org.junit.jupiter.api.Test;

import static org.bukkit.Color.YELLOW;
import static org.bukkit.Particle.BARRIER;

import static com.karuslabs.commons.effect.particles.ColouredParticles.builder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class ColouredParticlesTest extends Base {
    
    ColouredParticles particles = builder().particle(BARRIER).colour(YELLOW).build();
        
    
    @Test
    void render_Location() {
        particles.render(location);
        
        verify(world).spawnParticle(BARRIER, location, 0, 1, 1, 0, 1);
    }
    
    
    @Test
    void render_Player_Location() {
        particles.render(player, location);
        
        verify(player).spawnParticle(BARRIER, location, 0, 1, 1, 0, 1);
    }
    
    
    @Test
    void getters() {
        assertEquals(YELLOW, particles.getColour());
    }
    
}
