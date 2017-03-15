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

import java.util.*;

import junitparams.*;

import org.bukkit.plugin.Plugin;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class TabCompleterTest {
    
    private static final String[] EMPTY = new String[0];
    
    private TabCompleter completer;
    private Command command;
    private Command subcommand;
    
    
    public TabCompleterTest() {
        completer = TabCompleter.INSTANCE;
        
        command = new Command("", mock(Plugin.class), null, null);
        subcommand = mock(Command.class);
        
        command.getNestedCommands().put("subcommand", subcommand);
    }
    
    
    @Test
    @Parameters
    public void complete(String[] arguments, int times, List<String> expected) {
        List<String> returned = completer.complete(null, command, null, arguments);
        
        verify(subcommand, times(times)).tabComplete(any(), any(), any());
        assertEquals(expected, returned);
    }
    
    public Object[] parametersForComplete() {
        List<String> emptyList = Collections.emptyList();
        return new Object[] {
            new Object[] {EMPTY, 0, emptyList},
            new Object[] {new String[] {"subcommand"}, 0, Arrays.asList("subcommand")},
            new Object[] {new String[] {"invalid"}, 0, emptyList},
            new Object[] {new String[] {"subcommand", "blah"}, 1, emptyList},
            new Object[] {new String[] {"invalid", "blah"}, 0, emptyList}
        };
}
    
}
