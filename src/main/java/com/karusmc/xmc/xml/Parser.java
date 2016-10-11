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

import com.ctc.wstx.stax.WstxInputFactory;

import com.karusmc.xmc.core.XMCommand;
import com.karusmc.xmc.xml.tags.Tag;

import java.io.File;
import java.util.*;
import javax.xml.stream.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class Parser {
    
    private ParserRegister register;
    private Tag baseTag;
    
    private WstxInputFactory factory;
    
    
    public Parser(Tag baseTag, String dtd, String path) {
        register = new ParserRegister();
        this.baseTag = baseTag;
        
        factory = new WstxInputFactory();
        factory.setXMLResolver((String publicID, String systemID, String baseURI, String namespace) -> {
            if (systemID.equals(dtd)) {
                return getClass().getClassLoader().getResourceAsStream(path);
            } else {
                throw new ParserException("Invalid DTD: " + systemID);
            }
        });
        
        factory.setProperty(WstxInputFactory.IS_VALIDATING, true);
    }
    
    
    public void parse(File file) {
        XMLEventReader reader = null;
        
        try {
            reader = factory.createXMLEventReader(file);
            baseTag.parse(reader, register);
            
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
    
    
    public Map<String, XMCommand> getCommands() {
        return register.getCommands();
    }
    
    public void setCommands(Map<String, XMCommand> commands) {
        register.setCommands(commands);
    }
    
}
