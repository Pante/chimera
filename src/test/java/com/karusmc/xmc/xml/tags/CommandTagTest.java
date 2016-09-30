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
package com.karusmc.xmc.xml.tags;

import java.util.*;
import javax.xml.stream.XMLStreamException;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
@RunWith(Parameterized.class)
public class CommandTagTest extends TagBase<CommandTag> {
    
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {"xml/tags/command/withAliases.xml", "case 1", Arrays.asList("1", "2", "3")},
            {"xml/tags/command/withEmptyAliases.xml", "case 2", Collections.emptyList()},
            {"xml/tags/command/noAliases.xml", "case 3", Collections.emptyList()}
        });
    }
    
    
    
    private String expectedName;
    private List<String> expectedAliases;
    
    
    public CommandTagTest(String fileName, String expectedName, List<String> expectedAliases) {
        super(new CommandTag(), fileName);
        
        this.expectedName = expectedName;
        this.expectedAliases = expectedAliases;
    }
    
    
    @Test
    public void getter_ReturnsList() throws XMLStreamException {
        assertEquals(expectedName, tag.getName());
        assertEquals(expectedAliases.size(), tag.getAliases().size());
        assertTrue(expectedAliases.containsAll(tag.getAliases()));
    }
    
}
