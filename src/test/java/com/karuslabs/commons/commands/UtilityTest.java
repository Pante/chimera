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

import com.google.common.collect.Sets;
import com.karuslabs.commons.commands.reference.MarshallCommand;

import java.util.*;

import junitparams.*;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class UtilityTest {
    
    private static final String[] EMPTY = new String[0];
    
    
    @Test
    @Parameters
    public void trim(String[] args, String[] expected) {
        String[] returned = Utility.trim(args);
        assertArrayEquals(expected, returned);
    }
    
    protected Object[] parametersForTrim() {
        return new Object[] {
            new Object[] {new String[] {"1", "2", "3"}, new String[] {"2", "3"}},
            new Object[] {new String[] {"1"}, EMPTY},
            new Object[] {EMPTY, EMPTY}
        };
    }
    
    
    @Test
    @Parameters
    public void getArgumentOrDefault(String[] arguments, int index, String expected) {
        String returned = Utility.getArgumentOrDefault(arguments, index, "");
        assertEquals(expected, returned);
    }
    
    protected Object[] parametersForGetArgumentOrDefault() {
        return new Object[] {
            new Object[] {new String[] {"1", "2", "3"}, 1, "2"},
            new Object[] {new String[] {"1", "2", "3"}, 2, "3"},
            new Object[] {new String[] {"1", "2", "3"}, 3, ""}
        };
    } 
    
    
    @Test
    @Parameters({"3, 3", "invalid, 1", "3.01, 1", "-3, 1"})
    public void toInt(String argument, int expected) {
        int returned = Utility.toInt(argument);
        assertEquals(expected, returned);
    }
    
    
    @Test
    @Parameters({"7, 3, 3", "2, 3, 1", "0 , 0, 0", "0, 3, 0", "3, 0, 0"})
    public void getTotalPages(int totalEntries, int pageSize, int expected) {
        int returned = Utility.getTotalPages(totalEntries, pageSize);
        assertEquals(expected, returned);
    }
    
    
    @Test
    @Parameters({"13, 4, 3, 8", "7, 4, 3, 7"})
    public void getFirstIndex(int totalEntries, int pageSize, int page, int expected) {
        int returned = Utility.getFirstIndex(totalEntries, pageSize, page);
        assertEquals(expected, returned);
    }
    
    
    @Test
    @Parameters({"3, 2, 1, 2", "1, 2, 1, 1"})
    public void getLastIndex(int totalEntries, int pageSize, int page, int expected) {
        int returned = Utility.getLastIndex(totalEntries, pageSize, page);
        assertEquals(expected, returned);
    }
    
    
    @Test
    @Parameters
    public void flatMap(Command command, Set<String> expected) {
        Map<String, Command> returned = new HashMap<>(2);
        Utility.flapMap(command.getName(), command, returned);
        
        assertEquals(expected, returned.keySet());
    }
    
    protected Object[] parametersForFlatMap() {
        MarshallCommand root = (MarshallCommand) stub("root", true);
        MarshallCommand command = (MarshallCommand) stub("command", true);
        Command subcommand = stub("subcommand", false);
        
        when(root.getSubcommands()).thenReturn(Collections.singletonMap("command", command));
        when(command.getSubcommands()).thenReturn(Collections.singletonMap("subcommand", subcommand));
        
        return new Object[] {
            root, Sets.newHashSet("root", "root command", "root command subcommand")
        };
    }
    
    protected Command stub(String name, boolean isMarshall) {
        Command command;
        if (isMarshall) {
            command = mock(MarshallCommand.class);
        } else {
            command = mock(Command.class);
        }
        
        when(command.getName()).thenReturn(name);
        return command;
    }
    
    
}
