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

import static com.karusmc.xmc.util.ValidatorParameters.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
@RunWith(JUnitParamsRunner.class)
public class ValidatorTest {
    
    
    @Test
    @Parameters(method = "is_Parameters")
    public void is(boolean criteria, Else handler, boolean expected, int times) {
        boolean returned = Validator.is(criteria, handler, null);
        
        assertEquals(expected, returned);
        verify(handler, times(times)).handle(null);
    }
    
    public Object[] is_Parameters() {
        Else handler = mock(Else.class);
        
        return new Object[]{
            new Object[]{true, handler, true, 0},
            new Object[]{false, handler, false, 1}
        };
    }
    
    
    @Test
    @Parameters(method = "canUse_Parameters")
    public void canUse(XMCommand command, CommandSender sender, boolean expected) {
        boolean returned = Validator.canUse(command, sender);
        
        assertEquals(expected, returned);
    }
    
    public Object[] canUse_Parameters() {
        return new Object[] {
            canUse_Parameter(true, true, true, true),
            canUse_Parameter(true, false, true, false),
            
            canUse_Parameter(true, true, false, false),
            canUse_Parameter(true, false, false, false),
            
            canUse_Parameter(false, true, true, false),
            canUse_Parameter(false, false, true, true),
            
            canUse_Parameter(false, true, false, false),
            canUse_Parameter(false, false, false, false)
        };
    }
    
    @Test
    @Parameters(method = "canUseInWorld_Parameters")
    public void canUseInWorld(ConfigurableCommand command, CommandSender sender, boolean expected) {
        boolean returned = Validator.canUseInWorld(command, sender);
        
        assertEquals(expected, returned);
    }
    
    public Object[] canUseInWorld_Parameters() {
        return new Object[] {
            canUseInWorld_Parameter(true, true, true, false),
            canUseInWorld_Parameter(true, true, false, false),
            
            canUseInWorld_Parameter(true, false, true, false),
            canUseInWorld_Parameter(true, false, false, false),
            
            canUseInWorld_Parameter(false, true, true, false),
            canUseInWorld_Parameter(false, true, false, false),
            
            canUseInWorld_Parameter(false, false, true, false),
            canUseInWorld_Parameter(false, false, false, false)
        };
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
