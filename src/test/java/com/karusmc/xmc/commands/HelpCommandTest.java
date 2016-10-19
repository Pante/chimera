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
import java.util.logging.Level;
import java.util.logging.Logger;

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
        
        command = spy(new HelpCommand(plugin, "help"));
        command.setPageSize(2);
    }
    
    
    @Before
    public void setup() {
        command.getCommands().clear();
    }
    
    
    @Test
    public void setCommands() {
        command.getCommands().put("1", mock(XMCommand.class));
        
        Map<String, XMCommand> commands = new HashMap<>();
        
        DispatchCommand dispatcher = new DispatchCommand(null, "command");       
        
        XMCommand subcommand = mock(XMCommand.class);
        when(subcommand.getName()).thenReturn("subcommand");     
        
        dispatcher.getCommands().put(subcommand.getName(), subcommand);        
        commands.put(dispatcher.getName(), dispatcher);
        
        command.setCommands(commands);
        
        assertEquals(2, command.getCommands().size());
        assertTrue(command.getCommands().keySet().containsAll(Arrays.asList("command", "command subcommand")));
    }
    
    
    @Test
    @Parameters(method = "update_parameters")
    public void update(Command toUpdate, int size, List<String> expectedCommands) {
        Map<String, XMCommand> commands = command.getCommands();
        
        command.update(null, toUpdate);
        
        assertEquals(size, commands.size());
        assertTrue(commands.keySet().containsAll(expectedCommands));
    }
    
    public Object[] update_parameters() {
        return new Object[] {
            new Object[] {mock(Command.class), 0, Collections.emptyList()},
            new Object[] {command, 1, Arrays.asList("help")}
        };
    }
    
    
    @Test
    @Parameters(source = CommandProvider.class)
    public void getUsages(Map<String, XMCommand> commands, int page, String criteria, String[] expectedArray) {
        command.getCommands().putAll(commands);
        String[] returned = command.getUsages(null, page, criteria);
        
        Logger.getAnonymousLogger().log(Level.SEVERE, returned.toString());
        
        assertArrayEquals(expectedArray, returned);
    }
    
}
