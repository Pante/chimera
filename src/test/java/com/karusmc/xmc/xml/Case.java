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
package com.karusmc.xmc.xml;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class Case {
    
    public XMLEventReader find(XMLEventReader reader, String id) {
        try {
            while(reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                StartElement element;
                if (event.isStartElement() && (element = event.asStartElement()).getName().getLocalPart().equals("case") 
                        && element.getAttributeByName(new QName("id")).getValue().equals("id")) {
                    break;
                }
            }
            return reader;
            
        } catch (XMLStreamException e) {
            throw new RuntimeException("Failed to find case with ID: " + id);
        }
    }
    
}
