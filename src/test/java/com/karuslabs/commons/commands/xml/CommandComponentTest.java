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
package com.karuslabs.commons.commands.xml;

import com.karuslabs.commons.core.xml.*;
import com.karuslabs.commons.commands.Command;
import com.karuslabs.commons.commands.reference.MarshallCommand;

import com.karuslabs.commons.core.test.XMLResource;

import java.util.*;

import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.*;


public class CommandComponentTest {
    
    @Rule
    public XMLResource resource = new XMLResource().load(getClass().getClassLoader().getResourceAsStream("commands/commands-component.xml"), null);
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private CommandComponent component;
    private SetterComponent<Map<String, Command>> setter;
    private MarshallCommand command;
    
    
    public CommandComponentTest() {
        setter = mock(SetterComponent.class);
        command = mock(MarshallCommand.class);
        
        component = new CommandComponent(setter);
    }
    
    
    @Test
    public void parse() {
        component.parse(resource.getRoot(), command);
        
        verify(command, times(1)).setAliases(Arrays.asList(new String[] {"cmd", "comm"}));
        verify(command, times(1)).setDescription("command description");
        verify(command, times(1)).setUsage("command usage");
        
        verify(command, times(1)).setPermission("command.permission");
        verify(command, times(1)).setPermissionMessage("You do not have permission to use this command");
        
        verify(setter, times(1)).parse(any(), any());
    }
    
    
    @Test
    public void parse_ThrowsException() {
        exception.expect(ParserException.class);
        exception.expectMessage("Command: \"null\" does not implement the Marshall interface, unable to retrieve child commands");
        
        component.parse(resource.getRoot(), mock(Command.class));
    }
    
}
