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
package com.karusmc.xmc.xml.tags;

import com.karusmc.xmc.core.XMCommand;

import java.util.*;
import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class BlockTag implements Tag {
    
    private Map<QName, Tag> tags;
    private String tagName;
    
    
    public BlockTag(String tagName) {
        tags = new HashMap<>();
        this.tagName = tagName;
    }
    
    
    @Override
    public void parse(XMLEventReader reader, XMCommand command) throws XMLStreamException {
        while(reader.hasNext()) {
            XMLEvent event = reader.peek();
            
            QName name;
            if (event.isStartElement() && tags.containsKey((name = event.asStartElement().getName()))) {
                tags.get(name).parse(reader, command);
                
            } else if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals(tagName)) {
                break;
                
            } else {
                reader.nextEvent();
            }
        }
    }
    
    
    public Map<QName, Tag> getTags() {
        return tags;
    }
    
}
