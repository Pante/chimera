/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karuslabs.commons.animation.particles;

import com.karuslabs.commons.animation.particles.Particles.Builder;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class ParticlesTest {
    
    private Particles particles;
    private Player player;
    private Location location;
    
    
    public ParticlesTest() {
        particles = spy(new StubParticles(null, 0));
        location = mock(Location.class);
        player = when(mock(Player.class).getLocation()).thenReturn(location).getMock();
    }
    
    
    @Test
    public void render() {
        particles.render(player);
        verify(particles).render(player, location);
    }
    
    
    @Test
    public void builder() {
        Builder builder = new StubBuilder(particles);
        Particles particles = builder.type(Particle.CRIT).amount(100).build();
        
        assertEquals(Particle.CRIT, particles.getType());
        assertEquals(100, particles.getAmount());
    }
    
    
    private static class StubParticles extends Particles {

        public StubParticles(Particle type, int amount) {
            super(type, amount);
        }

        @Override
        public void render(Player player, Location location) {
            
        }

        @Override
        public void render(Location location) {
            
        }
        
    }
    
    private static class StubBuilder extends Builder<StubBuilder, Particles> {

        public StubBuilder(Particles particles) {
            super(particles);
        }

        @Override
        protected StubBuilder getThis() {
            return this;
        }
        
    }
    
}
