/*
 * Copyright (C) 2016 PanteLegacy @ karusmc.com
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
package com.karusmc.xmc.core;

import java.util.*;

import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class CommandMapProxyTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private CommandMapProxy injector;
    private StubServer server;
    private SimpleCommandMap commandMap;
    
    private Plugin plugin;
    private XMCommand command;
    private Command mockCommand;
    
    
    public CommandMapProxyTest() {
        server = new StubServer((commandMap = mock(SimpleCommandMap.class)));

        command = mock(XMCommand.class);
        when(command.getPlugin()).thenReturn(plugin = mock(Plugin.class));
        when(plugin.getName()).thenReturn("name");
        
        when(command.getName()).thenReturn("XMCommand");
        
        mockCommand = mock(Command.class);
        
        when(commandMap.getCommands()).thenReturn(Arrays.asList(command, mockCommand));
    }
    
    
    @Test
    public void constructor_ThrowsException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Server instance does not contain field: commandMap");
        
        injector = new CommandMapProxy(mock(Server.class));
    }
    
    
    @Test
    public void getCommandMap_ReturnsCommandMap() {
        injector = new CommandMapProxy(server);
        assertTrue(commandMap == injector.getUnderlyingCommandMap());
    }
    
    
    @Test
    public void getPluginCommands_ReturnsPluginCommand() {
        injector = new CommandMapProxy(server);
        assertEquals(injector.getPluginCommands("name", Command.class).keySet(), new HashSet<>(Arrays.asList("XMCommand")));
    }
    
    
    @Test
    public void getXMCommands_ReturnsPluginCommand() {
        injector = new CommandMapProxy(server);
        Map<String, XMCommand> commands = injector.getPluginCommands("name", XMCommand.class);
        
        assertEquals(commands.keySet(), new HashSet<>(Arrays.asList("XMCommand")));
    }
    
}
