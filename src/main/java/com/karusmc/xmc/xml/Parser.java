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
import com.karusmc.xmc.xml.nodes.Node;

import java.io.File;
import java.util.Map;
import javax.xml.stream.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class Parser {
    
    private WstxInputFactory factory;
    
    private ParserRegister register;
    private Node node;
    
    
    public Parser(Node node, String dtd, String path) {
        this(node, new XMLResolver() {
            @Override
            public Object resolveEntity(String publicID, String systemID, String baseURI, String namespace) throws XMLStreamException {
                if (systemID.equals(dtd)) {
                    return getClass().getClassLoader().getResourceAsStream(path);
                    
                } else {
                    throw new ParserException("Invalid DTD: " + systemID);
                }
            }
        });
    }
    
    public Parser(Node node, XMLResolver resolver) {
        this.register = new ParserRegister();
        this.node = node;
        
        factory = new WstxInputFactory();
        factory.setXMLResolver(resolver);
        factory.setProperty(WstxInputFactory.IS_VALIDATING, true);
    }
    
    
    public void parse(File file) {
        try (ClosableXMLEventReader reader = new ClosableXMLEventReader(factory.createXMLEventReader(file))) {
            node.parse(reader, register);
            
        } catch (XMLStreamException e) {
            throw new ParserException("An error occurred while attempting to parse an XML Document", e);
        }
    }
    
    
    public void register(XMCommand command) {
        register.getCommands().put(command.getName(), command);
    }
    
    public void unregister(String name) {
        register.getCommands().remove(name);
    }
    
    
    public void registerAll(Map<String, XMCommand> commands) {
        register.getCommands().putAll(commands);
    }
    
    public void unregisterAll() {
        register.getCommands().clear();
    }
    
}
