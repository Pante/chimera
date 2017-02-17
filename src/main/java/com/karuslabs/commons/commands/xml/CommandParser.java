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
package com.karuslabs.commons.commands.xml;

import com.karuslabs.commons.core.xml.SetterParser;
import com.karuslabs.commons.core.xml.SetterComponent;
import com.karuslabs.commons.commands.Command;

import java.util.*;

import org.jdom2.Element;


/**
 * Parses a XML Document and transforms it into command information.
 */
public class CommandParser extends SetterParser<Map<String, Command>> {
    
    private SetterComponent<Map<String, Command>> component;
    
    
    /**
     * Constructs this with the specified component and the default XML schema.
     * 
     * @param component The component used to parse commands nodes
     */
    public CommandParser(SetterComponent<Map<String, Command>> component) {
        super(null);
        
        schemaPath = getClass().getClassLoader().getResource("commands/commands.xsd").getPath();
        this.component = component;
    }
   
    
    /**
     * Constructs this with the specified component and XML schema.
     * 
     * @param component The component used to parse commands nodes
     * @param schemaPath The path to the XML schema
     */
    public CommandParser(SetterComponent<Map<String, Command>> component, String schemaPath) {
        super(schemaPath);
        this.component = component;
    }
    
    
    @Override
    protected void parse(Element element, Map<String, Command> commands) {
        component.parse(element, commands);
    }
    
}
