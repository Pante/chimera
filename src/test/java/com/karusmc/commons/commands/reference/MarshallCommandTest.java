/*
 * Copyright (C) 2017 PanteLegacy @ karusmc.com
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
package com.karusmc.commons.commands.reference;

import com.karusmc.commons.commands.*;

import java.util.*;

import junitparams.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class MarshallCommandTest {
    
    private MarshallCommand command;
    private PluginCommand delegate;
    private Map<String, Command> subcommands;
    private Command subcommand;
    
    
    public MarshallCommandTest() {
        delegate = mock(PluginCommand.class);
        subcommands = Collections.singletonMap("subcommand", subcommand = mock(Command.class));
        
        command = new MarshallCommand(delegate, subcommands);
    }
    
    
    @Test
    @Parameters
    public void execute(String[] args, int delegateTimes, int subcommandTimes) {
        command.execute(null, args);
        
        verify(delegate, times(delegateTimes)).execute(any(), any());
        verify(subcommand, times(subcommandTimes)).execute(any(), any());
    }
    
    public Object[] parametersForExecute() {
        return new Object[] {
            new Object[] {new String[] {}, 1, 0},
            new Object[] {new String[] {"argument"}, 1, 0},
            new Object[] {new String[] {"subcommand", "subcommand argument"}, 0, 1}
        };
    }
    
    
    @Test
    @Parameters
    public void tabComplete(String[] arguments, int times, List<String> expected) {
        List<String> returned = command.tabComplete(null, null, arguments);
        
        verify(subcommand, times(times)).tabComplete(any(), any(), any());
        assertEquals(expected, returned);
    }
    
    public Object[] parametersForTabComplete() {
        List emptyList = Collections.emptyList();
        return new Object[] {
            new Object[] {new String[] {}, 0, emptyList},
            new Object[] {new String[] {"subcommand"}, 0, Arrays.asList("subcommand")},
            new Object[] {new String[] {"invalid"}, 0, emptyList},
            new Object[] {new String[] {"subcommand", "blah"}, 1, emptyList},
            new Object[] {new String[] {"invalid", "blah"}, 0, emptyList}
        };
    }
    
}
