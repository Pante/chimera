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

import com.karusmc.xmc.core.*;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class CommandsNode implements Node {
    
    private Node node;
    
    
    public CommandsNode(Node node) {
        this.node = node;
    }
    
    
    @Override
    public void parse(XMLEventReader reader, XMCommand command) throws XMLStreamException {
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            
            StartElement element;
            if (event.isStartElement() && (element = event.asStartElement()).getName().getLocalPart().equals("command")) {
                
                String name = element.getAttributeByName(new QName("name")).getValue();
                
                Dispatcher dispatcher;
                if (command instanceof Dispatcher && (dispatcher = (Dispatcher) command).getCommands().containsKey(name)) {
                    node.parse(reader, dispatcher.getCommands().get(name));  
                }
               
           } else if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals("commands")) {
               break;
            }
        }
    }
    
}
