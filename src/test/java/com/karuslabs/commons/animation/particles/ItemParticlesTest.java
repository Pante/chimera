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
import org.bukkit.inventory.ItemStack;

import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.bukkit.Particle.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class ItemParticlesTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private ItemParticles particles;
    private Player player;
    private Location location;
    private World world;
    private ItemStack item;
    
    
    public ItemParticlesTest() {
        item = mock(ItemStack.class);
        particles = new ItemParticles(1, item);
        world = mock(World.class);
        location = when(mock(Location.class).getWorld()).thenReturn(world).getMock();
        player = when(mock(Player.class).getLocation()).thenReturn(location).getMock();
    }
    
    
    @Test
    public void render_Player_Location() {
        particles.render(player, location);
        
        verify(player).spawnParticle(ITEM_CRACK, location, 1, 0, 0, 0, 1, item);
    }
    
    
    @Test
    public void render_Location() {
        particles.render(location);
        
        verify(world).spawnParticle(ITEM_CRACK, location, 1, 0, 0, 0, 1, item);
    }
    
    
    @Test
    public void newItemParticles() {
        ItemParticles particles = ItemParticles.newItemParticles().type(ITEM_CRACK).item(item).build();
        assertEquals(ITEM_CRACK, particles.getType());
        assertEquals(item, particles.getItem());
    }
    
    
    @Test
    public void newItemParticles_ThrowsException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid Particles: " + NOTE);
        
        ItemParticles.newItemParticles().type(NOTE);
    }
    
}
