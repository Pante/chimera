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
package com.karuslabs.commons.commands.reference;

import com.karuslabs.commons.commands.Command;

import java.util.*;

import static org.mockito.Mockito.*;


public class CommandProvider {
    
    private static Command mockCommand(String name, String usage, boolean permission) {
        Command command = mock(Command.class);
        when(command.getName()).thenReturn(name);
        when(command.getUsage()).thenReturn(usage);
        when(command.testPermissionSilent(null)).thenReturn(permission);
        
        return command;
    }
    
    
    public static Object[] provideWithCriteria() {
        
        Map<String, Command> commands = new HashMap<>(3);
        commands.put("command", mockCommand("command", "command usage", true));
        commands.put("invalidcommand", mockCommand("invalidcommand", "invalidcommand usage", true));
        commands.put("invalidpermission", mockCommand("invalidpermission", "invalidpermission usage", false));
        
        return new Object[] {
            new Object[] {commands, 1, "com", new String[] {"command usage"}}
        };
    }
    
    public static Object[] provideWithPageLength() {
        
        Map<String, Command> commands = new HashMap<>(3);
        commands.put("1", mockCommand("1", "1 usage", true));
        commands.put("2", mockCommand("2", "2 usage", true));
        commands.put("3", mockCommand("3", "3 usage", true));
        
        return new Object[] {
            new Object[] {commands, 1, "", new String[] {"1 usage", "2 usage"}},
            new Object[] {commands, 2, "", new String[] {"3 usage"}}
        };
    }
    
}
