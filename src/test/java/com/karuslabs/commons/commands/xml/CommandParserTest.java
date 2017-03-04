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
package com.karuslabs.commons.commands.xml;

import com.karuslabs.commons.commands.Command;
import com.karuslabs.commons.core.xml.SetterComponent;

import java.util.*;

import org.jdom2.Element;

import org.junit.Test;

import static org.mockito.Mockito.*;


public class CommandParserTest {
    
    private CommandParser parser;
    private SetterComponent<Map<String, Command>> component;
    
    
    public CommandParserTest() {
        component = mock(SetterComponent.class);
        parser = new CommandParser(component);
    }
    
    
    @Test
    public void parse() {
        Element element = mock(Element.class);
        Map<String, Command> commands = new HashMap<>(0);
        
        parser.parse(element, commands);
        
        verify(component, times(1)).parse(element, commands);
    }
    
}
