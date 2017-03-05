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

import java.util.Arrays;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class CommandTest {
    
    private Command command;
    
    
    public CommandTest() {
        command = spy(new Command("name", Criteria.NONE) {});
    }
    
    
    @Test
    public void execute() {
        boolean returned = command.execute(null, null, null);
        
        verify(command, times(1)).execute(null, null);
        assertTrue(returned);
    }
    
    @Test
    public void newMethods() {
        command.newAliases(Arrays.asList("new alias"))
                .newDescription("new description")
                .newLabel("new label")
                .newName("new name")
                .newPermission("new permission")
                .newPermissionMessage("new message")
                .newUsage("new usage");
        
        assertEquals(Arrays.asList("new alias"), command.getAliases());
        assertEquals("new description", command.getDescription());
        assertEquals("new label", command.getLabel());
        assertEquals("new name", command.getName());
        assertEquals("new permission", command.getPermission());
        assertEquals("new message", command.getPermissionMessage());
        assertEquals("new usage", command.getUsage());
    }
    
}
