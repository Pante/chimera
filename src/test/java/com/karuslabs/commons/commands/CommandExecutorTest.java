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

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import org.junit.*;
import org.junit.runner.RunWith;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class CommandExecutorTest {
    
    private static final String[] EMPTY = new String[0];
    
    private CommandExecutor executor;
    private CommandSender sender;
    private Command command;
    private Command subcommand;
    private Option option;
    
    
    public CommandExecutorTest() {
        executor = spy(new StubExecutor());
        sender = when(mock(CommandSender.class).hasPermission(anyString())).thenReturn(true).getMock();
        
        command = new Command("", mock(Plugin.class), null);
        command.setPermission("");
        
        subcommand = when(mock(Command.class).tabComplete(any(), any(), any())).thenReturn(singletonList("subcommand tabComplete")).getMock();
        when(subcommand.getName()).thenReturn("subcommand");
        when(subcommand.getPermission()).thenReturn("");
        
        option = mock(Option.class);
        
        command.subcommands.put("subcommand", subcommand);
        command.getOptions().put("option", option);
    }
    
    
    @Before
    public void setup() {
        command.subcommands.clear();
    }
    
    
    @Test
    public void default_CommandExeuctor() {
        CommandExecutor.DEFAULT.onExecute(sender, command, null, EMPTY);
        
        verify(sender).sendMessage(ChatColor.RED + "Unknown command! Type / help for more information");
    }
    
    
    @Test
    @Parameters
    public void execute(boolean permission, String[] args, int commandTimes, int extensionTimes, int executeTimes, int permissionTimes) {
        command.subcommands.put(subcommand.getName(), subcommand);
        
        when(sender.hasPermission(any(String.class))).thenReturn(permission);

        doNothing().when(executor).onNoPermission(sender, command, null, args);
    
        executor.execute(sender, command, null, args);

        verify(subcommand, times(commandTimes)).execute(any(), any(), any());
        verify(option, times(extensionTimes)).execute(sender, command);
        verify(executor, times(executeTimes)).onExecute(any(), any(), any(), any());
        verify(executor, times(permissionTimes)).onNoPermission(sender, command, null, args);
    }
    
    protected Object[] parametersForExecute() {
        return new Object[] {
            new Object[] {true, new String[] {"subcommand"}, 1, 0, 0, 0},
            new Object[] {false, new String[] {"subcommand"}, 1, 0, 0, 0},
            new Object[] {true, new String[] {"option"}, 0, 1, 0, 0},
            new Object[] {true, EMPTY, 0, 0, 1, 0},
            new Object[] {true, new String[] {"argument"}, 0, 0, 1, 0},
            new Object[] {false, EMPTY, 0, 0, 0, 1}
        };
    }
    
    
    @Test
    public void onInvalid() {
        command.setPermissionMessage("message");
        
        executor.onNoPermission(sender, command, null, EMPTY);
        
        verify(sender).sendMessage(ChatColor.RED + "message");
    }
    
    
    @Test
    @Parameters
    public void tabComplete(boolean empty, String[] args, List<String> expected) {
        if (!empty) {
            command.getSubcommands().put(subcommand.getName(), subcommand);
        }

        List<String> returned = executor.tabComplete(sender, command, null, args);

        assertThat(returned, equalTo(expected));
    }

    protected Object[] parametersForTabComplete() {
        return new Object[]{
            new Object[]{false, new String[]{"sub"}, singletonList("subcommand")},
            new Object[]{true, new String[]{"sub"}, singletonList("Pante")},
            new Object[]{false, new String[]{"subcommand", "argument"}, singletonList("subcommand tabComplete")},
            new Object[]{true, new String[]{"argument", "argument"}, singletonList("Pante")}
        };
    }
    
    
    // Workaround until Mockito supports spying lambda expressions
    private static class StubExecutor implements CommandExecutor {

        @Override
        public void onExecute(CommandSender sender, Command command, String label, String[] args) {
            
        }
        
        
        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            return singletonList("Pante");
        }
        
    }
    
}
