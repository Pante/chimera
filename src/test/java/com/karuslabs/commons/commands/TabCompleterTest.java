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

import org.junit.*;
import org.junit.runner.RunWith;

import static java.util.Collections.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class TabCompleterTest {
    
    private TabCompleter completer;
    private Command command;
    private Command subcommand;
    private CommandSender sender;
    
    
    public TabCompleterTest() {
        completer = spy(new StubCompleter());

        subcommand = when(mock(Command.class).tabComplete(any(), any(), any())).thenReturn(singletonList("subcommand tabComplete")).getMock();
        when(subcommand.getName()).thenReturn("subcommand");
        when(subcommand.getPermission()).thenReturn("");
        
        command = new Command("", mock(Plugin.class), null, null);
        
        sender = when(mock(CommandSender.class).hasPermission(anyString())).thenReturn(true).getMock();
    }
    
    
    @Before
    public void setup() {
        command.getSubcommands().clear();
    }

    
    @Test
    @Parameters
    public void tabComplete(boolean empty, String[] args, List<String> expected) {
        if (!empty) {
            command.getSubcommands().put(subcommand.getName(), subcommand);
        }
        
        List<String> returned = completer.tabComplete(sender, command, null, args);
        
        assertThat(returned, equalTo(expected));
    }
    
    protected Object[] parametersForTabComplete() {
        return new Object[] {
            new Object[]{false, new String[]{"sub"}, singletonList("subcommand")},
            new Object[]{true, new String[]{"sub"}, singletonList("Pante")},
            new Object[]{false, new String[]{"subcommand", "argument"}, singletonList("subcommand tabComplete")},
            new Object[]{true, new String[]{"argument", "argument"}, singletonList("Pante")}
        };
    }
    
    
    // Workaround until Mockito supports spying lambda expressions
    private static class StubCompleter implements TabCompleter {

        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            return singletonList("Pante");
        }
        
    }
    
}
