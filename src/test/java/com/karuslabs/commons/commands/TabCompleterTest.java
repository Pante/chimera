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

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import org.junit.Test;
import org.junit.runner.RunWith;

import static java.util.Collections.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class TabCompleterTest {
    
    private static final String[] EMPTY = new String[0];
    
    private TabCompleter completer;
    private TabCompleter playerNames;
    private Command command;
    private Command subcommand;
    
    
    public TabCompleterTest() {
        completer = spy(new StubCompleter());
        playerNames = TabCompleter.PLAYER_NAMES;
        
        command = new Command("", mock(Plugin.class), null, null);
        subcommand = mock(Command.class);
        
        command.getNestedCommands().put("subcommand", subcommand);
        command.getNestedNames().add("subcommand");
    }
    
    
    @Test
    @Parameters
    public void tabComplete(String[] arguments, int subcommandTimes, int playerTimes, List<String> expected) {
        List<String> returned = completer.tabComplete(null, command, null, arguments);
        
        verify(subcommand, times(subcommandTimes)).tabComplete(any(), any(), any());
        verify(completer, times(playerTimes)).onTabComplete(null, command, null, arguments);
        
        assertEquals(expected, returned);
    }
    
    protected Object[] parametersForTabComplete() {
        return new Object[] {
            new Object[] {EMPTY, 0, 1, EMPTY_LIST},
            new Object[] {new String[] {"subcommand"}, 0, 0, singletonList("subcommand")},
            new Object[] {new String[] {"invalid"}, 0, 0, EMPTY_LIST},
            new Object[] {new String[] {"subcommand", "blah"}, 1, 0, EMPTY_LIST},
            new Object[] {new String[] {"invalid", "blah"}, 0, 1, EMPTY_LIST}
        };
    }
    
    
    private static class StubCompleter implements TabCompleter {

        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            return EMPTY_LIST;
        }
        
    }
    
}
