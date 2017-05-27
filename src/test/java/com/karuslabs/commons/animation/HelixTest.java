/*
 * Copyright (C) 2017 Karus Labs
 * All rights reserved.
 */
package com.karuslabs.commons.animation;

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
        Helix helix = new Helix(BLANK);
        
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
