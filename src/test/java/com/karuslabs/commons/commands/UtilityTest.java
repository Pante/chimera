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

import com.karuslabs.commons.commands.Utility;
import com.karuslabs.commons.commands.Command;
import java.util.*;

import junitparams.*;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


@RunWith(JUnitParamsRunner.class)
public class UtilityTest {
    
    @Test
    @Parameters
    public void trim(String[] args, String[] expected) {
        String[] returned = Utility.trim(args);
        assertArrayEquals(expected, returned);
    }
    
    public Object[] parametersForTrim() {
        return new Object[] {
            new Object[] {new String[] {"1", "2", "3"}, new String[] {"2", "3"}},
            new Object[] {new String[] {"1"}, new String[] {}},
            new Object[] {new String[] {}, new String[] {}}
        };
    }
    
    
    @Test
    @Parameters
    public void getArgumentOrDefault(String[] arguments, int index, String expected) {
        String returned = Utility.getArgumentOrDefault(arguments, index, "");
        assertEquals(expected, returned);
    }
    
    public Object[] parametersForGetArgumentOrDefault() {
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
    @Parameters(source = CommandProvider.class)
    public void flatMap(Command command, Set<String> expected) {
        Map<String, Command> commands = new HashMap<>(2);
        Utility.flapMap(command.getName(), command, commands);
        
        assertEquals(commands.keySet(), expected);
    }
    
    
}
