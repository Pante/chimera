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

import org.bukkit.Server;
import org.bukkit.command.CommandMap;

import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class CommandInjectorTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private CommandInjector injector;
    private StubServer server;
    private CommandMap commandMap;
    
    
    public CommandInjectorTest() {
        server = new StubServer((commandMap = mock(CommandMap.class)));
    }
    
    
    @Test
    public void constructor_ThrowsException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Server class doest not contain field: commandMap");
        
        injector = new CommandInjector(mock(Server.class));
    }
    
    
    @Test
    public void getCommandMap_ReturnsCommandMap() {
        injector = new CommandInjector(server);
        assertEquals(commandMap, injector.getCommandMap());
    }
    
    
    @Test
    public void setCommandMap_SetsCommandMap() {
        server = new StubServer(mock(CommandMap.class));
        injector = new CommandInjector(server);
        
        commandMap = mock(CommandMap.class);
        injector.setCommandMap(commandMap);
        
        assertEquals(commandMap, server.getCommandMap());
    }
    
}
