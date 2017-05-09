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

import com.karuslabs.mockkit.rule.MockkitRule;
import com.karuslabs.mockkit.stub.StubServer;

import java.util.Collections;

import junitparams.*;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class ProxiedCommandMapTest {
        
    @ClassRule
    public static MockkitRule mockkit = MockkitRule.INSTANCE;
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    
    private ProxiedCommandMap proxy;
    private Command command;
    
    
    public ProxiedCommandMapTest() {
        proxy = spy(new ProxiedCommandMap(StubServer.INSTANCE));
        command = mock(Command.class);
    }
    
    
    @Test
    public void CommandMapProxy_ThrowsException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Server contains an incompatible CommandMap implementation");
        
        new ProxiedCommandMap(mock(Server.class));
    }
    
    
    @Test
    @Parameters
    public void getCommand(org.bukkit.command.Command command, Command expected) {
        when(proxy.getProxiedMap().getCommand(any(String.class))).thenReturn(command);
        
        assertEquals(expected, proxy.getCommand("name"));
    }
    
    protected Object[] parametersForGetCommand() {
        Command command = new Command(null, null, CommandExecutor.DEFAULT, TabCompleter.PLAYER_NAMES);
        return new Object[] {
            new Object[] {command, command},
            new Object[] {mock(org.bukkit.command.Command.class), null}
        };
    }
    
    
    @Test
    @Parameters
    public void getCommands(org.bukkit.command.Command command, Plugin plugin, boolean empty) {
        when(proxy.getProxiedMap().getCommands()).thenReturn(Collections.singleton(command));
        
        assertEquals(empty, proxy.getCommands(plugin).isEmpty());
    }
    
    protected Object[] parametersForGetCommands() {
        Plugin plugin = mock(Plugin.class);
        Command command = new Command("", plugin, CommandExecutor.DEFAULT, TabCompleter.PLAYER_NAMES);
        
        org.bukkit.command.Command mock = mock(org.bukkit.command.Command.class);
        
        return new Object[] {
            new Object[] {command, plugin, false},
            new Object[] {command, mock(Plugin.class), true},
            new Object[] {mock, plugin, true},
            new Object[] {mock, mock(Plugin.class), true}
        };
    }

}
