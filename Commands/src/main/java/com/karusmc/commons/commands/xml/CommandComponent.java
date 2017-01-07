/*
 * Copyright (C) 2017 PanteLegacy @ karusmc.com
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
package com.karusmc.commons.commands.xml;

import com.karusmc.commons.commands.*;
import com.karusmc.commons.core.xml.*;

import java.util.*;

import org.jdom2.Element;


public class CommandComponent implements SetterComponent<Command> {
    
    private SetterComponent<Map<String, Command>> commandsComponent;
    
    
    public CommandComponent(SetterComponent<Map<String, Command>> component) {
        this.commandsComponent = component;
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
                commandsComponent.parse(commands, ((Marshall) command).getCommands());
                
            } else {
                throw new ParserException("Command: \"" + command.getName() + "\" does not implement the Marshall interface, unable to retrieve child commands");
            }
        }
    }

    
    public SetterComponent<Map<String, Command>> getCommandsComponent() {
        return commandsComponent;
    }

    public void setCommandsComponent(SetterComponent<Map<String, Command>> commandsComponent) {
        this.commandsComponent = commandsComponent;
    }
    
    
    
    
}
