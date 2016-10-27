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
    
    @Test
    @Parameters(source = PlayerProvider.class)
    public void canUse(XMCommand command, CommandSender sender, boolean expected) {
        boolean returned = Validator.canUse(command, sender);
        
        assertEquals(expected, returned);
    }

    
    @Test
    @Parameters(source = WorldsProvider.class)
    public void canUseInWorld(ConfigurableCommand command, CommandSender sender, boolean expected) {
        boolean returned = Validator.canUseInWorld(command, sender);
        
        assertEquals(expected, returned);
    }

    
    @Test
    @Parameters({"0, 0, 2, true", "0, 2, 2, true", "1, 0, 2, false", "1, 3, 2, false"})
    public void hasLength(int min, int length, int max, boolean expected) {
        boolean returned = Validator.hasLength(min, length, max);
        
        assertEquals(expected, returned);
    }
    
    
    @Test
    @Parameters({"1, 0, false", "0, 1, true", "0, 0, false"})
    public void hasCooldown(long currentTime, long lastUse, boolean expected) {
        ConfigurableCommand command = mock(ConfigurableCommand.class);
        when(command.getCooldown()).thenReturn((long) 0);
        
        boolean returned = Validator.hasCooldown(command, currentTime, lastUse);
        
        assertEquals(expected, returned);
    }
    
}
