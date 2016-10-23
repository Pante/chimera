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
package com.karusmc.xmc.xml.nodes;

import com.karusmc.xmc.core.XMCommand;
import com.karusmc.xmc.xml.XMLResource;

import javax.xml.stream.XMLStreamException;

import org.junit.*;

import static org.mockito.Mockito.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class CommandsNodeTest {
    
    @ClassRule
    public static XMLResource resource = new XMLResource("xml/nodes/commands.xml");
    
    private CommandsNode node;
    private Node mockNode;
    
    private StubDispatcherCommand stubCommand;
    private XMCommand mockCommand;
    
    
    public CommandsNodeTest() {
        mockNode = mock(Node.class);
        node = new CommandsNode(mockNode);
        
        stubCommand = new StubDispatcherCommand();
        mockCommand = mock(XMCommand.class);
        
        stubCommand.getCommands().put("subcommand", mockCommand);
    }
    
    
    @Test
    public void parse() throws XMLStreamException {
        resource.find("commands");
        node.parse(resource.getReader(), stubCommand);
        
        verify(mockNode, times(1)).parse(resource.getReader(), mockCommand);
    }
    
}
