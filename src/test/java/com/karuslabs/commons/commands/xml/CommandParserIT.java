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

import com.karuslabs.commons.commands.Command;
import com.karuslabs.commons.commands.reference.MarshallCommand;

import java.util.*;

import org.junit.Test;

import static org.mockito.Mockito.*;


public class CommandParserIT {
    
    private CommandParser parser;
    private Map<String, Command> commands;
    
    private MarshallCommand command;
    private Command subcommand;
    
    
    public CommandParserIT() {
        parser = new CommandParser(new CommandsComponent());
        commands = new HashMap<>(1);
        
        command = mock(MarshallCommand.class);
        when(command.getName()).thenReturn("command");
        
        subcommand = mock(Command.class);
        when(subcommand.getName()).thenReturn("subcommand");

        when(command.getCommands()).thenReturn(Collections.singletonMap("subcommand", subcommand));
        
        commands.put("command", command);
    }
    
    
    @Test
    public void parse() {
        parser.parse(getClass().getClassLoader().getResourceAsStream("commands/commands.xml"), commands);
        
        verify(command, times(1)).newAliases(Arrays.asList(new String[] {"cmd", "comm"}));
        verify(command, times(1)).newDescription("command description");
        verify(command, times(1)).newUsage("command usage");
        
        verify(command, times(1)).newPermission("command.permission");
        verify(command, times(1)).newPermissionMessage("You do not have permission to use this command");
        
        
        verify(subcommand, times(1)).newAliases(Arrays.asList(new String[] {"subcmd", "subcomm"}));
        verify(subcommand, times(1)).newDescription("subcommand description");
        verify(subcommand, times(1)).newUsage("subcommand usage");
        
        verify(subcommand, times(1)).newPermission("subcommand.permission");
        verify(subcommand, times(1)).newPermissionMessage("You do not have permission to use this subcommand");
    }
    
}
