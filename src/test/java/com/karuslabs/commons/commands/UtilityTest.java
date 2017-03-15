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

import junitparams.*;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class UtilityTest {
    
    private static final String[] EMPTY = new String[0];
    
    
    private Command command;
    private CommandSender sender;
    
    
    public UtilityTest() {
        command = mock(Command.class);
        sender = mock(CommandSender.class);
    }
    
    
    @Test
    @Parameters
    public void trim(String[] args, String[] expected) {
        String[] returned = Utility.trim(args);
        assertArrayEquals(expected, returned);
    }
    
    protected Object[] parametersForTrim() {
        return new Object[] {
            new Object[] {new String[] {"1", "2", "3"}, new String[] {"2", "3"}},
            new Object[] {new String[] {"1"}, EMPTY},
            new Object[] {EMPTY, EMPTY}
        };
    }
    
    
    @Test
    @Parameters({"1, true", "0, false", "2, false"})
    public void within(int length, boolean expected) {
        assertEquals(expected, Utility.within(1, 1, new String[length]));
    }
    
    
    @Test
    @Parameters
    public void isPermittedPlayer(CommandSender sender, boolean permission, boolean expected) {
        when(command.testPermissionSilent(any(CommandSender.class))).thenReturn(permission);
        
        assertEquals(expected, Utility.isPermittedPlayer(sender, command));
    }
    
    protected Object[] parametersForIsPermittedPlayer() {
        Player player = mock(Player.class);
        return new Object[] {
            new Object[] {player, true, true},
            new Object[] {player, false, false},
            new Object[] {sender, true, false},
            new Object[] {sender, false, false}
        };
}
    
}
