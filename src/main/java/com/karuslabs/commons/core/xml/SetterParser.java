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
package com.karuslabs.commons.core.xml;

import java.io.*;

import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.input.sax.XMLReaders;


public abstract class SetterParser<Argument> {
    
    protected String schemaPath;
    protected SAXBuilder builder;
    
    
    public SetterParser(String schemaPath) {
        this(schemaPath, new SAXBuilder(XMLReaders.XSDVALIDATING));
    }
    
    public SetterParser(String schemaPath, SAXBuilder builder) {
        this.schemaPath = schemaPath;
        this.builder = builder;
    }
    
    
    
    public void parse(File file, Argument argument) {
        try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file))) {
            parse(stream, argument);
            
        } catch (IOException e) {
            throw new ParserException("Failed to parse file: " + file.getName(), e);
        }
    }
    
    public void parse(InputStream stream, Argument argument) {
        try {
            Element element = builder.build(stream, schemaPath).getRootElement();
            parse(element, argument);
            
        } catch (JDOMException | IOException e) {
            throw new ParserException("Failed to parse XML Document", e);
        }
    }
    
    
    protected abstract void parse(Element element, Argument argument);
    
}
