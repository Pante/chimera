/*
 * Copyright (C) 2017 Karus Labs
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
package com.karuslabs.commons.commands.xml;

import com.karuslabs.commons.core.xml.ParserException;
import com.karuslabs.commons.core.xml.SetterComponent;
import com.karuslabs.commons.commands.Command;
import com.karuslabs.commons.commands.Marshall;

import java.util.*;

import org.jdom2.Element;


public class CommandComponent implements SetterComponent<Command> {
    
    private SetterComponent<Map<String, Command>> subcomponent;
    
    
    public CommandComponent(SetterComponent<Map<String, Command>> component) {
        this.subcomponent = component;
    }
    
    
    @Override
    public void parse(Element element, Command command) {
        Element meta = element.getChild("meta");
        Element permission = element.getChild("permission");
        Element commands = element.getChild("commands");
        
        command.setAliases(Arrays.asList(meta.getAttribute("aliases").getValue().split("\\s*,\\s*")));
        command.setDescription(meta.getAttribute("description").getValue());
        command.setUsage(meta.getAttribute("usage").getValue());
        
        command.setPermission(permission.getAttribute("permission").getValue());
        command.setPermissionMessage(permission.getAttribute("message").getValue());
            
        if (commands != null) {
            if (command instanceof Marshall) {
                subcomponent.parse(commands, ((Marshall) command).getCommands());
                
            } else {
                throw new ParserException("Command: \"" + command.getName() + "\" does not implement the Marshall interface, unable to retrieve child commands");
            }
        }
    }

    
    public SetterComponent<Map<String, Command>> getSubcomponent() {
        return subcomponent;
    }
  
}
