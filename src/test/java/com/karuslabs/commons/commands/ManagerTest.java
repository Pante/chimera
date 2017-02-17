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
package com.karuslabs.commons.commands;

import com.karuslabs.commons.commands.CommandMapProxy;
import com.karuslabs.commons.commands.Manager;
import com.karuslabs.commons.commands.Command;
import com.karuslabs.commons.core.xml.SetterParser;

import java.io.File;
import java.util.*;

import org.junit.Test;

import static org.mockito.Mockito.*;


public class ManagerTest {
    
    private Manager manager;
    private CommandMapProxy proxy;
    private SetterParser<Map<String, Command>> parser;
    
    
    public ManagerTest() {
        proxy = mock(CommandMapProxy.class);
        parser = mock(SetterParser.class);
        
        manager = new Manager(proxy, parser);
    }
    
    
    @Test
    public void load() {
        File file = mock(File.class);
        Map<String, Command> commands = new HashMap<>(0);
        
        manager.load(file, commands);
        
        verify(parser, times(1)).parse(file, commands);
        verify(proxy, times(1)).register(commands);
    }
    
}
