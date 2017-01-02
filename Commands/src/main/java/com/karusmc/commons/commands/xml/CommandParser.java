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

import com.karusmc.commons.commands.Command;
import com.karusmc.commons.core.xml.Parser;

import java.util.Map;

import org.jdom2.Element;


public class CommandParser extends Parser<Map<String, Command>> {
    
    private Map<String, Command> commands;
    
    
    public CommandParser(Map<String, Command> commands) {
        super(null);
        schemaPath = getClass().getClassLoader().getResource("commands.xsd").getPath();
        this.commands = commands;
    }
    
    public CommandParser(String schemaPath, Map<String, Command> commands) {
        super(schemaPath);
        this.commands = commands;
    }
    

    @Override
    protected Map<String, Command> parse(Element element) {
        //TODO
    }
    
}
