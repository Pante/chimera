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
package com.karuslabs.commons.test;

import com.karuslabs.commons.xml.ParserException;

import java.io.*;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;

import org.junit.rules.ExternalResource;


/**
 * Represents a XML resource.
 */
public class XMLResource extends ExternalResource {
    
    private Document document;
    
    
    /**
     * Loads the XML document with the XML schema specified.
     * Setting the schema path as <code>null</code> will load the document without validation.
     * 
     * @param file the XML document
     * @param schemaPath the XML schema path
     * @return this
     */
    public XMLResource load(File file, String schemaPath) {
        try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file))) {
            load(stream, schemaPath);
            
            return this;
            
        } catch (IOException e) {
            throw new ParserException("Failed to parse file: " + file.getName(), e);
        }
    }
    
    
    /**
     * Loads the XML document from the <code>inputstream</code> with the XML schema specified.
     * Setting the schema path as <code>null</code> will load the document without validation.
     * 
     * @param stream the inputstream
     * @param schemaPath the XML schema path
     * @return this
     */
    public XMLResource load(InputStream stream, String schemaPath) {
        try {
            if (schemaPath == null) {
                document = new SAXBuilder(XMLReaders.NONVALIDATING).build(stream);
            } else {
                document = new SAXBuilder(XMLReaders.XSDVALIDATING).build(stream, schemaPath);
            }
            
            return this;

        } catch (IOException | JDOMException e) {
            throw new ParserException("Failed to parse XML Document", e);
        }
    }
    
    
    /**
     * @return the currently loaded document
     */
    public Document getDocument() {
        return document;
    }
    
    /*
     * @return the root element of the currently loaded document
     */
    public Element getRoot() {
        return document.getRootElement();
    }
    
}
