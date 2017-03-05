/*
 * Copyright (C) 2016 Karus Labs
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

import com.karuslabs.commons.commands.events.CommandRegistrationEvent;
import com.karuslabs.commons.test.StubServer;

import java.util.*;

import junitparams.*;

import org.bukkit.Server;
import org.bukkit.event.Cancellable;
import org.bukkit.plugin.*;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class CommandMapProxyTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private CommandMapProxy proxy;
    private StubServer server;
    private PluginCommand command;
    
    
    public CommandMapProxyTest() {        
        server = new StubServer();
        proxy = spy(new CommandMapProxy(server));
        command = mock(PluginCommand.class);
    }
    
    
    @Test
    public void CommandMapProxy_ThrowsException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Server does not contain field: commandMap");
        
        new CommandMapProxy(mock(Server.class));
    }
    
    
    @Test
    public void register_Map() {
        Map<String, Command> commands = new HashMap<>(1);
        commands.put("command", command);
        
        proxy.register(commands);
        
        verify(proxy, times(1)).register("command", command);
    }
    
    
    @Test
    @Parameters({"false, 1", "true, 0"})
    public void register_Command(boolean cancelled, int times) {
        Answer<Void> setCancelled = invocation -> {
            ((Cancellable) invocation.getArgument(0)).setCancelled(cancelled);
            return null;
        };
        
        doAnswer(setCancelled).when(server.getPluginManager()).callEvent(any(CommandRegistrationEvent.class));
        
        proxy.register("", command);
        
        verify(server.getSimpleCommandMap(), times(times)).register(any(), any());
    }
    
    
    @Test
    @Parameters
    public void getPluginCommands(Command command, Set<String> expected) {
        List<org.bukkit.command.Command> bukkitCommands = Arrays.asList(command);
        
        when(server.getSimpleCommandMap().getCommands()).thenReturn(bukkitCommands);
        
        Set<String> returned = proxy.getPluginCommands("test").keySet();
        assertEquals(expected, returned);
    }
    
    protected Object[] parametersForGetPluginCommands() {        
        return new Object[] {
            new Object[] {stub("command name", "test"), Collections.singleton("command name")},
            new Object[] {stub("", "differentPlugin"), Collections.<String>emptySet()},
            new Object[] {new Command("", Criteria.NONE) {}, Collections.<String>emptySet()}
        };
    }
    
    protected PluginCommand stub(String name, String pluginName) {
        Plugin plugin = mock(Plugin.class);
        when(plugin.getName()).thenReturn(pluginName);
        return new PluginCommand(name, plugin, Criteria.NONE) {};
    }
    
}
