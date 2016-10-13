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
package com.karusmc.xmc.xml.tags;

import com.karusmc.xmc.core.ConfigurableCommand;
import com.karusmc.xmc.xml.XMLResource;

import junitparams.*;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
@RunWith(JUnitParamsRunner.class)
public class CooldownTagTest {
    
    @ClassRule
    public static XMLResource resource = new XMLResource("xml/tags/cooldown.xml");
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private CooldownTag tag;
    private ConfigurableCommand command;
    
    
    public CooldownTagTest() {
        tag = new CooldownTag();
        command = mock(ConfigurableCommand.class);
    }
    
    
    @Test
    @Parameters({"1, 1, 10", "2, 1, 0", "3, 0, -5"})
    public void parse(String id, int times, long cooldown) {
        resource.findCase(id);
        
        tag.parse(resource.find("cooldown"), command);
        
        verify(command, times(times)).setCooldown(cooldown);
    }
    
    
    @Test
    public void parse_ThrowsException() {
        exception.expect(NumberFormatException.class);
        
        resource.findCase("4");
        tag.parse(resource.find("cooldown"), command);
    }
    
}
