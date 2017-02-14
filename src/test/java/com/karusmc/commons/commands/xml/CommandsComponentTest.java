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
package com.karusmc.commons.commands.xml;

import com.karusmc.commons.commands.Command;

import com.karusmc.commons.core.xml.*;
import com.karusmc.commons.core.test.XMLResource;

import java.util.*;

import org.jdom2.Element;

import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.*;


public class CommandsComponentTest {
    
    @Rule
    public XMLResource resource = new XMLResource().load(getClass().getClassLoader().getResourceAsStream("commands/commands-component.xml"), null);
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private CommandsComponent component;
    private SetterComponent<Command> setter;
    private Map<String, Command> commands;
    
    
    public CommandsComponentTest() {
        setter = mock(SetterComponent.class);
        commands = new HashMap<>(1);
        
        component = new CommandsComponent(setter);
    }
    
    
    @Before
    public void setup() {
        commands.clear();
    }
    
    
    @Test
    public void parse() {
        commands.put("subcommand", mock(Command.class));
        component.parse(resource.getRoot().getChild("commands"), commands);
        
        verify(setter, times(1)).parse(any(Element.class), any(Command.class));
    }
    
    
    @Test
    public void parse_ThrowsException() {
        exception.expect(ParserException.class);
        exception.expectMessage("No such registered command with name: \"subcommand\"");
        
        commands.put("command", mock(Command.class));
        component.parse(resource.getRoot().getChild("commands"), commands);
    }
    
}
