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
package com.karuslabs.commons.commands;

import com.karuslabs.commons.commands.events.RegistrationEvent;
import com.karuslabs.commons.commands.yml.Parser;

import java.util.*;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.*;

import org.junit.Test;

import static org.mockito.Mockito.*;


public class CommandManagerTest {
    
    private CommandManager manager;
    private Plugin plugin;
    private PluginManager pluginManager;
    private ProxiedCommandMap map;
    private Parser parser;
    
    
    public CommandManagerTest() {
        plugin = mock(Plugin.class);
        pluginManager = mock(PluginManager.class);
        map = mock(ProxiedCommandMap.class);
        parser = mock(Parser.class);
        
        manager = spy(new CommandManager(plugin, pluginManager, map, parser));
    }
    
    
    @Test
    public void load() {
        doReturn(Collections.EMPTY_LIST).when(manager).load("commands.yml");
        
        manager.load();
        
        verify(manager).load("commands.yml");
    }
    
    
    @Test
    public void load_Path() {
        when(plugin.getName()).thenReturn("");
        doReturn(Collections.EMPTY_LIST).when(parser).parse(any(ConfigurationSection.class));
        
        manager.load("commands/commands.yml");
        
        verify(parser).parse(any(ConfigurationSection.class));
        verify(map).registerAll(any(String.class), any(List.class));
        verify(pluginManager).callEvent(argThat((RegistrationEvent event) -> event.getCommands().isEmpty()));
    }
    
    
    @Test
    public void getCommand() {
        manager.getCommand("name");
        
        verify(map).getCommand("name");
    }
    
}
