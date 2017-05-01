/*
 * Copyright (C) 2017 Karus Labs
 * All rights reserved.
 */
package com.karuslabs.commons.animation;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;

import org.junit.Test;

import static com.karuslabs.commons.Yaml.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class ActionBarTest {
    
    private ActionBar bar;
    
    
    public ActionBarTest() {
        bar = spy(new ActionBar(ANIMATION.getConfigurationSection("actionbar")));
    }
    
    
    @Test
    public void actionbar() {
        assertEquals(ChatColor.RED + " test message", bar.getMessage());
        assertEquals(ChatColor.RED, bar.getColor());
        assertEquals(5, bar.getFrames());
    }
    
    
    @Test
    public void actionbar_default() {
        ActionBar bar = new ActionBar(BLANK);
        
        assertEquals("", bar.getMessage());
        assertEquals(ChatColor.WHITE, bar.getColor());
        assertEquals(4, bar.getFrames());
    }
    
    
    @Test
    public void animate() {
        Player player = mock(Player.class);
        
        doNothing().when(bar).animate(player, 0);
        
        bar.animate(player);
        
        verify(bar).animate(player, 0);
    }

}
