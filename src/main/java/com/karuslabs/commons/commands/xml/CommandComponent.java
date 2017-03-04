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

import com.karuslabs.commons.core.xml.*;
import com.karuslabs.commons.commands.*;

import java.util.*;

import org.jdom2.Element;


/**
 * Represents a component for parsing <code>&ltcommand&gt</code> nodes.
 */
public class CommandComponent implements SetterComponent<Command> {
    
    private SetterComponent<Map<String, Command>> subcomponent;
    
    
    /**
     * Creates a new component with a component for parsing <code>&ltcommand&gt</code> nodes specified.
     * 
     * @param component the component for parsing <code>&ltcommand&gt</code> nodes
     */
    public CommandComponent(SetterComponent<Map<String, Command>> component) {
        this.subcomponent = component;
    }
    
    
    /**
     * Parses a <code>&ltcommand&gt</code> node and sets the associated values for the <code>Command</code> specified.
     * 
     * @param element the <code>&ltcommand&gt</code>
     * @param command the values of a <code>Command</code> to set
     */
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

    
    /**
     * @return the component used to parse <code>&ltcommand&gt</code> nodes.
     */
    public SetterComponent<Map<String, Command>> getSubcomponent() {
        return subcomponent;
    }
  
}
