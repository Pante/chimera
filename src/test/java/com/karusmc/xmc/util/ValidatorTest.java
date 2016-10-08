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
package com.karusmc.xmc.util;

import com.karusmc.xmc.core.*;

import junitparams.*;

import org.bukkit.command.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
@RunWith(JUnitParamsRunner.class)
public class ValidatorTest {
    
    private ConfigurableCommand command;
    
    private CommandSender sender;
    private ConsoleCommandSender console;
    
    
    public ValidatorTest() {
        command = mock(ConfigurableCommand.class);
        sender = mock(CommandSender.class);
        console = mock(ConsoleCommandSender.class);
    }
    
    
    @Test
    @Parameters(method = "is_HandlesCriteria_Parameters")
    public void is_HandlesCriteria(boolean criteria, Else handler, boolean expected, int times) {
        boolean returned = Validator.is(criteria, handler, null);
        
        assertEquals(expected, returned);
        verify(handler, times(times)).handle(null);
    }
    
    public Object[] is_HandlesCriteria_Parameters() {
        Else handler = mock(Else.class);
        
        return new Object[]{
            new Object[]{true, handler, true, 0},
            new Object[]{false, handler, false, 1}
        };
    }
    
    
    @Test
    @Parameters(method = "isAllowed_ChecksUser_Parameters")
    public void isAllowed_ChecksUser(XMCommand command, CommandSender sender, boolean expected) {
        boolean returned = Validator.isAllowed(command, sender);
        
        assertEquals(expected, returned);
    }
    
    public Object[] isAllowed_ChecksUser_Parameters() {
        return new Object[] {
            new Object[] {configureCommand(true, true), configureSender(true), true}
        };
    }
    
    
    public ConfigurableCommand configureCommand(boolean consoleAllowed, boolean hasPermission) {
        when(command.isConsoleAllowed()).thenReturn(consoleAllowed);
        when(command.testPermissionSilent(any(CommandSender.class))).thenReturn(hasPermission);
        
        return command;
    }
    
    public CommandSender configureSender(boolean isPlayer) {
        if (isPlayer) {
            return sender;
        } else {
            return console;
        }
    }
    
}
