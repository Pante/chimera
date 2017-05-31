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
package com.karuslabs.commons.animation;

import com.karuslabs.commons.configuration.Configurations;

import org.bukkit.*;
import org.bukkit.entity.Player;

import org.junit.Test;

import static com.karuslabs.commons.Yaml.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class HelixTest {
    
    private Helix helix;
    private Location location;
    private Player player;
    
    
    public HelixTest() {
        helix = spy(new Helix(ANIMATION.getConfigurationSection("helix")));
        location = new Location(mock(World.class), 0, 0, 0);
        player = when(mock(Player.class).getLocation()).thenReturn(location).getMock();
    }
    
    
    @Test
    public void helix() {
        assertEquals(6.9, helix.getRadius(), 0);
        assertEquals(Particle.FLAME, helix.getParticles());
        assertEquals(10, helix.getAmount());
        assertEquals(5.5, helix.getHeight(), 0);
        assertEquals(4.5, helix.getPeriod(), 0);
    }
    
    
    @Test
    public void helix_Default() {
        Helix helix = new Helix(Configurations.BLANK);
        
        assertEquals(2, helix.getRadius(), 0);
        assertEquals(Particle.ENCHANTMENT_TABLE, helix.getParticles());
        assertEquals(50, helix.getAmount());
        assertEquals(12, helix.getHeight(), 0);
        assertEquals(4, helix.getPeriod(), 0);
    }
    
    
    @Test
    public void render_Player() {
        doNothing().when(helix).render(player, location);
        
        helix.render(player);
        
        verify(helix).render(player, location);
    }
    
    
    @Test
    public void render_Player_Location() {
        helix.render(player, location);
        
        verify(player).spawnParticle(helix.getParticles(), 6.9, 0, 0, 10);
    }
    
    
    @Test
    public void render_Location() {
        helix.render(location);
        
        verify(location.getWorld()).spawnParticle(helix.getParticles(), 6.9, 0, 0, 10);
    }
    
}
