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
import com.karusmc.xmc.util.Else;
import java.util.Arrays;
import java.util.List;

import junitparams.*;

import org.bukkit.command.CommandSender;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
@RunWith(JUnitParamsRunner.class)
public class DispatchCommandTest {
    
    private DispatchCommand command;
    
    private XMCommand subcommand;
    private CommandSender sender;
    private Else handle;
    
    
    public DispatchCommandTest() {
        command = new DispatchCommand(null, "dispatch", handle = mock(Else.class));
        command.getCommands().put("subcommand", subcommand = mock(XMCommand.class));
        
        sender = mock(CommandSender.class);
    }
    
    
    @Test
    @Parameters(method = "execute_parameters")
    public void execute(String[] arguments, int subcommandTimes, int handleTimes) {
        command.execute(sender, arguments);
        
        verify(subcommand, times(subcommandTimes)).execute(any(), any());
        verify(handle, times(handleTimes)).handle(sender);
    }
    
    public Object[] execute_parameters() {
        return new Object[] {
            new Object[] {new String[] {"subcommand", "blah"}, 1, 0},
            new Object[] {new String[] {"subcommand"}, 1, 0},
            new Object[] {new String[] {} , 0, 1},
            new Object[] {new String[] {"invalid", "blah"}, 0, 1}
        };
    }
    
    
    @Test
    @Parameters(method = "tabComplete_parameters")
    public void tabComplete(String[] arguments, int times, List<String> expected) {
        List<String> returned = command.tabComplete(sender, null, arguments);
        
        verify(subcommand, times(times)).tabComplete(any(), any(), any());
        assertEquals(expected, returned);
    }
    
    public Object[] tabComplete_parameters() {
        return new Object[] {
            new Object[] {new String[] {}, 0, null},
            new Object[] {new String[] {"subcommand"}, 0, Arrays.asList("subcommand")},
            new Object[] {new String[] {"invalid"}, 0, Arrays.asList()},
            new Object[] {new String[] {"subcommand", "blah"}, 1, Arrays.asList()},
            new Object[] {new String[] {"invalid", "blah"}, 0, null}
        };
    }
    
}
