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
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;


/**
 * Represents a parser which parses a XML Document and returns a parsed object.
 * @see SetterParser
 * 
 * @param <ParsedObject> the type of the parsed object
 */
public abstract class Parser<ParsedObject> {
    
    protected String schemaPath;
    protected SAXBuilder builder;
    
    
    /**
     * Creates a new, schema validating parser with the XML schema specified.
     * 
     * @param schemaPath the XML schema path
     */
    public Parser(String schemaPath) {
        this(schemaPath, new SAXBuilder(XMLReaders.XSDVALIDATING));
    }
    
    /**
     * Creates a new parser with the XML schema and SAXBuilder specified.
     * 
     * @param schemaPath the XML schema path
     * @param builder the SAXBuilder
     */
    public Parser(String schemaPath, SAXBuilder builder) {
        this.schemaPath = schemaPath;
        this.builder = builder;
    }
    
    
    /**
     * Parses a XML document and returns a parsed object.
     * 
     * @param file the XML document
     * @return the parsed object
     */
    public ParsedObject parse(File file) {
        try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file))) {
            return parse(stream);
            
        } catch (IOException e) {
            throw new ParserException("Failed to parse file: " + file.getName(), e);
        }
    }
    
    /**
     * Parses a XML document from the <code>inputstream</code> specified and returns a parsed object.
     * 
     * @param stream the <code>inputstream</code>
     * @return the parsed object
     */
    public ParsedObject parse(InputStream stream) {
        try {
            Element element = builder.build(stream, schemaPath).getRootElement();
            return parse(element);
            
        } catch (JDOMException | IOException e) {
            throw new ParserException("Failed to parse XML Document", e);
        }
    }
    
    /**
     * Parses a XML document starting from the root element specified and returns a parsed object.
     * 
     * @param element the root element of a XML document
     * @return the parsed object
     */
    protected abstract ParsedObject parse(Element element);
    
}