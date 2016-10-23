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

import com.ctc.wstx.stax.WstxInputFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.junit.rules.ExternalResource;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class XMLResource extends ExternalResource {
    
    private WstxInputFactory factory;
    private XMLEventReader reader;
    private String path;
    
    
    public XMLResource(String path) {
        this.path = path;
        factory = new WstxInputFactory();
    }
    
    public XMLResource(String path, String dtd) {
        this.path = path;
        
        factory = new WstxInputFactory();
        factory.setXMLResolver((String publicID, String systemID, String baseURI, String namespace) -> getClass().getClassLoader().getResourceAsStream(path));
        factory.setProperty(WstxInputFactory.IS_VALIDATING, true);
    }
    
    
    @Override
    protected void before() throws Throwable {
        reader = factory.createXMLEventReader(getClass().getClassLoader().getResourceAsStream(path));
    }
    
    
    @Override
    protected void after() {
        try {
            reader.close();
        } catch (XMLStreamException e) {
            throw new RuntimeException();
        }
    }
    
    
    public XMLEventReader getReader() {
        return reader;
    }
    
    
    public void findCase(String id) {
        try {
            while(reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                StartElement element;
                if (event.isStartElement() && (element = event.asStartElement()).getName().getLocalPart().equals("case") 
                        && element.getAttributeByName(new QName("id")).getValue().equals(id)) {
                    
                    return;
                }
            }
            
            throw new RuntimeException("Failed to find case with id: " + id);
            
        } catch (XMLStreamException e) {
            throw new RuntimeException("An error occured while trying to find case with id: " + id);
        }
    }
    
    public StartElement find(String name) {
        try {
            while(reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                StartElement element;
                if (event.isStartElement() && (element = event.asStartElement()).getName().getLocalPart().equals(name)) {
                    return element;
                }
            }
            
            throw new RuntimeException("Failed to find tag with name: " + name);
            
        } catch (XMLStreamException e) {
            throw new RuntimeException("An error occured while trying to find tag with name: " + name);
        }
    }
    
}
