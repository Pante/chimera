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

import java.util.*;
import javax.xml.namespace.QName;
import javax.xml.stream.events.StartElement;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class WorldsTag implements Tag {
    
    private QName type;
    private QName list;
    
    
    public WorldsTag() {
        type = new QName("type");
        list = new QName("list");
    }
    
    
    @Override
    public void parse(StartElement element, XMCommand xmCommand) {
        ConfigurableCommand command = (ConfigurableCommand) xmCommand;
        
        if (element.getAttributeByName(type).getValue().equals("blacklist")) {
            command.setBlacklist(true);
        } else {
            command.setBlacklist(false);
        }
        
        command.setWorlds(new HashSet<>(Arrays.asList(element.getAttributeByName(list).getValue().split("\\s*,\\s*"))));
    }
    
}
