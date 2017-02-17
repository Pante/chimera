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
package com.karuslabs.commons.commands.reference;

import com.karuslabs.commons.commands.reference.HelpCommand;
import com.karuslabs.commons.commands.Criteria;
import com.karuslabs.commons.commands.Command;

import java.util.*;

import junitparams.*;

import org.bukkit.plugin.Plugin;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class HelpCommandTest {
    
    private HelpCommand command;
    
    
    public HelpCommandTest() {
        Plugin plugin = mock(Plugin.class);
        when(plugin.getName()).thenReturn("test");
        
        command = new HelpCommand("help", plugin, Criteria.NONE, new HashMap<>(), 2);
    }
 
    
    @Before
    public void setup() {
        command.getCommands().clear();
    }
    
    
    @Test
    @Parameters(source = CommandProvider.class)
    public void getUsages(Map<String, Command> commands, int page, String criteria, String[] expectedArray) {
        command.getCommands().putAll(commands);
        String[] returned = command.getUsages(null, page, criteria);
        
        assertArrayEquals(expectedArray, returned);
    }
    
}
