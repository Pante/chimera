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

import com.karuslabs.commons.xml.*;
import com.karuslabs.commons.commands.Command;

import java.util.*;

import org.jdom2.Element;


/**
 * Represents a parser for parsing XML documents to <code>Command</code>s.
 */
public class CommandParser extends SetterParser<Map<String, Command>> {
    
    private SetterComponent<Map<String, Command>> component;
    
    
    /**
     * Creates a new parser with the component for parsing <code>&lt;commands&gt;</code> nodes specified.
     * 
     * @param component the component for parsing &lt;commands&gt; nodes
     */
    public CommandParser(SetterComponent<Map<String, Command>> component) {
        super(null);
        
        schemaPath = getClass().getClassLoader().getResource("commands/commands.xsd").getPath();
        this.component = component;
    }
    
    /**
     * Creates a new parser with the component for parsing <code>&lt;commands&gt;</code> nodes and XML schema specified.
     * 
     * @param component the component for parsing &lt;commands&gt; nodes
     * @param schemaPath the XML schema path
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
