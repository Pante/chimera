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
package com.karusmc.commons.core.xml;

import java.io.*;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;


/**
 * Parses a XML Document and returns a parsed object.
 * 
 * @param <ParsedObject> The object type to be parsed to
 */
public abstract class Parser<ParsedObject> {
    
    protected String schemaPath;
    protected SAXBuilder builder;
    
    
    /**
     * Constructs this with the specified schema and a default schema validating SAXBuilder.
     * 
     * @param schemaPath The schema path
     */
    public Parser(String schemaPath) {
        this(schemaPath, new SAXBuilder(XMLReaders.XSDVALIDATING));
    }
    
    /**
     * Constructs this with the specified schema and SAXBuilder.
     * 
     * @param schemaPath The schema path
     * @param builder The SAXBuilder
     */
    public Parser(String schemaPath, SAXBuilder builder) {
        this.schemaPath = schemaPath;
        this.builder = builder;
    }
    
    
    /**
     * Parses a file and returns a parsed object.
     * 
     * @param file The file to parse
     * @return The parsed object
     */
    public ParsedObject parse(File file) {
        try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file))) {
            return parse(stream);
            
        } catch (IOException e) {
            throw new ParserException("Failed to parse file: " + file.getName(), e);
        }
    }
    
    /**
     * Parses an inputstream and returns a parsed object.
     * 
     * @param stream The inputstream to parse
     * @return The parsed object
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
     * Contains the parsing logic. Used in the template methods, {@link #parse(java.io.File)} and {@link #parse(java.io.InputStream)}.
     * 
     * @param element The element to parse
     * @return The parsed object
     */
    protected abstract ParsedObject parse(Element element);
    
}