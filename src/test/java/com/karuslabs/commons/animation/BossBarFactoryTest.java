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

import java.util.Collections;

import org.bukkit.Server;
import org.bukkit.boss.*;
import org.bukkit.entity.Player;

import org.junit.Test;

import static com.karuslabs.commons.Yaml.ANIMATION;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class BossBarFactoryTest {

    private BossBarFactory factory;
    private Server server;
    private BossBar bar;
    private Player player;
    
    
    public BossBarFactoryTest() {
        bar = mock(BossBar.class);
        server = when(mock(Server.class).createBossBar("test message", BarColor.BLUE, BarStyle.SOLID, BarFlag.CREATE_FOG)).thenReturn(bar).getMock();
        factory = new BossBarFactory(server, ANIMATION.getConfigurationSection("bar"));
        player = mock(Player.class);
    }
    
    
    @Test
    public void bossBarFactory() {
        assertEquals("test message", factory.getMessage());
        assertEquals(BarColor.BLUE, factory.getColor());
        assertEquals(BarStyle.SOLID, factory.getStyle());
        assertThat(factory.getFlags(), equalTo(new BarFlag[] {BarFlag.CREATE_FOG}));
    }
    
    
    @Test
    public void newBar_Players() {
        BossBar bar = factory.newBar(player);
        
        verify(server).createBossBar("test message", BarColor.BLUE, BarStyle.SOLID, BarFlag.CREATE_FOG);
        verify(bar).addPlayer(player);
    }
    
    
    @Test
    public void newBar_Collection() {
        BossBar bar = factory.newBar(Collections.singleton(player));
        
        verify(server).createBossBar("test message", BarColor.BLUE, BarStyle.SOLID, BarFlag.CREATE_FOG);
        verify(bar).addPlayer(player);
    }
    
}
