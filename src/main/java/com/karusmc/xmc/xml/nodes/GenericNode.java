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
import com.karusmc.xmc.xml.tags.Tag;

import java.util.*;
import javax.xml.stream.*;
import javax.xml.stream.events.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class GenericNode implements Node<XMCommand> {
    
    private String name;
    
    private Map<String, Node> nodes;
    private Map<String, Tag> tags;
    
    
    public GenericNode(String name) {
        this.name = name;
        
        nodes = new HashMap<>();
        tags = new HashMap<>();
    }
    
    
    @Override
    public void parse(XMLEventReader reader, XMCommand command) throws XMLStreamException {
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            
            if (event.isStartElement()) {
                String name = event.asStartElement().getName().getLocalPart();
                
                if (nodes.containsKey(name)) {
                    nodes.get(name).parse(reader, command);
                    
                } else if (tags.containsKey(name)) {
                    tags.get(name).parse(event.asStartElement(), command);
                }
                
            } else if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals(name)) {
                break;
            }
        }
    }
    
    
    public Map<String, Node> getNodes() {
        return nodes;
    }
    
    public Map<String, Tag> getTags() {
        return tags;
    }
    
}
