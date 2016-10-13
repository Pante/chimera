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

import java.util.Arrays;

import javax.xml.namespace.QName;
import javax.xml.stream.events.StartElement;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class MetaTag implements Tag {

    @Override
    public void parse(StartElement element, XMCommand command) {
        
        String aliases;
        if ((aliases = element.getAttributeByName(new QName("aliases")).getValue()).length() != 0) {
            command.setAliases(Arrays.asList(aliases.split("\\s*,\\s*")));
        } else {
            command.setAliases(Arrays.<String>asList());
        }
        
        command.setDescription(element.getAttributeByName(new QName("description")).getValue());
        command.setUsage(element.getAttributeByName(new QName("usage")).getValue());
    }
    
}
