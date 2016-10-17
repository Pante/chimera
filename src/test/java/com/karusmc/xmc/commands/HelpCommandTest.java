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

import org.junit.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class HelpCommandTest {
    
    private HelpCommand command;
    
    public HelpCommandTest() {
        command = spy(new HelpCommand(null, null));
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
    public void update() {
        
    }
    
}
