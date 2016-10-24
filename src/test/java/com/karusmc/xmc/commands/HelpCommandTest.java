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
package com.karusmc.xmc.commands;

import com.karusmc.xmc.core.XMCommand;

import java.util.*;

import junitparams.*;

import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
@RunWith(JUnitParamsRunner.class)
public class HelpCommandTest {
    
    private HelpCommand command;
    
    
    public HelpCommandTest() {
        Plugin plugin = mock(Plugin.class);
        when(plugin.getName()).thenReturn("test");
        
        command = new HelpCommand(plugin, "help", 2);
    }
    
    
    @Before
    public void setup() {
        command.getCommands().clear();
    }
    
    
    @Test
    public void setCommands() {
        Map<String, XMCommand> commands = new HashMap<>();
        
        DispatchCommand dispatcher = new DispatchCommand(null, "command");       
        
        XMCommand subcommand = mock(XMCommand.class);
        when(subcommand.getName()).thenReturn("subcommand");     
       
        commands.put(dispatcher.getName(), dispatcher);
        dispatcher.getCommands().put(subcommand.getName(), subcommand);
        
        command.setCommands(commands);
        
        assertEquals(command.getCommands().keySet(), new HashSet<>(Arrays.asList("command", "command subcommand")));
    }
    
    
    @Test
    @Parameters(method = "register_parameters")
    public void register(Command toUpdate, Set<String> expectedCommands) {
        command.register(toUpdate);
        assertEquals(command.getCommands().keySet(), expectedCommands);
    }
    
    public Object[] register_parameters() {
        return new Object[] {
            new Object[] {mock(Command.class), Collections.emptySet()},
            new Object[] {command, new HashSet<>(Arrays.asList("help"))}
        };
    }
    
    
    @Test
    @Parameters(method = "registerAll_parameters")
    public void registerAll(Collection<? extends Command> commands, Set<String> expectedCommands) {
        command.registerAll(commands);
        assertEquals(command.getCommands().keySet(), expectedCommands);
    }
    
    public Object[] registerAll_parameters() {
        return new Object[] {
            new Object[] {Arrays.asList(mock(Command.class)), Collections.emptySet()},
            new Object[] {Arrays.asList(command), new HashSet<>(Arrays.asList("help"))}
        };
    }
    
    
    @Test
    public void unregister() {
        command.getCommands().put("command", command);
        command.getCommands().put("command subcommand", command);
        
        command.unregister("command");
        
        assertTrue(command.getCommands().isEmpty());
    }
    
    
    @Test
    public void unregisterAll() {
        command.getCommands().put("a", command);
        command.getCommands().put("b", command);
        
        command.unregisterAll();
        
        assertTrue(command.getCommands().isEmpty());
    }
    
    
    @Test
    @Parameters(source = CommandProvider.class)
    public void getUsages(Map<String, XMCommand> commands, int page, String criteria, String[] expectedArray) {
        command.getCommands().putAll(commands);
        String[] returned = command.getUsages(null, page, criteria);
        
        assertArrayEquals(expectedArray, returned);
    }
    
}
