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
import java.util.*;

import org.jdom2.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class CommandsStratergy implements ParserStratergy {
    
    @Override
    public void parse(Element root, Map<String, XMCommand> commands) {
        List<Element> commandElements = root.getChildren("command");
        commandElements.stream().forEach(element -> {
            
            String name = element.getAttribute("name").getValue();
            if (commands.containsKey(name)) {
                XMCommand command = commands.get(name);
                
                parseMeta(elemen, command);
                
                
                
//                Element configurationElement = element.getChild("configuration");
//                configurationElement.getChild("cooldown").getAttribute("time").getLongValue();
                if (command instanceof Dispatcher) {
                    parse(element.getChild("commands"), ((Dispatcher) command).getCommands());
                }
                
                
            }
        });
    }
    
    
    private void parseMeta(Element metaElement, XMCommand command) {
        command.setAliases(Arrays.asList(metaElement.getAttribute("aliases").getValue().split("\\s*,\\s*")));
        command.setDescription(metaElement.getAttribute("description").getValue());
        command.setUsage(metaElement.getAttribute("usage").getValue());
    }
    
    private void parsePermissions(Element permissionElement, XMCommand command) {
        command.setPermission(permissionElement.getAttribute("permission").getValue());
        command.setPermissionMessage(permissionElement.getAttribute("message").getValue());
        command.setConsoleAllowed(permissionElement.getAttribute("console-allowed").getBooleanValue());
    }
    
}
