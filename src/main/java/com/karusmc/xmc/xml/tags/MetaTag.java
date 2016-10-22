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
import javax.xml.stream.events.StartElement;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class MetaTag implements Tag {
    
    private QName aliases;
    private QName description;
    private QName usage;
    
    
    public MetaTag() {
        aliases = new QName("aliases");
        description = new QName("description");
        usage = new QName("usage");
    }
    
    
    @Override
    public void parse(StartElement element, XMCommand command) {
        
        String aliases;
        if ((aliases = element.getAttributeByName(this.aliases).getValue()).length() != 0) {
            command.setAliases(Arrays.asList(aliases.split("\\s*,\\s*")));
        } else {
            command.setAliases(new ArrayList<>());
        }
        
        command.setDescription(element.getAttributeByName(description).getValue());
        command.setUsage(element.getAttributeByName(usage).getValue());
    }
    
}
