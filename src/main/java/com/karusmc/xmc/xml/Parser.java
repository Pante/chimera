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
import com.karusmc.xmc.xml.tags.Tag;

import com.ctc.wstx.stax.WstxInputFactory;

import java.io.*;
import java.util.*;
import javax.xml.stream.*;

import org.bukkit.command.CommandSender;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class Parser extends XMCommand implements Dispatcher {
    
    private Map<String, XMCommand> commands;
    private Tag commandsTag;
    private WstxInputFactory factory;
    
    
    public Parser(Tag commandsTag, String dtd) {
        super(null, null);
        this.commandsTag = commandsTag;
        
        commands = new HashMap<>();
        factory = new WstxInputFactory();
        
        factory.setXMLResolver((String publicID, String systemID, String baseURI, String namespace) -> {
            if (systemID.equals(dtd)) {
                return getClass().getClassLoader().getResourceAsStream("dtds/" + dtd);
            } else {
                throw new ParserException("Invalid DTD: " + systemID);
            }
        });
        
        factory.setProperty(XMLInputFactory.IS_VALIDATING, true);
    }
    
    
    
    public void parse(File file) {
        XMLEventReader reader = null;
        
        try {
            reader = factory.createXMLEventReader(file);
            commandsTag.parse(reader, this);
            
        } catch (XMLStreamException e) {
            throw new ParserException("An error occurred while attempting to parse an XML Document", e);
            
        } finally {
            try {
                reader.close();
            } catch (XMLStreamException e) {
                throw new ParserException("An error occured while trying to close underlying XML stream", e);
            }
        }
    }

    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, XMCommand> getCommands() {
        return commands;
    }
    
}
