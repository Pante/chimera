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

import com.karusmc.xmc.xml.nodes.Node;

import java.io.File;
import javax.xml.stream.XMLStreamException;

import junitparams.*;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
@RunWith(JUnitParamsRunner.class)
public class ParserTest {
    
    @ClassRule
    public static XMLResource resource = new XMLResource("xml/parser.xml");
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private Parser parser;
    private Node node;
    
    
    public ParserTest() {
        parser = new Parser(node = mock(Node.class), "test.dtd", "xml/test.dtd");
    }
    
    
    @Test
    public void parse_CallsNode() throws XMLStreamException {
        parser.parse(new File(getClass().getClassLoader().getResource("xml/parser.xml").getPath()));
        
        verify(node, times(1)).parse(any(), any());
    }
    
    
    @Test
    @Parameters(method = "parse_ThrowsException_parameters")
    public void parse_ThrowsException(String expectedMessage, Node stubNode, String dtd, String dtdPath, String file) {
        exception.expect(ParserException.class);
        exception.expectMessage(expectedMessage);
        
        new Parser(stubNode, dtd, dtdPath).parse(new File(getClass().getClassLoader().getResource(file).getPath()));
    }
    
    public Object[] parse_ThrowsException_parameters() {
        Node stubNode = (reader, command) -> {
            while (reader.hasNext()) {
                reader.nextEvent();
            }
        };
        
        return new Object[]{
            new Object[] {"Invalid DTD: invalid.dtd", stubNode, "test.dtd", "xml/test.dtd", "xml/invalid.xml"}, 
            new Object[] {"An error occurred while attempting to parse an XML Document", stubNode, "invalid.dtd", "xml/invalid.dtd", "xml/invalid.xml"},
        };
    }
    
    
    
}
