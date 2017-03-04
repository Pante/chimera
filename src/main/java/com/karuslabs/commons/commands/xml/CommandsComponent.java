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

import com.karuslabs.commons.xml.SetterComponent;
import com.karuslabs.commons.xml.ParserException;
import com.karuslabs.commons.commands.Command;

import java.util.*;

import org.jdom2.Element;


public class CommandsComponent implements SetterComponent<Map<String, Command>>{
    
    private SetterComponent<Command> component;
    
    
    public CommandsComponent() {
        component = new CommandComponent(this);
    }
    
    public CommandsComponent(SetterComponent<Command> component) {
        this.component = component;
    }
    
    
    @Override
    public void parse(Element root, Map<String, Command> commands) {
        root.getChildren("command").forEach((element) -> {
            String name = element.getAttribute("name").getValue();  
            
            if (commands.containsKey(name)) { 
                component.parse(element, commands.get(name));

            } else {
                throw new ParserException("No such registered command with name: \"" + name + "\"");
            }
        });
    }
    
}
