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

import com.ctc.wstx.stax.WstxInputFactory;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;

import org.junit.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 * @param <GenericTag>
 */
public abstract class TagBase<GenericTag extends Tag> {
    
    private static final WstxInputFactory FACTORY = new WstxInputFactory();

    private String fileName;
    private XMLEventReader reader;
        
    protected GenericTag tag;
    
    
    public TagBase(GenericTag tag, String fileName) {
        this.tag = tag;
        this.fileName = fileName;
    }
    
    
    @Before
    public void setup() throws XMLStreamException {
        reader = FACTORY.createXMLEventReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));
        find();
    }
    
    
    @After
    public void closeStream() throws XMLStreamException {
        reader.close();
    }
    
    
    public void find() throws XMLStreamException {
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            
            if (tag.isElement(event)) {
                tag.set(event.asStartElement());
                break;
            }
        }
    }
    
}
