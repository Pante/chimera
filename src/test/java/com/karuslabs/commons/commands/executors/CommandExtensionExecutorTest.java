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
package com.karuslabs.commons.commands.executors;

import com.karuslabs.commons.commands.extensions.Extension;

import java.util.*;

import junitparams.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class CommandExtensionExecutorTest {
    
    private CommandExtensionExecutor executor;
    private CommandExecutor delegated;
    private Map<String, Extension> extensions;
    private Extension extension;
    
    
    public CommandExtensionExecutorTest() {
        executor = new CommandExtensionExecutor(delegated = mock(CommandExecutor.class));
        
        extension = mock(Extension.class);
        
        extensions = new HashMap<>();
        extensions.put("name", extension);
        
        executor.setExtensions(extensions);
    }
    
    
    @Test
    @Parameters
    public void execute(String[] args, int extensionTimes, int delegatedTimes) {
        executor.execute(null, null, null, args);
        
        verify(extension, times(extensionTimes)).execute(any(), any(), any(), any());
        verify(delegated, times(delegatedTimes)).execute(any(), any(), any(), any());
    }
    
    protected Object[] parametersForExecute() {
        return new Object[] {
            new Object[] {new String[] {"name"}, 1, 0},
            new Object[] {new String[] {""}, 0, 1},
            new Object[] {new String[] {}, 0, 1}
        };
    }
    
}
