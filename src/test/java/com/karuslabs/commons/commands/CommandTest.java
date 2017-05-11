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


import org.bukkit.plugin.Plugin;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class CommandTest {
    
    private Command command;
    private Plugin plugin;
    private CommandExecutor executor;
    
    
    public CommandTest() {
        plugin = mock(Plugin.class);
        executor = mock(CommandExecutor.class);
        
        command = new Command("", plugin, executor);
    }
    
    
    @Test
    public void execute() {
        assertTrue(command.execute(null, null, null));
        verify(executor).execute(null, command, null, null);
    }
    
    
    @Test
    public void tabComplete() {
        command.tabComplete(null, null, null);
        
        verify(executor).tabComplete(null, command, null, null);
    }
    
}
