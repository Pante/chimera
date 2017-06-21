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

import org.bukkit.*;
import org.bukkit.entity.Player;

import org.junit.Test;

import static org.bukkit.Particle.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class AnimationParticlesTest {
    
    private AnimationParticles particles;
    private Player player;
    private Location location;
    private World world;
    
    
    public AnimationParticlesTest() {
        particles = new AnimationParticles(BARRIER, 1, 2, 3, 4, 5);
        world = mock(World.class);
        location = when(mock(Location.class).getWorld()).thenReturn(world).getMock();
        player = when(mock(Player.class).getLocation()).thenReturn(location).getMock();
    }
    
    
    @Test
    public void render_Player_Location() {
        particles.render(player, location);
        
        verify(player).spawnParticle(BARRIER, location, 1, 2.0, 3.0, 4.0, 5.0);
    }
    
    
    @Test
    public void render_Location() {
        particles.render(location);
        
        verify(world).spawnParticle(BARRIER, location, 1, 2.0, 3.0, 4.0, 5.0);
    }
    
    
    @Test
    public void newAnimationParticles() {
        AnimationParticles particles = AnimationParticles.newAnimationParticles().offsetX(2).offsetY(3).offsetZ(4).speed(5).build();
        assertEquals(2.0, particles.getOffsetX(), 0);
        assertEquals(3.0, particles.getOffsetY(), 0);
        assertEquals(4.0, particles.getOffsetZ(), 0);
        assertEquals(5.0, particles.getSpeed(), 0);
    }
    
}
