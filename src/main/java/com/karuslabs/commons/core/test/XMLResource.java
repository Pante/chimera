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
package com.karuslabs.commons.core.test;

import com.karuslabs.commons.core.xml.ParserException;

import java.io.*;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;

import org.junit.rules.ExternalResource;


public class XMLResource extends ExternalResource {
    
    private Document document;
    
    
    public XMLResource load(File file, String schemaPath) {
        try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file))) {
            load(stream, schemaPath);
            
            return this;
            
        } catch (IOException e) {
            throw new ParserException("Failed to parse file: " + file.getName(), e);
        }
    }
    
    
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
    
    
    public Document getDocument() {
        return document;
    }
    
    public Element getRoot() {
        return document.getRootElement();
    }
    
}
