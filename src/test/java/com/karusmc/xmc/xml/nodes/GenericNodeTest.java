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

import com.karusmc.xmc.xml.XMLResource;
import com.karusmc.xmc.xml.tags.Tag;

import javax.xml.stream.XMLStreamException;

import org.junit.*;

import static org.mockito.Mockito.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class GenericNodeTest {
    
    @ClassRule
    public static XMLResource resource = new XMLResource("xml/nodes/genericnode.xml");
    
    private GenericNode node;
    
    private Node mockNode;
    private Tag mockTag;
    
    
    public GenericNodeTest() {
        node = new GenericNode("node");
        
        node.getNodes().put("node", mockNode = mock(Node.class));
        node.getTags().put("tag", mockTag = mock(Tag.class));
    }
    
    
    @Test
    public void parse() throws XMLStreamException {
        resource.find("node");
        
        node.parse(resource.getReader(), null);
        
        verify(mockTag, times(1)).parse(any(), any());
        verify(mockNode, times(1)).parse(any(), any());
    }
    
}
