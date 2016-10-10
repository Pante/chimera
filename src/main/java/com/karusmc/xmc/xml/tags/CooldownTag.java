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

import com.karusmc.xmc.core.*;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class CooldownTag implements Tag {

    @Override
    public void parse(XMLEventReader reader, XMCommand xmCommand) throws XMLStreamException {
        XMLEvent event;
        if (reader.hasNext() && (event = reader.nextEvent()).isStartElement() && xmCommand instanceof ConfigurableCommand) {
            
            StartElement element = event.asStartElement();
            ConfigurableCommand command = (ConfigurableCommand) xmCommand;
            
            command.setCooldown(Long.parseLong(element.getAttributeByName(new QName("time")).getValue()));
        }
    }
    
}
