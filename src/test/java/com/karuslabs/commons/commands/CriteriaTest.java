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

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class CriteriaTest {
    
    private Command command;
    private CommandSender sender;
    private String[] args;
    
    
    public CriteriaTest() {
        command = mock(Command.class);
        sender = mock(CommandSender.class);
        args = new String[0];
    }
    
    
    @Test
    @Parameters({"true", "false"})
    public void permitted(boolean permission) {
        when(command.testPermissionSilent(any(CommandSender.class))).thenReturn(permission);
        
        assertEquals(permission, Criteria.PERMITTED.test(command, sender, args));
    }
    
    
    @Test
    @Parameters
    public void permittedPlayer(CommandSender sender, boolean permission, boolean expected) {
        when(command.testPermissionSilent(any(CommandSender.class))).thenReturn(permission);
        
        assertEquals(expected, Criteria.PERMITTED_PLAYER.test(command, sender, args));
    }
    
    protected Object[] parametersForPermittedPlayer() {
        Player player = mock(Player.class);
        return new Object[] {
            new Object[] {player, true, true},
            new Object[] {player, false, false},
            new Object[] {sender, true, false},
            new Object[] {sender, false, false}
        };
    }
    
    
    @Test
    @Parameters({"1, false", "0, true"})
    public void noArguments(int length, boolean expected) {
        assertEquals(expected, Criteria.NO_ARGUMENTS.test(command, sender, new String[length]));
    }
    
    
    @Test
    @Parameters({"1, true", "0, false", "2, false"})
    public void within(int length, boolean expected) {
        assertEquals(expected, Criteria.within(new String[length], 1, 1));
    }
    
}
