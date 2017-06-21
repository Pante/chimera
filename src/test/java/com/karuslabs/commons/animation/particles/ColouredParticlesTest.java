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

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.bukkit.Color.*;
import static org.bukkit.Particle.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class ColouredParticlesTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private ColouredParticles particles;
    private Player player;
    private Location location;
    private World world;
    private double r, g, b;
    
    
    public ColouredParticlesTest() {
        particles = new ColouredParticles(SPELL, 1, ORANGE);
        world = mock(World.class);
        location = when(mock(Location.class).getWorld()).thenReturn(world).getMock();
        player = when(mock(Player.class).getLocation()).thenReturn(location).getMock();
        r = Color.ORANGE.getRed() / 255.0;
        g = Color.ORANGE.getGreen() / 255.0;
        b = Color.ORANGE.getBlue() / 255.0;
    }
    
    
    @Test
    @Parameters
    public void validate(Particle particle) {
        assertEquals(particle, ColouredParticles.validate(particle));
    }
    
    protected Object[] parametersForValidate() {
        return new Object[] {
            SPELL, SPELL_INSTANT, SPELL_MOB, SPELL_MOB_AMBIENT, SPELL_WITCH
        };
    }
    
    
    @Test
    public void validate_ThrowsException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid Particle: " + BLOCK_CRACK);
        
        ColouredParticles.validate(BLOCK_CRACK);
    }
   
    
    @Test
    public void colouredParticles_ThrowsException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid Particle: " + null);
        
        new ColouredParticles(null, 0, WHITE);
    }
    
    
    @Test
    public void render_Player_Location() {
        particles.render(player, location);
        
        verify(player).spawnParticle(SPELL, location, 1, r, g, b, 1.0);
    }
    
    
    @Test
    public void render_Location() {
        particles.render(location);
        
        verify(world).spawnParticle(SPELL, location, 1, r, g, b, 1.0);
    }
    
    
    @Test
    public void newColouredParticles() {
        ColouredParticles particles = ColouredParticles.newColouredParticles().type(SPELL_WITCH).colour(GREEN).build();
        
        assertEquals(SPELL_WITCH, particles.getType());
        assertEquals(GREEN, particles.getColour());
    }
    
    
    @Test
    public void newColouredParticles_ThrowsException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid Particle: " + BARRIER);
        
        ColouredParticles.newColouredParticles().type(BARRIER);
    }
    
}
