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

import java.util.*;
import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class CommandTag extends Tag {
    
    public CommandTag() {
        super("command");
    }
    
    
    public String getName() {
        return element.getAttributeByName(new QName("name")).getValue();
    }
    
    public List<String> getAliases() {
        Attribute aliases = element.getAttributeByName(new QName("aliases"));
        if (aliases != null) {
            
            String[] parsed = aliases.getValue().split("\\s*,\\s*");
            if (parsed.length != 1 || !parsed[0].equals("")) {
                return Arrays.asList(parsed);
            }   
        }
        
        return Collections.emptyList();
    }
    
}
