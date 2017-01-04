/*
 * Copyright (C) 2016 PanteLegacy @ karusmc.com
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
package com.karusmc.commons.commands;

import com.karusmc.commons.commands.events.CommandRegistrationEvent;

import java.util.*;

import junitparams.*;

import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.event.Cancellable;
import org.bukkit.plugin.*;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class CommandMapProxyTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private CommandMapProxy proxy;
    private Server server;
    private SimpleCommandMap map;
    private PluginManager manager;
    
    
    public CommandMapProxyTest() {
        map = mock(SimpleCommandMap.class);
        manager = mock(PluginManager.class);
        
        server = new StubServer(map, manager);
        proxy = spy(new CommandMapProxy(server));
    }
    
    
    @Test
    public void CommandMapProxy_ThrowsException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Server does not contain field: commandMap");
        
        new CommandMapProxy(mock(Server.class));
    }
    
    
    @Test
    public void register_Map() {
        doNothing().when(proxy).register(any(), any());
        
        Map<String, Command> commands = new HashMap<>(1);
        Command command = mock(Command.class);
        commands.put("command", command);
        
        proxy.register(commands);
        
        verify(proxy, times(1)).register("command", command);
    }
    
    
    @Test
    @Parameters({"false, 1", "true, 0"})
    public void register_Command(boolean cancelled, int times) {
        doAnswer(invocation -> {
            ((Cancellable) invocation.getArgument(0)).setCancelled(cancelled);
            return invocation.getArgument(0);
        }).when(manager).callEvent(any(CommandRegistrationEvent.class));
        
        proxy.register("", mock(Command.class));
        
        verify(map, times(times)).register(any(), any());
    }
    
    
    @Test
    @Parameters
    public void getPluginCommands(Command command, Set<String> expected) {
        List<org.bukkit.command.Command> commands = new ArrayList<>(1);
        commands.add(command);
        when(map.getCommands()).thenReturn(commands);
        
        Set<String> returned = proxy.getPluginCommands("test").keySet();
        assertEquals(expected, returned);
    }
    
    public Object[] parametersForPluginCommands() {
        Command nonPluginCommand = mock(Command.class);
        PluginCommand differentPluginCommand = mock(PluginCommand.class);
        PluginCommand command = mock(PluginCommand.class);
        
        Plugin aPlugin = mock(Plugin.class);
        when(aPlugin.getName()).thenReturn("differentPlugin");
        when(differentPluginCommand.getPlugin()).thenReturn(aPlugin);
        
        Plugin plugin = mock(Plugin.class);
        when(plugin.getName()).thenReturn("test");
        when(differentPluginCommand.getPlugin()).thenReturn(aPlugin);
        
        return new Object[] {
            new Object[] {nonPluginCommand, Collections.<String>emptySet()},
            new Object[] {differentPluginCommand, Collections.<String>emptySet()},
            new Object[] {command, Collections.singleton("test")}
        };
    }
    
}
