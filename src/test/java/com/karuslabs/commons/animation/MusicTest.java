/*
 * Copyright (C) 2017 Karus Labs
 * All rights reserved.
 */
package com.karuslabs.commons.animation;

import com.karuslabs.commons.configuration.Configurations;

import org.bukkit.*;
import org.bukkit.entity.Player;

import org.junit.Test;

import static com.karuslabs.commons.Yaml.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class MusicTest {
    
    private Music music;
    private Location location;
    private Player player;
    
    
    public MusicTest() {
        music = new Music(ANIMATION.getConfigurationSection("music"));
        location = new Location(mock(World.class), 0, 0, 0);
        player = when(mock(Player.class).getLocation()).thenReturn(location).getMock();
    }
    
    
    @Test
    public void music() {
        assertEquals(Sound.BLOCK_ANVIL_BREAK, music.getSound());
        assertEquals(SoundCategory.MASTER, music.getCategory());
        assertEquals(0.5, music.getPitch(), 0);
        assertEquals(1.5, music.getVolume(), 0);
    }
    
    
    @Test
    public void music_Default() {
        Music music = new Music(Configurations.BLANK);
        
        assertEquals(Sound.BLOCK_GLASS_BREAK, music.getSound());
        assertEquals(SoundCategory.PLAYERS, music.getCategory());
        assertEquals(1, music.getPitch(), 0);
        assertEquals(1, music.getVolume(), 0);
    }
    
    
    @Test
    public void play_Player() {
        music.play(player);
        
        verify(player).playSound(player.getLocation(), music.getSound(), music.getCategory(), music.getVolume(), music.getPitch());
    }
    
    
    @Test
    public void play_Location() {
        music.play(location);
        
        verify(location.getWorld()).playSound(location, music.getSound(), music.getCategory(), music.getVolume(), music.getPitch());
    }
    
}
