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

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class ClosableXMLEventReader implements XMLEventReader, AutoCloseable {

    private final XMLEventReader internal;

    public ClosableXMLEventReader(XMLEventReader internal) {
        this.internal = internal;
    }

    @Override
    public XMLEvent nextEvent() throws XMLStreamException {
        return internal.nextEvent();
    }

    @Override
    public boolean hasNext() {
        return internal.hasNext();
    }

    @Override
    public XMLEvent peek() throws XMLStreamException {
        return internal.peek();
    }

    @Override
    public String getElementText() throws XMLStreamException {
        return internal.getElementText();
    }

    @Override
    public XMLEvent nextTag() throws XMLStreamException {
        return internal.nextTag();
    }

    @Override
    public Object getProperty(String name) throws IllegalArgumentException {
        return internal.getProperty(name);
    }

    @Override
    public void close() throws XMLStreamException {
        internal.close();
    }

    @Override
    public Object next() {
        return internal.next();
    }
    
}
