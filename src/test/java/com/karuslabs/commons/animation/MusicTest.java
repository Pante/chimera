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
        Music music = new Music(BLANK);
        
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
