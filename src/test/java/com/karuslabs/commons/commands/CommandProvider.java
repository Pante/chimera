/*
 * Copyright (C) 2017 PanteLegacy @ karusmc.com
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

import com.karuslabs.commons.commands.Command;
import com.karuslabs.commons.commands.reference.MarshallCommand;

import java.util.*;

import static org.mockito.Mockito.*;


public class CommandProvider {
    
    public static Object[] provideCommands() {
        MarshallCommand rootCommand = mock(MarshallCommand.class);
        when(rootCommand.getName()).thenReturn("rootCommand");
        
        MarshallCommand command = mock(MarshallCommand.class);
        when(command.getName()).thenReturn("command");
        
        Command subcommand = mock(Command.class);
        when(subcommand.getName()).thenReturn("subcommand");
        
        when(rootCommand.getCommands()).thenReturn(Collections.singletonMap("command", command));
        when(command.getCommands()).thenReturn(Collections.singletonMap("subcommand", subcommand));
        
        Set<String> expected = new HashSet<>(Arrays.asList(new String[] {"rootCommand", "rootCommand command", "rootCommand command subcommand"}));
        
        return new Object[] {
            new Object[] {rootCommand, expected}
        };
    }
    
}
