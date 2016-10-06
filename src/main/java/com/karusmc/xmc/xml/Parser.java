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

import com.karusmc.xmc.core.*;
import com.karusmc.xmc.xml.tags.Tag;

import com.ctc.wstx.stax.WstxInputFactory;

import java.io.*;
import java.util.*;
import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class Parser {
    
    private Map<String, XMCommand> commands;
    private Tag commandTag;
    private WstxInputFactory factory;
    
    
    public Parser(Tag commandTag, String dtd, String path) {
        this.commandTag = commandTag;
        
        commands = new HashMap<>();
        factory = new WstxInputFactory();
        
        factory.setXMLResolver((String publicID, String systemID, String baseURI, String namespace) -> {
            if (systemID.equals(dtd)) {
                return getClass().getClassLoader().getResourceAsStream(path);
            } else {
                throw new ParserException("Invalid DTD: " + systemID);
            }
        });
        
        factory.setProperty(XMLInputFactory.IS_VALIDATING, true);
    }
    
    
    
    public void parse(File file) {
        XMLEventReader reader = null;
        
        try {
            reader = factory.createXMLEventReader(file);
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                StartElement element;
                if (event.isStartElement() && (element = event.asStartElement()).getName().getLocalPart().equals("command")) {
                    
                    String name = element.getAttributeByName(new QName("name")).getValue();
                    if (commands.containsKey(name)) {
                        commandTag.parse(reader, commands.get(name));
                    }
                }
            }
            
        } catch (XMLStreamException e) {
            throw new ParserException("An error occurred while attempting to parse an XML Document", e);
            
        } finally {
            try {
                reader.close();
            } catch (XMLStreamException e) {
                throw new ParserException("An error occured while trying to close underlying XML stream", e);
            }
        }
    }
    
}
