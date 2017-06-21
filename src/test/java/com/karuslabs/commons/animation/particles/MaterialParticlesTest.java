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

import junitparams.*;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.bukkit.Particle.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class MaterialParticlesTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private MaterialParticles particles;
    private Player player;
    private Location location;
    private World world;
    private MaterialData data;
    
    
    public MaterialParticlesTest() {
        data = mock(MaterialData.class);
        particles = new MaterialParticles(BLOCK_CRACK, 1, 2, 3, 4, 5, data);
        world = mock(World.class);
        location = when(mock(Location.class).getWorld()).thenReturn(world).getMock();
        player = when(mock(Player.class).getLocation()).thenReturn(location).getMock();
    }
    
    
    @Test
    @Parameters
    public void validate(Particle particle) {
        assertEquals(particle, MaterialParticles.validate(particle));
    }
    
    protected Object[] parametersForValidate() {
        return new Object[] {
            BLOCK_CRACK, BLOCK_DUST, FALLING_DUST
        };
    }
    
    
    @Test
    public void validate_ThrowsException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid Particle: " + NOTE);
        
        MaterialParticles.validate(NOTE);
    }
        
    
    @Test
    public void materialParticles_ThrowsException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid Particle: " + null);
        
        new MaterialParticles(null, 0, null);
    }
    
    
    @Test
    public void render_Player_Location() {
        particles.render(player, location);
        
        verify(player).spawnParticle(BLOCK_CRACK, location, 1, 2.0, 3.0, 4.0, 5.0, data);
    }
    
    
    @Test
    public void render_Location() {
        particles.render(location);
        
        verify(world).spawnParticle(BLOCK_CRACK, location, 1, 2.0, 3.0, 4.0, 5.0, data);
    }
    
    
    @Test
    public void newMaterialParticles() {
        MaterialParticles particles = MaterialParticles.newMaterialParticles().type(FALLING_DUST).data(data).build();
        
        assertEquals(data, particles.getData());
        assertEquals(FALLING_DUST, particles.getType());
    }
    
    
    @Test
    public void newMaterialParticles_ThrowsException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid Particle: " + BARRIER);
        
        ColouredParticles.newColouredParticles().type(BARRIER);
    }
    
}
