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
import org.bukkit.material.MaterialData;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class MaterialParticlesTest {
    
    private MaterialData data = new MaterialData(Material.ACACIA_DOOR, (byte) 2);
    private MaterialParticles particles = spy(Particles.material().particle(Particle.CLOUD).offsetX(1).offsetY(2).offsetZ(3).speed(4).data(data).build());
    private World world = mock(World.class);
    private Location location = when(mock(Location.class).getWorld()).thenReturn(world).getMock();
    private Player player = when(mock(Player.class).getLocation()).thenReturn(location).getMock();
    
    
    @Test
    public void render_Player() {
        particles.render(player, location);
        
        verify(player).spawnParticle(Particle.CLOUD, location, 0, 1, 2, 3, 4, data);
    }
    
    
    @Test
    public void render() {
        particles.render(location);
        
        verify(world).spawnParticle(Particle.CLOUD, location, 0, 1, 2, 3, 4, data);
    }
    
    
    @Test
    public void get() {
        assertEquals(1, particles.getOffsetX());
        assertEquals(2, particles.getOffsetY());
        assertEquals(3, particles.getOffsetZ());
        assertEquals(4, particles.getSpeed());
        assertEquals(data, particles.getData());
    }
    
}