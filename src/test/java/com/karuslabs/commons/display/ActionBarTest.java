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
package com.karuslabs.commons.display;

import com.karuslabs.commons.display.ActionBar;
import com.karuslabs.commons.configuration.Configurations;

import com.karuslabs.mockkit.stub.StubScheduler;

import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import org.junit.Test;

import static com.karuslabs.commons.Yaml.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class ActionBarTest {
    
    private ActionBar bar;
    private Plugin plugin;
    
    public ActionBarTest() {
        Server server = when(mock(Server.class).getScheduler()).thenReturn(StubScheduler.INSTANCE).getMock();
        plugin = when(mock(Plugin.class).getServer()).thenReturn(server).getMock();
        bar = spy(new ActionBar(plugin, DISPLAY.getConfigurationSection("actionbar")));
    }
    
    
    @Test
    public void actionbar() {
        assertEquals(ChatColor.RED + " test message", bar.getMessage());
        assertEquals(ChatColor.RED, bar.getColor());
        assertEquals(5, bar.getFrames());
    }
    
    
    @Test
    public void actionbar_default() {
        ActionBar bar = new ActionBar(plugin, Configurations.BLANK);
        
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
    
    
    @Test
    public void animate_Delay() {
        Player.Spigot spigot = mock(Player.Spigot.class);
        Player player = when(mock(Player.class).spigot()).thenReturn(spigot).getMock();
        
        bar.animate(player, 0);
        
        verify(spigot, times(6)).sendMessage(eq(ChatMessageType.ACTION_BAR), any(TextComponent.class));
    }

}
